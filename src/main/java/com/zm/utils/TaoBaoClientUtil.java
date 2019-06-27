package com.zm.utils;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;

public class TaoBaoClientUtil {

	private static String url = "http://gw.api.taobao.com/router/rest?";
	private static String appkey = "24745141";
	private static String secret = "2c48adaaa0b70358396e3a946604c358";
	private static TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);;
	
	
	public static TaobaoClient  getClient( ){
		return client;
	}
	
	public static String getAppKey(){
		return appkey;
	}
}
