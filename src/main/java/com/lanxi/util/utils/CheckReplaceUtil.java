package com.lanxi.util.utils;
/**
 * 校验与替换工具类
 * @author yangyuanjian
 *
 */
public class CheckReplaceUtil {
	/**
	 * 传入一个字符串,若该字符串为null则返回空,否则返回去除空字符的结果
	 * @param string 传入的字符串
	 * @return 转换后的字符串
	 */
	public static String nullAsSpace(String string){
		return string==null?"":string.trim();
	}
	/**
	 * 传入一个字符串,若该字符串为空则返回null,否则返回去除空字符的结果
	 * @param string  传入的字符串
	 * @return 转换后的字符串
	 */
	public static String spaceAsNull(String string){
		return string==null?null:string.trim().equals("")?null:string.trim();
	}
	/**
	 * 传入一个字符串,若该字符串为null则返回null
	 * 			若该字符串第一个字符为小写英文字母,则将其改为大写并返回
	 * 			否则返回原字符串
	 * @param string  传入的字符串
	 * @return 转换后的字符串
	 */
	public static String firstCharUpcase(String string){
		if(string==null)
			return null;
		char first=string.charAt(0);
		if(first>='a'&&first<='z'){
			return string.replaceFirst(""+first,""+(char)(first-32));
		}
		return string;
	}
	/**
	 * 传入一个字符串,若该字符串为null则返回null
	 * 			若该字符串第一个字符为大写英文字母,则将其改为小写写并返回
	 * 			否则返回原字符串
	 * @param string  传入的字符串
	 * @return 转换后的字符串
	 */
	public static String firstCharLowcase(String string){
		if(string==null)
			return null;
		char first=string.charAt(0);
		if(first>='A'&&first<='Z'){
			return string.replaceFirst(""+first,""+(char)(first+32));
		}
		return string;
	}
	/**
	 * 传入一个字符串,若该字符串为null ,直接返回null
	 * 			若该字符串包含大写英文字母则将其改为下划线+对应小写的形式(_?)
	 * @param string  传入的字符串
	 * @return 	转换后的字符串
	 */
	public static String upcaseToUnderlineLowcaser(String string){
		if(string==null)
			return null;
		StringBuffer temp=new StringBuffer();
		for(char each:string.toCharArray()){
			if(each>='A'&&each<='Z'){
				temp.append("_").append((char)(each+32));
			}
			else{
				temp.append(each);
			}
		}
		return temp.toString();
	}
	
	/**
	 * [下划线小写]转大写
	 * @param string 传入的字符串
	 * @return 转换后的字符串
	 */
	public static String underlineLowcaserToUpcase(String string){
		if(string==null)
			return null;
		StringBuffer temp=new StringBuffer();
		String[] strs=string.split("_");
		boolean flag=true;
		temp.append(strs[0]);
		for(String each:strs){
			if(flag){
				flag=false;
				continue;
			}
			temp.append(firstCharUpcase(each));
		}
		return temp.toString();
	}
}
