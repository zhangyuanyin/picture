package com.zyy.pic.common.data;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.rock.framework.facade.IFaceImageFacade;
import com.rock.framework.facade.IImageFileFacade;
import com.rock.framework.facade.vo.outupt.UserApiResult;
import com.zyy.pic.common.PicturePropertyConfig;
import com.zyy.pic.common.datasource.DatabaseContextHolder;
import com.zyy.pic.common.datasource.DatabaseType;
import com.zyy.pic.dao.PictureBankDao;
import com.zyy.pic.dao.PictureDao;
import com.zyy.pic.dao.TmAppMainDao;
import com.zyy.pic.domain.Picture;
import com.zyy.pic.domain.PictureBank;
import com.zyy.pic.domain.TmAppMain;
import com.zyy.pic.entity.response.ConsumContractResponse;
import com.zyy.pic.entity.vo.VoPictureBank;
import com.zyy.pic.service.HttpFileService;
import com.zyy.pic.service.TmAppMainService;
import com.zyy.pic.util.Constants;
import com.zyy.pic.util.PictureCommonUtil;
import com.zyy.pic.util.RedisUtil;

/**
 * <dt>图片数据处理公共类</dt>
 * @author yyzhang 
 * @since 2018年5月24日16:36:45
 */
@Component
public class EntryDataHelper {
	private static final Logger logger = Logger.getLogger(EntryDataHelper.class);
	private static final String SPLIT_FLAG = String.copyValueOf(new char[]{(char)0x02});
	
	@Autowired
	private PictureBankDao pictureBankDao;
	@Autowired
	private TmAppMainDao tmAppMainDao;
	@Autowired
	private PictureDao pictureDao;
	@Autowired
    private IImageFileFacade iImageFileFacade;
    @Autowired
    private IFaceImageFacade iFaceImageFacade;
    @Autowired
    private HttpFileService httpFileService;
    @Autowired
    private TmAppMainService tmAppMainService;
    @Autowired
    private PicturePropertyConfig config;
    @Autowired
    private PictureCommonUtil commonUtil;
    @Autowired
    private FileDataHandler handler;
    @Autowired
    private RedisUtil redis;
	
	/**
	 * 保存历史数据
	 */
	public void savePictureBankHis() {
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		pictureBankDao.save();
		pictureBankDao.deleteBank("1");
		logger.info("【预处理】历史数据迁移至HIS表完成！");
	}
	
	/**
	 * 保存当日数据
	 */
	public void savePictureBank(List<String> ftpFiles) {
		int count = 0;
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		for (String dataLine : ftpFiles) {
			if(count == 0) {
				logger.info("ftp配置信息："+dataLine);
				commonUtil.cache4Bank(dataLine, SPLIT_FLAG);
				count++;
				continue;
			}
			PictureBank pictureBank = new PictureBank();
			String[] datas = dataLine.split(SPLIT_FLAG);
			pictureBank.setContractNo(datas[0]);
			pictureBank.setAppNo(datas[1]);
			pictureBank.setDuebillNo(datas[2]);
			pictureBank.setSysNo(datas[3]);
			pictureBank.setBusinessDate(datas[4]);
			pictureBank.setStatus("0");
			pictureBank.setCreateTime(new Date());
			pictureBank.setUpdateTime(new Date());
			pictureBankDao.save(pictureBank);
		}
		logger.info("【读取FTP文件】数据入库完成！");
	}
	
	/**
	 * 获取数据
	 * @param status
	 * @return
	 */
	public List<PictureBank> getPictureBankByStatus(String status) {
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		return pictureBankDao.getPictureBankByStatus(status);
	}
	
	/**
	 * 更新数据
	 * @param status
	 * @return
	 */
	public void updatePictureBankStatus() {
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		pictureBankDao.updatePictureBank("0", "1", new Date());
		logger.info("当日待处理数据，处理完成并已更新！");
	}
	
	/**
	 * 从用户中心获取文件信息
	 * @param pictureBank
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public VoPictureBank getPictureFromCustomerCenter(PictureBank pictureBank) {
		VoPictureBank voBank = new VoPictureBank();
		DatabaseContextHolder.setDatabaseType(DatabaseType.fendb);
		TmAppMain tmAppMain = tmAppMainDao.getMfAppMainByAppNo(pictureBank.getAppNo());
		if (null == tmAppMain) {
			logger.info("进件信息不存在！app_no="+pictureBank.getAppNo());
			return null;
		}
		// 正面
		UserApiResult front = iImageFileFacade.getImageFileByCustomerId(tmAppMain.getUniqueId(), "F");
		if ("0000".equals(front.getCode())) {
			Map<String, String> img = (Map<String, String>) front.getData();
			voBank.setFrontIdCard(img.get("imagePath"));
		} else {
			voBank.setFrontIdCard("");
		}
		// 正面
		UserApiResult reverse = iImageFileFacade.getImageFileByCustomerId(tmAppMain.getUniqueId(), "R");
		if ("0000".equals(reverse.getCode())) {
			Map<String, String> img = (Map<String, String>) reverse.getData();
			voBank.setReverseIdCrad(img.get("imagePath"));
		} else {
			voBank.setReverseIdCrad("");
		}
		// 活体图片
		UserApiResult face = iFaceImageFacade.queryFaceImageByCustomerId(tmAppMain.getUniqueId());
		if ("0000".equals(face.getCode())) {
			Map<String, String> img = (Map<String, String>) face.getData();
			voBank.setFacePluse(img.get("imagePath"));
//			logger.info("【用户中心】返回地址：" + img.get("imagePath"));
		} else {
			voBank.setFacePluse("");
		}
		voBank.setContractNo(pictureBank.getContractNo() == null ? "":pictureBank.getContractNo());
		voBank.setProductCd(tmAppMain.getProductCd());
		voBank.setSysNo(pictureBank.getSysNo());
		voBank.setDuebillNo(pictureBank.getDuebillNo());
		
		return voBank;
	}

	/**
	 * 将文件写入服务器
	 * 影像类型    01身份证  02face++ 03协议
	 * @param dataMap
	 * @param date 
	 */
	public void saveRemoteData2File(Map<String, VoPictureBank> dataMap, Date date) {
		HttpResponse response = null;
		if(dataMap != null && dataMap.size() > 0) {
			for (Entry<String, VoPictureBank> key : dataMap.entrySet()) {
				VoPictureBank bank = key.getValue();
				if(bank == null) {
					logger.info("【文件下载】文件内容不符合下载条件！！！");
					return;
				}
				String[] uri = {bank.getFrontIdCard(), bank.getReverseIdCrad(), bank.getFacePluse(), bank.getContractNo()};
				for (int i = 0; i < uri.length; i++) {
					if(!org.apache.commons.lang3.StringUtils.isEmpty(uri[i])) {
						if(i < 3 || !Constants.SYS_NO_MF.equals(bank.getSysNo())) {
							if(i == 2) {
								String[] uris = uri[i].split(",");
								for (String url : uris) {
									if(!StringUtils.isEmpty(url)) {
										response = httpFileService.sendGet(url);
										if(response != null){
											handler.write(response, i, "02", bank.getDuebillNo(), date, config);
										}
									}
								}
							} else {
								response = httpFileService.sendGet(uri[i]);
								if(response != null) {
									handler.write(response, i, i == 3 ? "03":"01", bank.getDuebillNo(), date, config);
								}
							}
							logger.info("系统号："+bank.getSysNo()+", 下载URL："+uri[i]);
						} else {
							response = httpFileService.sendPost(config.getComsumeurl(), commonUtil.convert2Json(bank));
							if(response != null) {
								HttpEntity entity = response.getEntity();
								try {
									String result = EntityUtils.toString(entity);
									ConsumContractResponse contractResponse = JSONObject.parseObject(result, ConsumContractResponse.class);
									if(Constants.CONSUME_RESULT_SUCCESS.equals(contractResponse.getCode())) {
										response = httpFileService.sendGet(contractResponse.getData());
										if(response != null)
											handler.write(response, i, "03", bank.getDuebillNo(), date, config);
									}
									logger.info("合同号:"+uri[i]+", 产品号:"+bank.getProductCd()+", 用户中心返回结果："+result.replaceAll("\n", "").replaceAll("\\s", ""));
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}
		}
		logger.info("【文件下载】处理完成！已处理条数："+ (dataMap == null ? "0":dataMap.size()));
	}
	
	/**
	 * 压缩指定文件夹下文件
	 * @param busDate 
	 */
	public boolean compress(Date date, String busDate) {
		boolean compress = handler.compress(date, busDate, config);
		if(compress) {
			logger.info("【压缩文件】处理完成！");
		} else {
			logger.info("【压缩文件】处理失敗！");
		}
		return compress;
	}
	
	/**
	 * 上传至ftp,由于各家银行上传地址可能不一致，此处使用redis缓存内容，重新定义连接
	 * @param busDate 
	 * @return
	 */
	public boolean upload2Ftp(String bank, Date date, String busDate) {
		return handler.upload2Ftp(bank, date, busDate, config);
	}
	
	/**
	 * 檢查文件是否存在
	 * @param date
	 * @param busDate
	 * @return
	 */
	public boolean checkIsExists(String bank, Date date, String busDate) {
		return handler.isFTPFileExist(bank, date, busDate, config);
	}

	/**
	 * 从影像系统中获取影像信息
	 * @param pictureBank
	 * @return
	 */
	public VoPictureBank getPictureFromPicApp(PictureBank pictureBank) {
		VoPictureBank voBank = new VoPictureBank();
		StringBuffer buffer = new StringBuffer();
		DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
		List<Picture> pictures = pictureDao.getPicturesByAppNo(pictureBank.getAppNo());
		if(pictures != null && pictures.size() > 0) {
			for (Picture picture : pictures) {
				String uri = tmAppMainService.getPictureDefaultUrl(picture, picture.getAppNo()) + "";
				String type = picture.getSubClassSort();
				
				if(StringUtils.isEmpty(uri) || StringUtils.isEmpty(type = StringUtils.trimToEmpty(type)))
					continue;
				// 美借系统获取图片地址
				if(Constants.SYS_NO_MJ.equals(pictureBank.getSysNo())) {
					if(Constants.MJ_B1.equals(type)) {
						voBank.setFrontIdCard(uri);
					} else if(Constants.MJ_B2.equals(type)) {
						voBank.setReverseIdCrad(uri);
					} else if(Constants.MJ_D.equals(type)) {
						voBank.setContractNo(uri);
					} else if(type.matches("FACE1|FACE2|FACE3|FACE4")){
						buffer.append(uri).append(",");
					}
				}
			}
		}
		voBank.setFacePluse(buffer.toString());
		voBank.setSysNo(pictureBank.getSysNo());
		voBank.setDuebillNo(pictureBank.getDuebillNo());
		return voBank;
	}
}
