package com.zyy.pic.service.impl;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zyy.pic.service.HttpFileService;

@Service
public class HttpFileServiceImpl implements HttpFileService {
	private static final Logger logger = Logger.getLogger(HttpFileServiceImpl.class);

	@Override
	public HttpResponse sendGet(String uri) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = httpClient.execute(get);
			// 获取并验证执行结果
			int code = response.getStatusLine().getStatusCode();
			if(code == HttpStatus.SC_OK){
				return response;
			}else {
				logger.info("请求用户中心失败, url="+uri);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	public HttpResponse sendPost(String uri, String json) {
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();  
			HttpPost httpPost = new HttpPost(uri);  
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");  
			
			StringEntity se = new StringEntity(json, "UTF-8");  
			se.setContentType("text/json");  
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));  
			httpPost.setEntity(se);  
			HttpResponse response = httpClient.execute(httpPost);  
			int code = response.getStatusLine().getStatusCode();
			if(code != HttpStatus.SC_OK) {
				logger.info("请求用户中心异常："+json);
			} else {
				return response;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return null;
	}
}
