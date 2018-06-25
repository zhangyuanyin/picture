package com.zyy.pic.common.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zyy.pic.common.PicturePropertyConfig;
import com.zyy.pic.entity.vo.VoFtpConfig;
import com.zyy.pic.util.DateTimeUtil;
import com.zyy.pic.util.FileTools;
import com.zyy.pic.util.FtpUtil;
import com.zyy.pic.util.RedisUtil;
import com.zyy.pic.util.ZipUtil;

@Service
public class FileDataHandler {
	private static final Logger logger = Logger.getLogger(FileDataHandler.class);
	private static final String FILE_NAME = "file_name"; // 文件名称
	private static final String FTP_BASE_PATH = "";
	
	@Autowired
    private RedisUtil redis;
	
	/**
	 * 写入
	 * @param response
	 * @param nameType 
	 * @param type
	 * @param duebillNo 
	 * @param date 
	 * @param config 
	 */
	public void write(HttpResponse response, int nameType, String type, String duebillNo, Date date, PicturePropertyConfig config) {
		InputStream in = null;
		FileOutputStream out = null;
		StringBuilder pathbBuilder = new StringBuilder();
		try {
			String fileName = "";
			in = response.getEntity().getContent();
			Header[] headers = response.getAllHeaders();
			for (Header header : headers) {
				if(FILE_NAME.equals(header.getName())){
					fileName = URLDecoder.decode(header.getValue(), "utf-8");
					break;
				}
			}
			// 判断文件路径是否存在，不存在创建对应路径
			pathbBuilder.append(config.getSvfilepath()).append(DateTimeUtil.getYear(date)).append("/")
						.append(DateTimeUtil.formatDate(date, false)).append("/res/")
						.append(duebillNo).append("/").append(type).append("/");
			File file = new File(pathbBuilder.toString());
			if(!file.isDirectory()){
				file.mkdirs();
			}
			// 判断文件是否存在，不存在创建新文件
			if(!StringUtils.trimToEmpty(fileName).contains(".")) {
				fileName = "." + fileName;
			}
			if(nameType == 0 || nameType == 1) {
				fileName = (nameType == 0 ? "F_front":"B_back") + fileName;
			} else {
				TimeUnit.SECONDS.sleep(1L);
				fileName = new Date().getTime() + fileName;
			}
			file = new File(pathbBuilder.toString() + fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			// 将输入流写入到新文件中
			out = new FileOutputStream(file);
			int i = 0;
			while(!((i = in.read()) == -1)){
				out.write(i);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null)
					out.close();
				if(in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 上传至ftp,由于各家银行上传地址可能不一致，此处使用redis缓存内容，重新定义连接
	 * @param busDate 
	 * @param config 
	 * @return
	 */
	public boolean upload2Ftp(String bank, Date date, String busDate, PicturePropertyConfig config) {
		logger.info("【FTP上传】文件上传开始...");
		FTPClient ftp = new FTPClient();
		VoFtpConfig ftpConfig = (VoFtpConfig) redis.get("bank_"+bank);
		String[] paths = this.genPath(date, config, ftpConfig);
		logger.info("【FTP上传】文件上传至路径:" + paths[4]);
		try {
			ftp = FtpUtil.getConnectionByBanks(ftpConfig);
			if(!ftp.isConnected()) {
				logger.info("【FTP上传】FTP连接异常!!!");
				return false;
			}
			//切换到上传目录
			if (!ftp.changeWorkingDirectory(paths[4])) {
				//如果目录不存在创建目录
				String[] dirs = paths[4].split("/");
				String tmppath = FTP_BASE_PATH;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) continue;
					tmppath += "/" + dir;
					if (!ftp.changeWorkingDirectory(tmppath)) {
						if (!ftp.makeDirectory(tmppath)) {
							return false;
						} else {
							ftp.changeWorkingDirectory(tmppath);
						}
					}
				}
				logger.info("【FTP上传】已切换至上传目录："+tmppath);
			}
			ftp.enterLocalPassiveMode();
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//上传文件
			InputStream in = new FileInputStream(paths[3] + busDate + ".zip");
			if (!ftp.storeFile(busDate + ".zip", in)) {
				return false;
			}
			if(in != null){
				in.close();
			}
			ftp.logout();
		} catch (Exception e) {
			logger.error("【FTP上传】文件上传失败...");
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
				}
			}
		}
		return true;
	}
	
	/**
	 * 檢查FTP指定目錄下文件是否存在
	 * @param bank
	 * @param date
	 * @param config
	 * @return
	 */
	public boolean isFTPFileExist(String bank, Date date, String busDate, PicturePropertyConfig config){
		logger.info("【FTP检查】检查文件是否存在开始...");
		
		String sourcePath = config.getSvfilepath() + DateTimeUtil.getYear(date) +"/" + DateTimeUtil.formatDate(date, false) +"/res/";
		File file = new File(sourcePath);
		if(!file.exists()) {
			logger.info("【FTP检查】本地服务文件不存在，不上传FTP");
			return true;
		}
		
		FTPClient ftp = new FTPClient();
		VoFtpConfig ftpConfig = (VoFtpConfig) redis.get("bank_"+bank);
		String[] paths = this.genPath(date, config, ftpConfig);
		logger.info("【FTP检查】待检查文件：" + paths[4] + busDate + ".zip");
		try {
			ftp = FtpUtil.getConnectionByBanks(ftpConfig);
			if(!ftp.isConnected()) {
				logger.info("【FTP检查】FTP连接异常!!!");
				return true;
			}
			
			ftp.enterLocalActiveMode();
			// 设置文件类型为二进制，与ASCII有区别
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			// 设置编码格式
			ftp.setControlEncoding("GBK");
			// 进入文件所在目录，注意编码格式，以能够正确识别中文目录
			ftp.changeWorkingDirectory(new String(paths[4].getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
			// 检验文件是否存在
			InputStream in = ftp.retrieveFileStream(new String((busDate + ".zip").getBytes("GBK"), FTP.DEFAULT_CONTROL_ENCODING));
			if (in == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
				return false;
			}
			if (in != null) {
				in.close();
				ftp.completePendingCommand();
			}
			logger.info("【FTP检查】文件已存在！");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(ftp != null){
				try {
					ftp.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/**
	 * 压缩指定文件夹下文件
	 * @param busDate 
	 * @param config 
	 */
	public boolean compress(Date date, String busDate, PicturePropertyConfig config) {
		String sourcePath = config.getSvfilepath() + DateTimeUtil.getYear(date) +"/" + DateTimeUtil.formatDate(date, false) +"/res/";
		String zipPath = config.getSvfilepath() + DateTimeUtil.getYear(date) +"/" + DateTimeUtil.formatDate(date, false) +"/";
		File file = new File(sourcePath);
		if(file.exists()) {
			boolean doZip = ZipUtil.doZip(sourcePath, zipPath+busDate+".zip");
			// 压缩成功后,清空原始文件
			if(doZip){
				FileTools.clearFilePath(sourcePath);
			}
			return doZip;
		} else {
			logger.info("【文件压缩】指定路径下文件不存在！！！");
		}
		return false;
	}
	
	/**
	 * 生成文件路徑
	 * @param date
	 * @param config
	 * @param ftpConfig
	 * @return
	 */
	public String[] genPath(Date date, PicturePropertyConfig config, VoFtpConfig ftpConfig) {
		String[] paths = new String[5];
		paths[0] = DateTimeUtil.getYear(date) + "";
		paths[1] = DateTimeUtil.formatDate(date, false);
		paths[2] = paths[0] + "/" + paths[1] + "/";
		paths[3] = config.getSvfilepath() + paths[2];
		paths[4] = config.getUpfilepath() + ftpConfig.getBankName() + ftpConfig.getFilepath() + "/" + paths[2];
		return paths;
	}
}
