package com.lanxi.util.entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

public class LogFactory {
	private static final Map<String, Logger> loggers=new ConcurrentHashMap<String, Logger>();
	/**
	 * 判定一个实例是否是Class对象
	 * @param obj 传入的实例
	 * @return 	true obj是Class对象时
	 * 			fals obj不是Class对象时
	 */
	private static boolean isClass(Object obj){
		if(obj instanceof Class<?>)
			return true;
		else
			return false;
	}
	/**
	 * 获取一个对象的Class实例
	 * @param obj 传入的对象
	 * @return obj本身 obj是Class实例时
	 * 		   obj的Class obj不是Class实例时
	 */
	private static Class<?> getClass(Object obj){
		if(isClass(obj))
			return (Class<?>) obj;
		else
			return obj.getClass();
	}
	/**
	 * 为一个对象创建 logger实例
	 * @param obj 需要创建logger的对象的实例或Class对象
	 */
	private static void createLogger(Object obj){
		Class<?> clazz=null;
		clazz=LogFactory.getClass(obj);
		Logger logger=Logger.getLogger(clazz);
		loggers.put(clazz.getName(), logger);
	}
	/**
	 * 获取一个对象的类的logger实例,若对应logger不存在则会创建并返回
	 * @param obj 需要获取logger的对象或实例
	 * @return 获取到的logger的实例
	 */
	public static Logger getLogger(Object obj){
		Class<?> clazz=null;
		if(obj instanceof Class<?>)
			clazz=(Class<?>)obj;
		else
			clazz=obj.getClass();
		Logger logger=loggers.get(clazz.getName());
		if(logger==null){
			createLogger(clazz);
			return getLogger(clazz);
		}
		return logger;
	}
	/**
	 * 记录debug级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 */
	public static void debug(Object obj,String message){
		getLogger(obj).debug(message);
	}
	/**
	 * 记录debug级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 * @param t 		异常源
	 */
	public static void debug(Object obj,String message,Throwable t){
		getLogger(obj).debug(message, t);
	}
	/**
	 * 记录info级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 */
	public static void info(Object obj,String message){
		getLogger(obj).info(message);
	}
	/**
	 * 记录info级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 * @param t 		异常源
	 */
	public static void info(Object obj,String message,Throwable t){
		getLogger(obj).info(message, t);
	}
	/**
	 * 记录warn级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 */
	public static void warn(Object obj,String message){
		getLogger(obj).warn(message);
	}
	/**
	 * 记录warn级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 * @param t 		异常源
	 */
	public static void warn(Object obj,String message,Throwable t){
		getLogger(obj).warn(message, t);
	}
	/**
	 * 记录error级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 */
	public static void error(Object obj,String message){
		getLogger(obj).error(message);
	}
	/**
	 * 记录error级别的日志
	 * @param obj 		记录日志的对象
	 * @param message	日志消息
	 * @param t 		异常源
	 */
	public static void error(Object obj,String message,Throwable t){
		getLogger(obj).error(message, t);
	}
}
