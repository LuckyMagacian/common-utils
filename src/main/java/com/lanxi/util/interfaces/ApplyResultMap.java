package com.lanxi.util.interfaces;

import java.util.Map;
import java.util.Map.Entry;

import com.lanxi.util.utils.BeanUtil;
/**
 * 在实现该接口的对象上应用结果集映射
 * 将arg的map.key的值映射为this的map.value的值
 * @author yangyuanjian
 *
 */
public interface ApplyResultMap {
	/**
	 * 使用结果集映射 arg.key->this.v
	 * @param map 映射集合
	 * @param arg 参数对象
	 */
	default public  void configByMap(Map<String, String> map,Object arg){
		for(Entry<String, String> each:map.entrySet()){
			String key=each.getKey();
			String value=each.getValue();
			BeanUtil.set(this, value, BeanUtil.get(arg, key));
		}
	}
}
