package com.lanxi.util.interfaces;

import java.util.Map;

public interface ConfigByMap extends FromMap{
	default public void configByMap(Map<String, String> map){
		fromMap(map);
	}
}
