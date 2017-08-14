package com.lanxi.util.interfaces;

import com.alibaba.fastjson.JSONObject;

public interface ToJson {
	/**
	 * 将当前对象转为json字符串
	 * @return 当前对象的json字符串
	 */
	default public String toJson(){
		return JSONObject.toJSONString(this);
	}
}
