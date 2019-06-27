package com.zm.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseInfoMap {

	public static int SUCCESS_CODE =100;
	public static int FAILURE_CODE =-1;
	public static String SUCCESS_MSG = "success";
	
	public static Map getResponseMap( int code, String msg, Object data ){
		
		Map<String,Object> map = new HashMap<String, Object>();
		if( msg == null ){
			msg = "";
		}
		Map<String,Object> busstatus = new HashMap<String, Object>();
		busstatus.put("code",  code );
		busstatus.put("msg", msg );
		map.put("busstatus",  busstatus );
		
		Map<String,Object> content = new HashMap<String, Object>();
		if( data != null ){
			content.put("data",  data );		
		}
		map.put("content",  content );
		
		Map<String,Object> status = new HashMap<String, Object>();
		status.put("code", 10);
		map.put("status",  status );
		
		return map;
	}
	
	
	
public static Map getErrorResponseMap( int code, String msg, Object data ){
		
		Map<String,Object> map = new HashMap<String, Object>();
		if( msg == null ){
			msg = "";
		}
		Map<String,Object> busstatus = new HashMap<String, Object>();
		busstatus.put("code",  -1 );
		busstatus.put("msg", msg );
		map.put("busstatus",  busstatus );
		
		Map<String,Object> content = new HashMap<String, Object>();
		if( data != null ){
			content.put("data",  data );		
		}
		map.put("content",  content );
		
		Map<String,Object> status = new HashMap<String, Object>();
		status.put("code", code);
		status.put("msg", msg);
		map.put("status",  status );
		
		return map;
	}
}
