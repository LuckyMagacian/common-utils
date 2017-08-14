package com.lanxi.util.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import com.lanxi.util.utils.BeanUtil;


public interface ConfigByBean { 
	/**
	 * 使用一个对象配置当前对象<br>
	 * 将两个对象中 名称及类型相同的属性设置为相同的值,通过method.invoke设置,因此需要相应的set/get方法
	 * @param arg 已经配置了值的对象
	 */
	default public void configByBean(Object arg) {
		Map<String, Method> setMethods = BeanUtil.getSetterMethods(this);
		Map<String, Method> getMethods = BeanUtil.getGetterMethods(arg);
		for (Entry<String, Method> each : setMethods.entrySet()) {
			String setMethodName = each.getKey();
			Method setMethod = each.getValue();
			Class<?>[] setType = setMethod.getParameterTypes();
			if (setType == null || setType.length > 1)
				continue;

			String getMethodName = "get" + setMethodName.substring(3);
			Method getMethod = getMethods.get(getMethodName);
			if (getMethod == null)
				continue;
			Class<?> getType = getMethod.getReturnType();
			if (getType == null)
				continue;
			if (!setType[0].equals(getType))
				continue;
			setMethod.setAccessible(true);
			getMethod.setAccessible(true);

			try {
				Object value=getMethod.invoke(arg);
				if(value==null)
					continue;
				setMethod.invoke(this,value);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(
						"使用[" + arg + "]的[" + getMethodName + "]返回值配置[" + this + "]的[" + setMethodName + "]时权限非法!", e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(
						"使用[" + arg + "]的[" + getMethodName + "]返回值配置[" + this + "]的[" + setMethodName + "]时参数非法!", e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(
						"使用[" + arg + "]的[" + getMethodName + "]返回值配置[" + this + "]的[" + setMethodName + "]时请求目标非法!", e);
			}

		}
	}
}
