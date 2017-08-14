package com.lanxi.util.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import com.lanxi.util.entity.LogFactory;
import com.lanxi.util.utils.RandomUtil.VerifyCodeType;

/**
 * 图片验证码工具类
 * @author yangyuanjian
 *
 */
public class PictureVerifyUtil {
	/** 随机对象 */
	private static Random random = new Random();
	/** 默认的图片宽度 */
	public static final int DEFAULT_WIDTH = 60;
	/** 默认的图片高度 */
	public static final int DEFAULT_HEIGHT = 30;
	/** 默认的验证码字符长度 */
	public static final int DEFAULT_CODE_LENGTH = 4;
	/** 默认的字符间隔距离 */
	public static final int DEFAULT_CODE_GAP = 14;

	/**
	 * 绘制干扰线
	 * 
	 * @param g
	 *            Graphics2D对象，用来绘制图像
	 * @param nums
	 *            干扰线的条数
	 * @param width
	 *            图片宽
	 * @param height
	 *            图片高
	 */
	public static void drawRandomLines(Graphics2D g, int nums, int width, int height) {
		g.setColor(RandomUtil.getRandomColor(160, 200));
		for (int i = 0; i < nums; i++) {
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(12);
			int y2 = random.nextInt(12);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * 绘制指定字符串
	 * 
	 * @param str
	 *            字符串
	 * @param g
	 *            画笔
	 */
	public static void drawString(String str, Graphics2D g) {
		for (int i = 0; i < str.length(); i++) {
			Color color = new Color(20 + random.nextInt(20), 20 + random.nextInt(20), 20 + random.nextInt(20));
			g.setColor(color);
			String temp = str.charAt(i) + "";
			AffineTransform trans = new AffineTransform();
			trans.rotate(random.nextInt(20) * 3.14 / 180, 5 * i + 8, 7);
			float scaleSize = random.nextFloat() + 0.8f;
			if (scaleSize > 1f)
				scaleSize = 1f;
			trans.scale(scaleSize, scaleSize);
			g.setTransform(trans);
			g.drawString(temp, DEFAULT_WIDTH/(str.length()+2)* (i-1) + DEFAULT_WIDTH/(str.length()), DEFAULT_HEIGHT/4*3);
		}
		g.dispose();
	}

	/**
	 * 绘制随机字符串并返回
	 * 
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param out
	 *            输出流
	 * @return 随机的字符串
	 */
	public static String makePic(Integer width, Integer height, OutputStream out) {
		String code = RandomUtil.getVerifyCode(DEFAULT_CODE_LENGTH, VerifyCodeType.NUM_LOWCHAR_UPCHAR);
		makePic(width, height, code, out);
		return code;
	}

	/**
	 * 绘制指定字符串
	 * 
	 * @param width
	 *            图片宽度
	 * @param height
	 *            图片高度
	 * @param code
	 *            指定的字符串
	 * @param out
	 *            输出流
	 * @return
	 */
	public static void makePic(Integer width, Integer height, String code, OutputStream out) {
		BufferedImage image = new BufferedImage(Integer.valueOf(width), Integer.valueOf(height),
				BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		Font myFont = new Font("黑体", Font.BOLD, 24);
		g.setFont(myFont);
		g.setColor(RandomUtil.getRandomColor(200, 250));
		g.fillRect(0, 0, Integer.valueOf(width), Integer.valueOf(height));
		g.setColor(RandomUtil.getRandomColor(180, 200));
		drawRandomLines(g, 160, Integer.valueOf(width), Integer.valueOf(height));
		drawString(code, g);
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", out);
			out.flush();
			out.close();
		} catch (IOException e) {
			LogFactory.error(PictureVerifyUtil.class, "生成图片验证码时发生异常", e);
		}
	}

	/**
	 * 以默认形式发送图片验证码到给定的servletResponse中
	 * @param res
	 * @return
	 */
	public static String sendVerifyCode(HttpServletResponse res) {
		String code = RandomUtil.getVerifyCode(DEFAULT_CODE_LENGTH, VerifyCodeType.NUM_LOWCHAR_UPCHAR);
		makePic(DEFAULT_WIDTH, DEFAULT_HEIGHT, code, res);
		return code;
	}
	
	/**
	 * 已给定的宽高绘制验证码字符串到给定的res中
	 * @param width
	 * @param height
	 * @param res
	 * @return
	 */
	public static String sendVerifyCode(int width, int height, HttpServletResponse res) {
		String code = RandomUtil.getVerifyCode(DEFAULT_CODE_LENGTH, VerifyCodeType.NUM_LOWCHAR_UPCHAR);
		makePic(DEFAULT_WIDTH, DEFAULT_HEIGHT, code, res);
		return code;
	}
	/**
	 * 绘制验证码字符串并直接写入到给定的输出流
	 * @param out
	 * @return
	 */
	public static String sendVerifyCode(OutputStream out) {
		String code = RandomUtil.getVerifyCode(DEFAULT_CODE_LENGTH, VerifyCodeType.NUM_LOWCHAR_UPCHAR);
		makePic(DEFAULT_WIDTH, DEFAULT_HEIGHT,code, out);
		return code;
	}

	/**
	 * 依据给定的宽高,绘制验证码并写入到给定的输出流
	 * @param width
	 * @param height
	 * @param out
	 * @return
	 */
	public static String sendVerifyCode(int width, int height, OutputStream out) {
		String code = RandomUtil.getVerifyCode(DEFAULT_CODE_LENGTH, VerifyCodeType.NUM_LOWCHAR_UPCHAR);
		makePic(width, height,code, out);
		return code;
	}
	/**
	 * 在HttpServletResponse输出流中输出绘制的指定验证码的验证码图片
	 * 
	 * @param code
	 *            验证码字符串
	 * @param res
	 *            HttpServletResponse对象
	 */
	
	public static void makePic(String code, HttpServletResponse res) {
		makePic(DEFAULT_WIDTH, DEFAULT_HEIGHT, code, res);
	}


	
	/**
	 * 在HttpServletResponse输出流中输出绘制的指定宽高和验证码的验证码图片
	 * 
	 * @param width
	 *            验证码图片宽
	 * @param height
	 *            验证码图片高
	 * @param code
	 *            验证码字符串
	 * @param res
	 *            HttpServletResponse对象
	 */
	public static void makePic(Integer width, Integer height, String code, HttpServletResponse res) {
		if (width == null) {
			LogFactory.warn( PictureVerifyUtil.class, "绘制验证码图片时未指定宽度,采用默认宽度!");
			width = DEFAULT_WIDTH;
		}
		if (height == null) {
			LogFactory.warn( PictureVerifyUtil.class, "绘制验证码图片时未指定高度,采用默认高度!");
			width = DEFAULT_WIDTH;
		}
		OutputStream out = null;
		try {
			out = res.getOutputStream();
			makePic(width, height, code, out);
		} catch (IOException e) {
			LogFactory.error(PictureVerifyUtil.class, "从httpServletResponse中获取输出流时发生异常", e);
		}
	}

	
	
}
