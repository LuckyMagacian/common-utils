package com.lanxi.util.utils;

import java.io.File;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import com.lanxi.util.entity.LogFactory;

/**
 * 日志工具类
 * @author yangyuanjian
 *
 */
public class LoggerUtil {
	/**日志文件已存在时是否清空原有日志*/
	private static boolean clear = false;
	/**日志的记录等级*/
	private static LogLevel logLevel=LogLevel.INFO;
	/**
	 * 日志配置初始化
	 */
	public static void init() {
		try {
			ClassLoader loader=LoggerUtil.class.getClassLoader();
			if(loader.getClass().equals(java.net.URLClassLoader.class))
				loader=sun.misc.Launcher.getLauncher().getClassLoader();
			URL url=loader.getResource("");
			String path = url.toURI().getPath();
			if (path.contains("target"))
				path = path.substring(0, path.indexOf("target")) + "/src/main/resources/log/daily.log";
			else if(path.contains("WEB-INF"))
				path = path.substring(0, path.indexOf("WEB-INF")) + "/WEB-INF/classes/log/daily.log";
			else
				path=path+"/log/daily.log";
			String tempPath = path.substring(0, path.indexOf("/daily.log"));
			File logDir = new File(tempPath);
			if (!logDir.exists())
				logDir.mkdirs();
			path=path.substring(0,path.indexOf("/daily.log"));
			Properties properties = new Properties();
			//配置日志等级 以及 日志对象
			properties.setProperty("log4j.rootLogger", logLevel+",debug,info,warn,error,console");
//			properties.setProperty("log4j.appender.INFO", "org.apache.log4j.ConsoleAppender");
//			properties.setProperty("log4j.appender.INFO.layout", "org.apache.log4j.PatternLayout");
//			properties.setProperty("log4j.appender.INFO.layout.ConversionPattern", "%d %p [%c] - <%m>%n");
			//配置每日生成日志文件 形式的日志对象
			properties.setProperty("log4j.appender.debug", "com.lanxi.util.entity.LogAppender");
			//配置日志文件路径
			properties.setProperty("log4j.appender.debug.File", path+"/debug/debug.log");
			FileUtil.makeDirAndFile(new File(path+"/debug/debug.log"));
			properties.setProperty("log4j.appender.debug.Threshold", "DEBUG");
			//配置日志文件编码方式
			properties.setProperty("log4j.appender.debug.Encoding", "UTF-8");
			//配置日志文件为追加形式
			properties.setProperty("log4j.appender.debug.File.Append", "true");
			//配置自动生成日志文件的后缀名
			properties.setProperty("log4j.appender.debug.DatePattern", "yyyy-MM-dd'.debug.log'");
			//配置日志文件的记录形式
			properties.setProperty("log4j.appender.debug.layout", "org.apache.log4j.PatternLayout");
			//配置日志文件表达式
			properties.setProperty("log4j.appender.debug.layout.ConversionPattern", "%d %p [%c] - <%m>%n");
			
			
			//配置每日生成日志文件 形式的日志对象
			properties.setProperty("log4j.appender.info", "com.lanxi.util.entity.LogAppender");
			//配置日志文件路径
			properties.setProperty("log4j.appender.info.File", path+"/info/info.log");
			FileUtil.makeDirAndFile(new File(path+"/info/info.log"));
			properties.setProperty("log4j.appender.info.Threshold", "INFO");
			//配置日志文件编码方式
			properties.setProperty("log4j.appender.info.Encoding", "UTF-8");
			//配置日志文件为追加形式
			properties.setProperty("log4j.appender.info.File.Append", "true");
			//配置自动生成日志文件的后缀名
			properties.setProperty("log4j.appender.info.DatePattern", "yyyy-MM-dd'.info.log'");
			//配置日志文件的记录形式
			properties.setProperty("log4j.appender.info.layout", "org.apache.log4j.PatternLayout");
			//配置日志文件表达式
			properties.setProperty("log4j.appender.info.layout.ConversionPattern", "%d %p [%c] - <%m>%n");
			
			
			//配置每日生成日志文件 形式的日志对象
			properties.setProperty("log4j.appender.warn", "com.lanxi.util.entity.LogAppender");
			//配置日志文件路径
			properties.setProperty("log4j.appender.warn.File", path+"/warn/warn.log");
			FileUtil.makeDirAndFile(new File(path+"/warn/warn.log"));
			properties.setProperty("log4j.appender.warn.Threshold", "WARN");
			//配置日志文件编码方式
			properties.setProperty("log4j.appender.warn.Encoding", "UTF-8");
			//配置日志文件为追加形式
			properties.setProperty("log4j.appender.warn.File.Append", "true");
			//配置自动生成日志文件的后缀名
			properties.setProperty("log4j.appender.warn.DatePattern", "yyyy-MM-dd'.warn.log'");
			//配置日志文件的记录形式
			properties.setProperty("log4j.appender.warn.layout", "org.apache.log4j.PatternLayout");
			//配置日志文件表达式
			properties.setProperty("log4j.appender.warn.layout.ConversionPattern", "%d %p [%c] - <%m>%n");
			
			
			
			//配置每日生成日志文件 形式的日志对象
			properties.setProperty("log4j.appender.error", "com.lanxi.util.entity.LogAppender");
			//配置日志文件路径
			properties.setProperty("log4j.appender.error.File", path+"/error/error.log");
			FileUtil.makeDirAndFile(new File(path+"/error/error.log"));
			properties.setProperty("log4j.appender.error.Threshold", "ERROR");
			//配置日志文件编码方式
			properties.setProperty("log4j.appender.error.Encoding", "UTF-8");
			//配置日志文件为追加形式
			properties.setProperty("log4j.appender.error.File.Append", "true");
			//配置自动生成日志文件的后缀名
			properties.setProperty("log4j.appender.error.DatePattern", "yyyy-MM-dd'.error.log'");
			//配置日志文件的记录形式
			properties.setProperty("log4j.appender.error.layout", "org.apache.log4j.PatternLayout");
			//配置日志文件表达式
			properties.setProperty("log4j.appender.error.layout.ConversionPattern", "%d %p [%c] - <%m>%n");
			
			
			
			//配置控制台输出的日志对象
			properties.setProperty("log4j.appender.console", "org.apache.log4j.ConsoleAppender");
			properties.setProperty("log4j.appender.console.layout", "org.apache.log4j.PatternLayout");
			properties.setProperty("log4j.appender.console.layout.ConversionPattern", "%d %p [%c] - <%m>%n");
			PropertyConfigurator.configure(properties);
			System.out.println("log4j初始化完成!");
			LogFactory.info(LoggerUtil.class,"log4j初始化完成!");
		} catch (Exception e) {
			throw new RuntimeException("加载log4j配置异常", e);
		}
	}
	
	public static boolean isClear() {
		return clear;
	}
	
	/**
	 * 设置清空日志
	 * @param    clear  清空日志的标记  
	 * 已失效
	 */
	@Deprecated
	public static void setClear(boolean clear) {
		LoggerUtil.clear = clear;
	}

	public static LogLevel getLogLevel() {
		return logLevel;
	}

	public static void setLogLevel(LogLevel logLevel) {
		LoggerUtil.logLevel = logLevel;
	}

	public static enum LogLevel{
		DEBUG,INFO,WARN,ERROR;
	}
	
}
