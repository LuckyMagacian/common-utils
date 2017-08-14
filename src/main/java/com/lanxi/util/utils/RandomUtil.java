package com.lanxi.util.utils;

import java.awt.Color;
import java.util.Random;
import java.util.UUID;

/**
 * 随机数字|字符串 生成工具类
 * 
 * @author yangyuanjian
 *
 */
public class RandomUtil {
	private static Random random = new Random();

	/**
	 * 生成随机字母(小写)
	 * 
	 * @return 随机字符
	 */
	public static char getRandomChar() {
		return (char) (random.nextInt(26) + 'a');
	}

	/**
	 * 生成随机数字
	 * 
	 * @return 随机一个数字
	 */
	public static char getRandomNum() {
		return (char) (random.nextInt(10) + '0');
	}

	/**
	 * 生成指定数量的字母字符串(小写)
	 * 
	 * @param count
	 *            字符串的字符个数
	 * @return 随机英文字符串
	 */
	public static String getRandomChar(int count) {
		StringBuilder rs = new StringBuilder();
		while (count-- > 0)
			rs.append(getRandomChar());
		return rs.toString();
	}

	/**
	 * 生成指定数量的随数字 字符串
	 * 
	 * @param count
	 *            字符数
	 * @return 随机数字符串
	 */
	public static String getRandomNumber(int count) {
		StringBuilder rs = new StringBuilder();
		while (count-- > 0)
			rs.append(getRandomNum());
		return rs.toString();
	}

	/**
	 * 获取uuid
	 * 
	 * @return uuid字符串
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}
	/**
	 * 生成验证码字符串
	 * @param count 字符数量
	 * @param type  验证码类型  @see{@link VerifyCodeType}
	 * @return
	 */
	public static String getVerifyCode(int count, VerifyCodeType type) {
		String upchar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String lowchar = "abcdefghijklmnopqrstuvwxyz";
		String number = "0123456789";
		String repository = null;
		StringBuffer code=new StringBuffer();
		
		switch (type) {
		case NUM:
			repository = number;
			break;
		case LOWCHAR:
			repository = lowchar;
			break;
		case UPCHAR:
			repository = upchar;
			break;
		case NUM_LOWCHAR:
			repository = number + lowchar;
			break;
		case NUM_UPCHAR:
			repository = number + upchar;
			break;
		case LOWCHAR_UPCHAR:
			repository = lowchar + upchar;
			break;
		case NUM_LOWCHAR_UPCHAR:
			repository = number + lowchar + upchar;
			break;
		default:
			repository = number + lowchar + upchar;
			break;
		}
		for(int i=0;i<count;i++)
			code.append(repository.charAt(random.nextInt(repository.length()-1)));
		return code.toString();
	}
	/**
	 * 验证码类型枚举类
	 * NUM 					取值为0-9之间随机的验证码<p>
	 * LOWCHAR 				取值为a-z之间的验证码<p>
	 * UPCHAR 				取值为A-Z之间的验证码<p>
	 * NUM_LOWCHAR 			取值为0-9a-z之间的验证码<p>
	 * NUM_UPCHAR 			取值为0-9A-Z之间的验证码<p>
	 * LOWCHAR_UPCHAR 		取值为a-zA-Z之间的验证码<p>
	 * NUM_LOWCHAR_UPCHAR 	取值为a-zA-Z0-9之间的验证码<p>
	 * @author yangyuanjian
	 */
	public static enum VerifyCodeType{
		NUM, LOWCHAR, UPCHAR, NUM_LOWCHAR, NUM_UPCHAR, LOWCHAR_UPCHAR, NUM_LOWCHAR_UPCHAR;
	}
	
	/**
	 * 生成随机颜色
	 * 
	 * @param fc
	 *            前景色
	 * @param bc
	 *            背景色
	 * @return Color对象，此Color对象是RGB形式的。@see{@link Color}
	 */
	public static Color getRandomColor(int fc, int bc) {
		if (fc > 255)
			fc = 200;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
	/**
	 * 生成精度从0.00000001到1的double
	 * @return
	 */
	public static double getRandomDouble() {
		return random.nextInt(100000000)/100000000D;
	}
}
