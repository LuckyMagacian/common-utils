package com.lanxi.util.utils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import com.lanxi.util.entity.MyOptinal;

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
	/**
	 * 将paddedStr 用padStr补齐到length长度<br>
	 * 若leftpad为true将在左边不齐否则在右边不齐
	 * @param paddedStr
	 * @param padStr
	 * @param length
	 * @param leftPad
	 * @return
	 */
	public static String pad(String paddedStr,String padStr,int length,boolean leftPad) {
		return leftPad?leftPad(paddedStr, padStr, length):rightPad(paddedStr, padStr, length);
	}
	
	/**
	 * 将paddedStr 用padStr在左边补齐到length长度
	 * @param paddedStr
	 * @param padStr
	 * @param length
	 * @return
	 */
	private static String leftPad(String paddedStr,String padStr,int length) {
		while(paddedStr.length()<length)
			paddedStr=padStr+paddedStr;
		return paddedStr;
	}
	/**
	 * 将paddedStr 用padStr在右边补齐到length长度
	 * @param paddedStr
	 * @param padStr
	 * @param length
	 * @return
	 */
	public static String rightPad(String paddedStr,String padStr,int length) {
		while(paddedStr.length()<length)
			paddedStr+=padStr;
		return paddedStr;
	}
	
	public static boolean isInt(String str) {
		try {
			if(str==null)
				return false;
			if(!str.matches("-?[0-9]+"))
				return false;
			if(str.contains("-")) {
				if(str.length()>(Integer.MAX_VALUE+"").length()+1)
					return false;
				return Long.parseLong(str)<Integer.MIN_VALUE?false:true;
			}else {
				if(str.length()>(Integer.MAX_VALUE+"").length())
					return false;
				return Long.parseLong(str)>Integer.MAX_VALUE?false:true;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isLong(String str) {
		try {
			if(str==null)
				return false;
			if(!str.matches("-?[0-9]+"))
				return false;
			if(new BigDecimal(str).compareTo(new BigDecimal(Long.MAX_VALUE))>0||
			   new BigDecimal(str).compareTo(new BigDecimal(Long.MIN_VALUE))<0)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isDouble(String str) {
		try {
			if(str==null)
				return false;
			if(!str.matches("-?[0-9]+(\\.[0-9]+)?")) {
				return false;
			}
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean isNumber(String str) {
		if(str==null)
			return false;
		if(!str.matches("[\\+-]?[0-9]+(\\.[0-9]+)?")) {
			return false;
		}
		return true;
	}
	
	public static MyOptinal<Number> toNumber(String str,Class<? extends Number> clazz) {
		if(str!=null&&str.matches("[0-9]+\\.?[0-9]*"))
			try {
				if(isNumber(str))
					return MyOptinal.of(clazz.getConstructor(String.class).newInstance(str));
				return MyOptinal.empty();
			} catch (InstantiationException e) {
				throw new RuntimeException("实例化异常",e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("非法存取",e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("参数非法",e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException("缺少目标",e);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException("构造方法不存在",e);
			} catch (SecurityException e) {
				throw new RuntimeException("安全异常",e);
			}
		else {
			return MyOptinal.empty();
		}
	}
	
	
	public static boolean possiblePhone(String phone) {
		if(phone==null)
			return false;
		if(!phone.matches("1[0-9]{10}"))
			return false;
		return true;
	}
}
