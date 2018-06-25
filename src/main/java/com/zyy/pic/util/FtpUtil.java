package com.zyy.pic.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.ObjectPool;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.Assert;
import org.thymeleaf.util.ArrayUtils;

import com.zyy.pic.entity.vo.VoFtpConfig;

/**
 * Ftp工具类
 */
public class FtpUtil {
	private static final Logger logger = Logger.getLogger(FtpUtil.class);

    /**
     * ftpClient连接池初始化标志
     */
    private static volatile boolean hasInit = false;
    /**
     * ftpClient连接池
     */
    private static ObjectPool<FTPClient> ftpClientPool;

    /**
     * 初始化ftpClientPool
     *
     * @param ftpClientPool
     */
    public static void init(ObjectPool<FTPClient> ftpClientPool) {
        if (!hasInit) {
            synchronized (FtpUtil.class) {
                if (!hasInit) {
                    FtpUtil.ftpClientPool = ftpClientPool;
                    hasInit = true;
                }
            }
        }
    }

    /**
     * 按行读取FTP文件
     *
     * @param remoteFilePath 文件路径（path+fileName）
     * @return
     * @throws IOException
     */
    public static List<String> readFileByLine(String remoteFilePath) throws IOException {
        FTPClient ftpClient = getFtpClient();
        InputStream in = null;
        BufferedReader br = null;
        try {
        	List<String> dataList = new ArrayList<String>();
        	in = ftpClient.retrieveFileStream(encodingPath(remoteFilePath));
        	br = new BufferedReader(new InputStreamReader(in));
        	
        	String line = "";
        	while ((line = br.readLine()) != null) {
        		dataList.add(line);
        	}
            return dataList;
        } finally {
        	if(in != null) {
        		in.close();
        	}
        	if(br != null) {
        		br.close();
        	}
        	ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 获取指定路径下FTP文件
     *
     * @param remotePath 路径
     * @return FTPFile数组
     * @throws IOException
     */
    public static FTPFile[] retrieveFTPFiles(String remotePath) throws IOException {
        FTPClient ftpClient = getFtpClient();
        try {
        	FTPFile[] listFiles = ftpClient.listFiles(encodingPath(remotePath + "/"));
            return listFiles;
        } finally {
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 获取指定路径下FTP文件名称
     *
     * @param remotePath 路径
     * @return ftp文件名称列表
     * @throws IOException
     */
    public static List<String> retrieveFileNames(String remotePath) throws IOException {
    	List<String> nameList = new ArrayList<String>();
        FTPFile[] ftpFiles = retrieveFTPFiles(remotePath);
        if (ArrayUtils.isEmpty(ftpFiles)) {
            return Collections.emptyList();
        }
        for (FTPFile ftpFile : ftpFiles) {
			nameList.add(ftpFile.getName());
		}
        return nameList;
    }

    /**
     * 编码文件路径
     */
    private static String encodingPath(String path) throws UnsupportedEncodingException {
        // FTP协议里面，规定文件名编码为iso-8859-1，所以目录名或文件名需要转码
        return new String(path.replaceAll("//", "/").getBytes("GBK"), "iso-8859-1");
    }

    /**
     * 获取ftpClient
     *
     * @return
     */
    private static FTPClient getFtpClient() {
        checkFtpClientPoolAvailable();
        FTPClient ftpClient = null;
        Exception ex = null;
        // 获取连接最多尝试3次
        for (int i = 0; i < 3; i++) {
            try {
                ftpClient = ftpClientPool.borrowObject();
                ftpClient.changeWorkingDirectory("/");
                break;
            } catch (Exception e) {
                ex = e;
            }
        }
        if (ftpClient == null) {
            throw new RuntimeException("Could not get a ftpClient from the pool", ex);
        }
        return ftpClient;
    }

    /**
     * 释放ftpClient
     */
    private static void releaseFtpClient(FTPClient ftpClient) {
        if (ftpClient == null) {
            return;
        }

        try {
            ftpClientPool.returnObject(ftpClient);
        } catch (Exception e) {
            logger.error("Could not return the ftpClient to the pool", e);
            // destoryFtpClient
            if (ftpClient.isAvailable()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException io) {
                }
            }
        }
    }

    /**
     * 检查ftpClientPool是否可用
     */
    private static void checkFtpClientPoolAvailable() {
        Assert.state(hasInit, "FTP未启用或连接失败！");
    }

    /**
     * 上传Excel文件到FTP
     * @param workbook
     * @param remoteFilePath
     * @throws IOException
     */
    public static boolean uploadExcel2Ftp(Workbook workbook, String remoteFilePath)
        throws IOException {
        Assert.notNull(workbook, "workbook cannot be null.");
        Assert.hasText(remoteFilePath, "remoteFilePath cannot be null or blank.");
        FTPClient ftpClient = getFtpClient();
        OutputStream out = null;
        try {
        	out = ftpClient.storeFileStream(encodingPath(remoteFilePath));
            workbook.write(out);
            return true;
        } finally {
        	if(out != null) {
        		out.close();
        	}
            ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }
    
    /**
     * 上传文件至FTP
     * @param remoteFilePath
     * @param in
     * @return
     * @throws IOException
     */
    public static boolean uploadFile(String remoteFilePath, InputStream in) throws IOException{
    	boolean success = false;
    	FTPClient ftpClient = getFtpClient();
    	try {
    		success = ftpClient.storeFile(remoteFilePath, in);
    	} finally {
    		ftpClient.completePendingCommand();
    		releaseFtpClient(ftpClient);
    	}
    	return success;
    }

    /**
     * 从ftp下载excel文件
     * @param remoteFilePath
     * @param response
     * @throws IOException
     */
    public static void downloadExcel(String remoteFilePath, HttpServletResponse response)
        throws IOException {
        String fileName = remoteFilePath.substring(remoteFilePath.lastIndexOf("/") + 1);
        fileName = new String(fileName.getBytes("GBK"), "iso-8859-1");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        FTPClient ftpClient = getFtpClient();
        try (InputStream in = ftpClient.retrieveFileStream(encodingPath(remoteFilePath));
            OutputStream out = response.getOutputStream()) {
            int size = 0;
            byte[] buf = new byte[10240];
            while ((size = in.read(buf)) > 0) {
                out.write(buf, 0, size);
                out.flush();
            }
        } finally {
            ftpClient.completePendingCommand();
            releaseFtpClient(ftpClient);
        }
    }

    /**
     * 使用銀行配置獲取FTP連接
     * @param config
     * @return
     * @throws IOException 
     * @throws SocketException 
     * @throws NumberFormatException 
     */
    public static FTPClient getConnectionByBanks(VoFtpConfig config) throws Exception {
    	FTPClient ftp = new FTPClient();
    	int reply;
		// 连接FTP服务器
		ftp.connect(config.getIp(), Integer.parseInt(config.getPort()));
		// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
		ftp.login(config.getUsername(), config.getPassword());
		reply = ftp.getReplyCode();
		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
		}
    	return ftp;
    }
}