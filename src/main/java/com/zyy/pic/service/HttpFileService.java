package com.zyy.pic.service;

import org.apache.http.HttpResponse;

/**
 * <dt>HTTP 服务处理接口</dt>
 * @author yyzhang
 * @since 2018年2月1日16:59:09
 */
public interface HttpFileService {
	
	HttpResponse sendGet(String uri);
	
	HttpResponse sendPost(String uri, String json);
}
