package com.lanxi.util.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.lanxi.util.utils.BeanUtil;
import com.lanxi.util.utils.CheckReplaceUtil;


public interface FromMap {
	/**
	 * 使用map中的键值来配置t对象
	 * @param map 键值对
	 */
	 default public void fromMap(Map<String, String> map){
		List<Method>setMethods=BeanUtil.getSetters(this);
		Class<?> clazz=null;
		Object arg=null;
		String value=null;
		Object value2=null;
		for(Method each:setMethods){
			try {
				Class<?>[] classes=each.getParameterTypes();
				if(classes.length==1)
					clazz=classes[0];
				else
					continue;
				arg=map.get(CheckReplaceUtil.firstCharLowcase(each.getName().substring(3)));
				if(arg==null)
					continue;
				else{
					if(map.getClass().getName().equals("org.apache.catalina.util.ParameterMap"))
						value=((String[])arg)[0].trim();
					else
						value=(String)arg;
					if(clazz.equals(Byte.class))
						value2=Byte.parseByte(value);
					else if(clazz.equals(Integer.class))
						value2=Integer.parseInt(value);
					else if(clazz.equals(Long.class))
						value2=Long.parseLong(value);
					else if(clazz.equals(Float.class))
						value2=Float.parseFloat(value);
					else if(clazz.equals(Double.class))
						value2=Double.parseDouble(value);
					else 
						value2=value;
					each.invoke(this, value2);
					}
				} catch (IllegalAccessException e) {
					throw new RuntimeException("["+this.getClass()+"]中["+each+"]方法为私有方法,权限不足!");
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("["+this.getClass()+"]中["+each+"]方法参数["+value+"]错误,当前参数类型["+value2.getClass()+"]!");
				} catch (InvocationTargetException e) {
					throw new RuntimeException("["+this.getClass()+"]中["+each+"]方法作用对象["+this+"]错误!");
				}
			}
		}
}
