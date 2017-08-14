package com.lanxi.util.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lanxi.util.utils.BeanUtil;


public interface ToMap {
	/**
	 * 将当前对象的属性封装为map
	 * @return 封装后的map
	 */
	default public Map<String, Object> toMap(){
		Map<String, Object> result=new HashMap<>();
		List<Method> gets=BeanUtil.getGetters(this);
		String attName=null;
		try {
			for(Method each:gets){
				String name=each.getName();
			    attName=name.substring(3);
				attName=(char)(attName.charAt(0)+32)+attName.substring(1);
				result.put(attName, each.invoke(this,new Object[]{}));
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("反射获取["+attName+"]时权限不足!",e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("添加["+attName+"]的值时获取目标错误!",e);
		}
		return result;
	}
}
