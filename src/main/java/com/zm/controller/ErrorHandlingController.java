package com.zm.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.zm.utils.ResponseInfoMap;


@Controller
public class ErrorHandlingController {


	@RequestMapping (value="/error500",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public  String ErrorHandlingWith500(){

		Gson gson = new Gson();
		return gson.toJson(ResponseInfoMap.getErrorResponseMap( 500 , "参数异常或者服务器程序异常",  null ));
	}


	@RequestMapping (value="/error404",produces={"application/json;charset=UTF-8"})
	@ResponseBody
	public String ErrorHandlingWith404(){

		Gson gson = new Gson();
		return gson.toJson(ResponseInfoMap.getErrorResponseMap( 404 , "URI错误，请检查接口是否存在",  null ));
	}

}
