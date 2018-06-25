package com.zyy.pic.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rock.framework.facade.IFaceImageFacade;
import com.rock.framework.facade.IImageFileFacade;
import com.rock.framework.facade.vo.outupt.UserApiResult;
import com.zyy.pic.common.datasource.DatabaseContextHolder;
import com.zyy.pic.common.datasource.DatabaseType;
import com.zyy.pic.dao.TmAppMainDao;
import com.zyy.pic.domain.Picture;
import com.zyy.pic.domain.TmAppMain;
import com.zyy.pic.entity.vo.VoGomeNetLoan;
import com.zyy.pic.service.TmAppMainService;

/**
 * Created by Administrator on 2017/12/19.
 */
@Service
public class TmAppMainServiceImpl implements TmAppMainService {
    @Autowired
    TmAppMainDao tmAppMainDao;
    @Autowired
    IImageFileFacade iImageFileFacade;
    @Autowired
    IFaceImageFacade iFaceImageFacade;
    
    @Value("${picture.mj.url}")
    private String pictureMjUrl;
    @Value("${picture.other.url}")
    private String pictureOtherUrl;
    @Value("${picture.default.url}")
    private String pictureDefaultUrl;

    @Override
    public TmAppMain getAppMain(String name, String idNo) {
        TmAppMain tmAppMain = null;
        DatabaseContextHolder.setDatabaseType(DatabaseType.fendb);
        tmAppMain = tmAppMainDao.getAppMain(name,idNo);
        if(tmAppMain == null){
            DatabaseContextHolder.setDatabaseType(DatabaseType.jiedb);
            tmAppMain = tmAppMainDao.getAppMain(name,idNo);
        }
        return tmAppMain;
    }

    @Override
    public TmAppMain getAppMainByIdNo(String idNo) {
        TmAppMain tmAppMain = null;
        DatabaseContextHolder.setDatabaseType(DatabaseType.fendb);
        tmAppMain = tmAppMainDao.getAppMainByIdNo(idNo);
        if(tmAppMain == null){
            DatabaseContextHolder.setDatabaseType(DatabaseType.chedb);
            tmAppMain = tmAppMainDao.getAppMainByIdNo(idNo);
        }
        if(tmAppMain == null){
            DatabaseContextHolder.setDatabaseType(DatabaseType.fangdb);
            tmAppMain = tmAppMainDao.getAppMainByIdNo(idNo);
        }
        if(tmAppMain == null){
            DatabaseContextHolder.setDatabaseType(DatabaseType.jiedb);
            tmAppMain = tmAppMainDao.getAppMainByIdNo(idNo);
        }
        return tmAppMain;
    }
    
    @Override
    public TmAppMain getAppMain(String appNo) {
             DatabaseContextHolder.setDatabaseType(DatabaseType.jiedb);
             TmAppMain tmAppMain = tmAppMainDao.getAppMainByAppNo(appNo);
             DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
			 tmAppMainDao.getPictureByAppNo(appNo);
			 DatabaseContextHolder.setDatabaseType(DatabaseType.fendb);
			 tmAppMainDao.getMfAppMainByAppNo(appNo);
             return tmAppMain;
    }

	@Override
	public VoGomeNetLoan getAppMainByAppNo(String appNo) {
		    VoGomeNetLoan voGomeNetLoan =new VoGomeNetLoan();
		    voGomeNetLoan.setFrontIdCard("");
			voGomeNetLoan.setBackIdCrad("");
			voGomeNetLoan.setLivingPhoto("");
			DatabaseContextHolder.setDatabaseType(DatabaseType.jiedb);
			TmAppMain tmAppMain =tmAppMainDao.getAppMainByAppNo(appNo);
			if(null==tmAppMain){
				//美借没数据，获取美分
				return getVoGomeNetLoanMfAppNo(appNo, voGomeNetLoan);
			}
			if(!"GMBTC".equals(tmAppMain.getCustomerSource())){
				DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
				List<Picture> list=tmAppMainDao.getPictureByAppNo(appNo);
				if(null!=list && list.size()>0){
					StringBuffer url=new StringBuffer();
					for (Picture picture : list) {
				    	if("B1".equals(picture.getSubClassSort())){
				    		 voGomeNetLoan.setFrontIdCard(getPictureDefaultUrl(picture, appNo).toString());
				    	}else if("B2".equals(picture.getSubClassSort())){
				    		 voGomeNetLoan.setBackIdCrad(getPictureDefaultUrl(picture, appNo).toString());
				    	}else{
				    		url=url.append(getPictureDefaultUrl(picture, appNo)).append(",");
				    	}
					}
					String livingPhotoUrl=url.toString();
					if(livingPhotoUrl.length()>0){
						voGomeNetLoan.setLivingPhoto(livingPhotoUrl.substring(0, livingPhotoUrl.length()-1));
					}
				}
			}else {
				voGomeNetLoan=getWhitebarImg(tmAppMain.getUniqueId());
			}
		return voGomeNetLoan;
	}
    /**
     * 美分
     */
	private VoGomeNetLoan getVoGomeNetLoanMfAppNo(String appNo,VoGomeNetLoan voGomeNetLoan) {
			DatabaseContextHolder.setDatabaseType(DatabaseType.fendb);
			TmAppMain tmAppMain =tmAppMainDao.getMfAppMainByAppNo(appNo);
			if(null==tmAppMain){
				voGomeNetLoan.setCode("404");
				return voGomeNetLoan;
			}
			DatabaseContextHolder.setDatabaseType(DatabaseType.picdb);
			List<Picture> list=tmAppMainDao.getPictureByAppMFNo(appNo);
			if(null!=list && list.size()>0){
				for (Picture picture : list) {
			    	if("F".equals(picture.getSubClassSort())){
			    		 voGomeNetLoan.setFrontIdCard(getPictureDefaultUrl(picture, appNo).toString());
			    	}else if("R".equals(picture.getSubClassSort())){
			    		 voGomeNetLoan.setBackIdCrad(getPictureDefaultUrl(picture, appNo).toString());
			    	}
				}
				UserApiResult face=iFaceImageFacade.queryFaceImageByCustomerId (tmAppMain.getUniqueId());
	    		if("0000".equals(face.getCode())){
	    			Map<String, String> img= (Map<String, String>) face.getData();
	    			voGomeNetLoan.setLivingPhoto(img.get("imagePath"));
	    		}else{
	    			voGomeNetLoan.setLivingPhoto("");
	    		}
			}
			return voGomeNetLoan;
	}
	/**
	 * 获取图片地址
	 * @param picture
	 * @param appNo
	 * @return
	 */
	public StringBuffer getPictureDefaultUrl(Picture picture, String appNo) {
		StringBuffer url=new StringBuffer();
		if (null != picture.getkId() && !"".equals(picture.getkId())) {
			if("mj".equals(picture.getSysName())){
				url.append(pictureMjUrl).append("rmps").append("/download")	// 请求文件下载服务的系统名称
				.append("?").append("curSysNo=").append("0909")	// 请求文件下载服务的系统编码
				.append("&").append("key=").append(picture.getkId());
			} else {
				url.append(pictureOtherUrl).append("ffce").append("/download")// 请求文件下载服务的系统名称
				.append("?").append("curSysNo=").append("0601")		// 请求文件下载服务的系统编码
				.append("&").append("key=").append(picture.getkId());
			}
		}else{
			if("crm".equals(picture.getSysName())){
				picture.setSysName("aps");
			}
			url.append(pictureDefaultUrl).append(picture.getSysName()).append("/")	// 请求文件下载服务的系统名称
			   .append(appNo).append("/").append(picture.getSubClassSort()).append("/") // 请求文件下载服务的系统编码
			   .append(picture.getImgname());
		}
		return url;
	}
	
	/**
	 * 通过customerId获取图片地址
	 * @param customerId
	 * @return
	 */
	private VoGomeNetLoan getWhitebarImg(String customerId){
		VoGomeNetLoan voGomeNetLoan=new VoGomeNetLoan();
		//正面
		UserApiResult front=iImageFileFacade.getImageFileByCustomerId(customerId, "F");
		if("0000".equals(front.getCode())){
			Map<String, String> img= (Map<String, String>) front.getData();
			voGomeNetLoan.setFrontIdCard(img.get("imagePath"));
		}else{
			voGomeNetLoan.setFrontIdCard("");
		}
		//正面
		UserApiResult reverse=iImageFileFacade.getImageFileByCustomerId(customerId, "R");
		if("0000".equals(reverse.getCode())){
			Map<String, String> img= (Map<String, String>) reverse.getData();
			voGomeNetLoan.setBackIdCrad(img.get("imagePath"));
		}else{
			voGomeNetLoan.setBackIdCrad("");
		}
		//活体图片
		UserApiResult face=iFaceImageFacade.queryFaceImageByCustomerId (customerId);
		if("0000".equals(face.getCode())){
			Map<String, String> img= (Map<String, String>) face.getData();
			voGomeNetLoan.setLivingPhoto(img.get("imagePath"));
		}else{
			voGomeNetLoan.setLivingPhoto("");
		}
		return voGomeNetLoan;
	}
}
