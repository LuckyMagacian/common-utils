package com.lanxi.util.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.lanxi.util.utils.LoggerUtil;





public class ConfigManager {
	private static boolean hasInit=false;
	/**
	 * 私有的构造方法,拒绝实例化
	 */
	private ConfigManager(){}
	public static final Map<String, Properties> configs=new ConcurrentHashMap<String, Properties>();
	static{
		loadConfigs();
//		Properties log=null;
//		log=ConfigManager.getProperties("log4j");
//		if(log!=null){
//			Logger.getLogger(ConfigManager.class).info("配置了log4j,采用新配置!");
//			PropertyConfigurator.configure(log);
//		}else{
//			log=ConfigManager.getProperties("log");
//			if(log!=null){
//				Logger.getLogger(ConfigManager.class).info("配置了log4j,采用新配置!");
//				String filePath=log.getProperty("");
//				PropertyConfigurator.configure(log);	
//			}else
//				Logger.getLogger(ConfigManager.class).info("未配置log4j,采用默认配置!");
//		}
	}
	/**
	 * 加载classpath下properties文件夹中的所有properties文件
	 */
	public static void loadConfigs(){
		try{
			if(hasInit)
				return;
			LoggerUtil.init();
			LogFactory.info(ConfigManager.class,"开始加载配置文件----------------------------------------------------------");
			String path=LoggerUtil.class.getClassLoader().getResource("").toURI().getPath();
			System.out.println(path);
			LogFactory.info(ConfigManager.class,"classpath位置:["+path+"]");
			if (path.contains("target"))
				path = path.substring(0, path.indexOf("target")) + "/src/main/resources/properties";
			else if(path.contains("WEB-INF"))
				path = path.substring(0, path.indexOf("WEB-INF")) + "/WEB-INF/classes/properties";
			else
				path=path+"/properties";
			String configPath=path;
			System.out.println(configPath);
			LogFactory.info(ConfigManager.class,"配置文件目录:["+configPath+"]");
			File file=new File(configPath);
			if(!file.exists()){
				LogFactory.error(ConfigManager.class,"配置文件目录不存在!");
				return;
			}
			if(!file.isDirectory()){
				LogFactory.error(ConfigManager.class,"配置文件位置不是目录!");
				return;
			}
			File[] files=file.listFiles();
			if(files.length==0){
				LogFactory.error(ConfigManager.class,"配置文件目录中没有文件");
				return;
			}
			for(File each:files){
				String fileName=each.getName();
				LogFactory.info(ConfigManager.class,"文件名:["+fileName+"]");
				if(!fileName.endsWith(".properties")){
					LogFactory.warn(ConfigManager.class,"该文件不是配置问,跳过后续处理!");
					continue;
				}
				Properties temp=new Properties();
				temp.load(new InputStreamReader(new FileInputStream(each),"utf-8"));
				configs.put(fileName, temp);
				LogFactory.info(ConfigManager.class,"加载配置文件:["+fileName+"]成功!");;
			}
			LogFactory.info(ConfigManager.class,"加载配置文件完成----------------------------------------------------------");
			configs.get("log4j.properties").setProperty("log4j.appender.logfile.File", configs.get("log4j.properties").getProperty("log4j.appender.logfile.File").replace("$root", path));
		}catch (RuntimeException e) {
			
		} catch (URISyntaxException e) {
			LogFactory.error(ConfigManager.class,"加载配置文件时出现异常,配置文件位置不存在",e);
		} catch (FileNotFoundException e) {
			LogFactory.error(ConfigManager.class,"加载配置文件时,没有找到指定的配置文件!",e);
		} catch (IOException e) {
			LogFactory.error(ConfigManager.class,"加载配置文件时,输入输出流异常!",e);
		}
		hasInit=true;
	}
	/**
	 * 获取指定配置文件名称中的指定参数的值
	 * @param fileName 配置文件名称
	 * @param key 	   参数名称
	 * @return 		  参数值
	 * @throws FileNotFoundException 指定配置文件不存在
	 */
	public static String get(String fileName,String key){
		if(!fileName.endsWith(".properties")){
			LogFactory.debug(ConfigManager.class,"尝试获取的配置文件["+fileName+"]名称不是.properties结尾,添加后缀名后再次尝试获取");
			return get(fileName+".properties", key);
		}
		Properties properties=getProperties(fileName);
		if(properties==null){
			LogFactory.error(ConfigManager.class,"配置文件:["+fileName+"]不存在");
			throw new RuntimeException("配置文件:["+fileName+"]不存在!");
		}
		return properties.getProperty(key);
	}
	/**
	 * 获取指定配置文件
	 * @param fileName  配置文件名
	 * @return 配置文件对象
	 */
	public static Properties getProperties(String fileName){
		if(!fileName.endsWith(".properties")){
			LogFactory.debug(ConfigManager.class,"尝试获取的配置文件["+fileName+"]名称不是.properties结尾,添加后缀名后再次尝试获取");
			return getProperties(fileName+".properties");
		}
		Properties properties=configs.get(fileName);
		if(properties==null){
			LogFactory.error(ConfigManager.class,"配置文件:["+fileName+"]不存在");
			throw new RuntimeException("配置文件:["+fileName+"]不存在!");
		}
		return properties;
	}
	/**
	 * 返回第一个同名属性值
	 * @param key 属性名
	 * @return 找到的属性值,未找到时返回null
	 */
	public static String get(String key){
		for(Entry<String, Properties> each:configs.entrySet()){
			String fileName=each.getKey();
			Properties config=each.getValue();
			for(Entry<Object,Object> one:config.entrySet()){
				if(one.getKey().equals(key)){
					LogFactory.info(ConfigManager.class, "返回的是["+fileName+"]的["+key+"]值为["+one.getValue()+"]");
					return one.getValue().toString();
				}
				if(one.getKey().toString().equals(key)){
					LogFactory.info(ConfigManager.class, "返回的是["+fileName+"]的["+key+"]值为["+one.getValue()+"]");
					return one.getValue().toString();
				}
			}
		}
		LogFactory.info(ConfigManager.class, "未在配置文件中找到属性["+key+"]");
		return null;
	}
	
	public static List<String> getPropertiesFileNames(){
		return new ArrayList<String>(configs.keySet());
	}
}
