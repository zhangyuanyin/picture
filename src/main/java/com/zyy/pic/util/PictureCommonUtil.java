package com.zyy.pic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zyy.pic.common.PicturePropertyConfig;
import com.zyy.pic.entity.vo.VoFtpConfig;
import com.zyy.pic.entity.vo.VoPictureBank;

@Component
public class PictureCommonUtil {
	private static final Logger logger = Logger.getLogger(PictureCommonUtil.class);
	private static final int COMPOSE_NUM = 4;
	private static final String CUSTOM_MAC = "mac";
	private static final String CUSTOM_APPID = "appId";
	private static final String CUSTOM_CONTRACTID = "contractId";
	private static final String CONTRACT_SIGN = "ESIGN_";
	private static final String BANK_NAME_TJ = "tianjin";
	
	@Autowired
	private PicturePropertyConfig config;
	@Autowired
	private RedisUtil redis;
	
	public String convert2Json(VoPictureBank voBank) {
		String[] seeds;
		String contractNo = "";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String productCd = StringUtils.trimToEmpty(voBank.getProductCd());
		if(Constants.SYS_NO_MF.equals(voBank.getSysNo())) {
			if(productCd.matches("6660|6670|6671|6680|6681|6605")) {
				seeds = new String[]{config.getMfappid(), config.getMfsecret()};
				paramMap.put(CUSTOM_APPID, config.getMfappid());
			} else {
				seeds = new String[]{config.getMfgappid(), config.getMfgsecret()};
				paramMap.put(CUSTOM_APPID, config.getMfgappid());
			}
			paramMap.put(CUSTOM_MAC, DigestUtils.md5Hex(StringUtils.join(seeds, "")));
		}

		if(productCd.matches("6660|6670|6671|6680|6681|6605")) {
			ProductCode[] codes = ProductCode.values();
			for (ProductCode code : codes) {
				if(productCd.equals(code.getCode())) {
					contractNo = CONTRACT_SIGN + code.getType() + "_C_";
					break;
				}
			}
		}
		paramMap.put(CUSTOM_CONTRACTID, contractNo + voBank.getContractNo());
		return JSON.toJSONString(paramMap);
	}
	
	public static void main(String[] args) {
		String[] seeds = new String[]{"1003", "18aa40c5-86e5-11e7-8344-005056946ec1"};
		System.out.println(DigestUtils.md5Hex(StringUtils.join(seeds, "")));
	}
	
	/**
	 * 將值緩存至redis
	 * @param cfg
	 * @param syntax
	 * @return
	 */
	public void cache4Bank(String cfg, String syntax) {
		if(StringUtils.isEmpty(cfg)) {
			return;
		}
		VoFtpConfig ftpConfig = new VoFtpConfig();
		String[] configs = cfg.split(syntax);
		int index = configs[4].indexOf("/");
		ftpConfig.setIp(configs[0]);
		ftpConfig.setPort(configs[1]);
		ftpConfig.setUsername(configs[2]);
		ftpConfig.setPassword(configs[3]);
		if(index < 0) {
			ftpConfig.setBankName(configs[4]);
			ftpConfig.setFilepath("");
		} else {
			ftpConfig.setBankName(configs[4].substring(0, index));
			ftpConfig.setFilepath(configs[4].substring(index));
		}
		boolean result = redis.set("bank_" + BANK_NAME_TJ, ftpConfig, 36000L);
		if(result) {
			logger.info("緩存成功！！！key=bank_" + BANK_NAME_TJ);
		}
	}
	
	/**
	 * 根据配置线程数,分解集合
	 * @param o
	 * @return
	 */
	public static List<List<?>> composeCollection(Object o) {
		List<List<?>> composes = new ArrayList<List<?>>();
		int onset = 0, offset = 0;
		if(o instanceof ArrayList) {
			List<?> list = (ArrayList<?>) o;
			if(list != null && list.size() > 0) {
				int interval = list.size() / COMPOSE_NUM;
				for(int i = 0; i < COMPOSE_NUM; i++) {
					offset = onset += interval;
					if(i == COMPOSE_NUM - 1) {
						offset = list.size();
					}
					composes.add(i, list.subList(onset - interval, offset));
				}
			}
		}
		return composes;
	}
	
	public boolean lock(String key) {
		logger.info("redis加锁开始...");
		long expire = System.currentTimeMillis() + 10000;
		boolean lock = redis.setNX(key, String.valueOf(expire));
		if(lock) {
			logger.info("redis加锁成功！");
		} else {
			logger.info("redis加锁失败！");
		}
		return lock;
	}
}
