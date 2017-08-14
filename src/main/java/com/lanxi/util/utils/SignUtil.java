package com.lanxi.util.utils;

import java.lang.reflect.Field;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 签名与加密工具类
 * @author yangyuanjian
 *
 */
public class SignUtil {
	//--------------------------------baseStart-------------------------------------------
	/**
	 * md5摘要算法
	 * 
	 * @param src
	 *            明文字节码
	 * @return   md5摘要的字节
	 */
	public static byte[] md5En(byte[] src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bytes = md5.digest(src);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException("md5摘要算法异常", e);
		}
	}

	/**
	 * md5摘要算法 补0
	 * 
	 * @param src  要md5摘要的字符串
	 * @param  charset  字符串转字节的 字符集
	 * @return 	 md5摘要的结果
	 */
	public static String md5LowerCase(String src, String charset) {
		try {
			if (src == null || src.length() < 1)
				throw new RuntimeException("md5摘要时,明文信息为null或为空");
			byte b[] = md5En(src.getBytes(charset));
			int i;
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();// 32位的加密
		} catch (Exception e) {
			throw new RuntimeException("md5摘要算法异常", e);
		}
	}

	/**
	 * SHA-1摘要算法
	 * 
	 * @param src
	 *            明文字节
	 * @return    sha1加密后的字节数组
	 */
	public static byte[] sha1En(byte[] src) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			return sha1.digest(src);
		} catch (Exception e) {
			throw new RuntimeException("sha1摘要异常", e);
		}
	}

	/**
	 * SHA-1加密
	 * 
	 * @param src
	 *            明文
	 * @param charset
	 *            字符集
	 * @return    sha1加密后的字符串
	 */
	public static String sha1En(String src, String charset) {
		try {
			byte[] bytes = sha1En(src.getBytes(charset));
			return byteToHex(bytes);
		} catch (Exception e) {
			throw new RuntimeException("sha1摘要异常", e);
		}
	}

	/**
	 * base64加密
	 * 
	 * @param src
	 *            明文
	 * @param charset
	 *            字符集
	 * @return base64处理后的字符串
	 */
	public static String base64En(String src, String charset) {
		try {
			if (src == null || src.length() < 1)
				throw new RuntimeException("bas64加密时,明文信息为null或为空");
			byte[] bytes = base64En(src.getBytes(charset));
			return new String(bytes, charset);
		} catch (Exception e) {
			throw new RuntimeException("bas64加密异常", e);
		}
	}
	/**
	 * base64加密
	 * 
	 * @param src
	 *            明文字节
	 * @return    base处理后的字节数组
	 */
	public static byte[] base64En(byte[] src) {
		try {
			if (src == null || src.length < 1)
				throw new RuntimeException("bas64加密时,明文信息为null或为空");
			byte[] bytes = Base64.encodeBase64(src);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException("bas64加密异常", e);
		}
	}

	/**
	 * base64解密
	 * 
	 * @param src
	 *            明文
	 * @param charset
	 *            字符集 
	 * @return base64解码后的字符串
	 */
	public static String base64De(String src, String charset) {
		try {
			if (src == null || src.length() < 1)
				throw new RuntimeException("bas64解密时,明文信息为null或为空");
			byte[] bytes = base64De(src.getBytes(charset));
			return new String(bytes, charset);
		} catch (Exception e) {
			throw new RuntimeException("bas64解密异常", e);
		}
	}

	/**
	 * base64解密
	 * 
	 * @param src
	 *            明文字节
	 * @return   base64处理后的字节数组
	 */
	public static byte[] base64De(byte[] src) {
		try {
			if (src == null || src.length < 1)
				throw new RuntimeException("bas64解密时,明文信息为null或为空");
			byte[] bytes = Base64.decodeBase64(src);
			return bytes;
		} catch (Exception e) {
			throw new RuntimeException("bas64解密异常", e);
		}
	}

	/**
	 * byte数组转hex字符串
	 * 
	 * @param bytes  字节组数
	 * @return 		转换为hex字符串
	 */
	public static String byteToHex(byte[] bytes) {
		try {
			if (bytes == null || bytes.length < 1)
				throw new RuntimeException("byte转Hex时,byte数组为null或为空");
			return new String(Hex.encodeHex(bytes));
		} catch (Exception e) {
			throw new RuntimeException("byte转hex时异常", e);
		}
	}

	// ------------------------------------baseEnd--------------------------------------------

	// ------------------------------------signStart------------------------------------------
	/**
	 * map转String key=value key2=value2 不排序 无签名
	 * 
	 * @param map 属性-值 映射
	 * @return 	拼装后的 字符串
	 */
	public static String mapToStringNoSign(Map<String, String> map) {
		StringBuffer rs = new StringBuffer();
		for (Map.Entry<String, String> each : map.entrySet()) {
			if (each.getKey().equals("sign"))
				continue;
			rs.append(each.getKey());
			rs.append("=");
			rs.append(each.getValue());
			rs.append("&");
		}
		return rs.toString().substring(0, rs.length() - 1);
	}

	/**
	 * map转String key=value key2=value2 不排序 有签名
	 * 
	 * @param map  属性 - 值映射
	 * @return     拼装后的字符串
	 */
	public static String mapToString(Map<String, String> map) {
		StringBuffer rs = new StringBuffer();
		for (Map.Entry<String, String> each : map.entrySet()) {
			rs.append(each.getKey());
			rs.append("=");
			rs.append(each.getValue());
			rs.append("&");
		}
		return rs.toString().substring(0, rs.length() - 1);
	}

	/**
	 * map转String key=value key2=value2 key按字典排序 无签名
	 * 
	 * @param map 属性-值映射
	 * @return 排序拼装后的字符串
	 */
	public static String mapSortToStringNoSign(Map<String, String> map) {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		StringBuffer rs = new StringBuffer();
		for (String each : list) {
			if (each.equals("sign"))
				continue;
			rs.append(each);
			rs.append("=");
			rs.append(map.get(each));
			rs.append("&");
		}
		return rs.toString().substring(0, rs.length() - 1);
	}

	/**
	 * map转String key=value  key2=value2 key按字典排序 有签名 
	 * 
	 * @param map 传入的map
	 * @return 拼装后的字符串
	 */
	public static String mapSortToString(Map<String, String> map) {
		List<String> list = new ArrayList<String>(map.keySet());
		Collections.sort(list);
		StringBuffer rs = new StringBuffer();
		for (String each : list) {
			rs.append(each);
			rs.append("=");
			rs.append(map.get(each));
			rs.append("&");
		}
		return rs.toString().substring(0, rs.length() - 1);
	}

	/**
	 * map转String 仅仅返回key拼接成的字符串
	 * 
	 * @param map 属性-值映射
	 * @return 拼装后的字符串
	 */
	public static String mapToKeyString(Map<String, String> map) {
		List<String> list = new ArrayList<String>(map.keySet());
		StringBuffer rs = new StringBuffer();
		for (String each : list) {
			if (each.equals("sign"))
				continue;
			rs.append(each);
		}
		return rs.toString();
	}

	/**
	 * map转String 仅仅返回value拼接成的字符串
	 * 
	 * @param map  信息映射表
	 * @return 拼装后的信息字符串
	 */
	public static String mapToValueString(Map<String, String> map) {
		StringBuffer rs = new StringBuffer();
		for (Map.Entry<String, String> each : map.entrySet()) {
			if (each.getKey().equals("sign"))
				continue;
			rs.append(each.getValue()==null?"":each.getValue());
		}
		return rs.toString();
	}

	/**
	 * 从对象中获取字段以及其对应的属性,封装成map并返回
	 * 
	 * @param obj 获取前的obj
	 * @return 获取到的map信息
	 */
	public static Map<String, String> objToMap(Object obj) {
		Class<?> clazz = obj.getClass();
		Map<String, Field> fields = BeanUtil.getFieldsNoStatic(clazz);
		Map<String, String> map = new LinkedHashMap<String, String>();
		try {
			for (Map.Entry<String, Field> each : fields.entrySet()) {
				String name = each.getKey();
				Field field = each.getValue();
				field.setAccessible(true);
				map.put(name, field.get(obj) + "");
			}
			return map;
		} catch (Exception e) {
			throw new RuntimeException("从对象中获取map异常", e);
		}
	}
	// ------------------------------------RSA--------------------------------------------
	/**
	 * 生成 rsa密钥对以map形式返回 public:公钥 private:私钥
	 * 
	 * @return 包含密钥对的map
	 */
	public static Map<String, Object> getKeyPair() {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair keyPair = generator.generateKeyPair();
			RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
			RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) keyPair.getPrivate();
			map.put("public", publicKey);
			map.put("private", privateKey);
			return map;
		} catch (Exception e) {
			throw new RuntimeException("获取密钥对异常", e);
		}
	}

	/**
	 * 获取公钥字符串
	 * 
	 * @param map
	 *            包含密钥对的map
	 * @return 公钥字符串
	 */
	public static String getPublicKey(Map<String, Object> map) {
		try {
			Key pubKey = (Key) map.get("public");
			return Base64.encodeBase64String(pubKey.getEncoded());
		} catch (Exception e) {
			throw new RuntimeException("获取公钥异常", e);
		}
	}

	/**
	 * 获取公钥字符串
	 * 
	 * @param pubKey
	 *            公钥对象
	 * @return 公钥字符串
	 */
	public static String getPublicKey(PublicKey pubKey) {
		try {
			return Base64.encodeBase64String(pubKey.getEncoded());
		} catch (Exception e) {
			throw new RuntimeException("获取公钥异常", e);
		}
	}

	/**
	 * 获取私钥字符串
	 * 
	 * @param map
	 *            包含密钥对的map
	 * @return 私钥字符串
	 */
	public static String getPrivateKey(Map<String, Object> map) {
		try {
			Key priKey = (Key) map.get("private");
			return Base64.encodeBase64String(priKey.getEncoded());
		} catch (Exception e) {
			throw new RuntimeException("获取公钥异常", e);
		}
	}

	public static String getPrivateKey(PrivateKey puiKey) {
		try {
			return Base64.encodeBase64String(puiKey.getEncoded());
		} catch (Exception e) {
			throw new RuntimeException("获取公钥异常", e);
		}
	}

	/**
	 * 将公钥字符串转为公钥对象
	 * 
	 * @param key
	 *            私钥
	 * @return 私钥字符串
	 */
	public static PublicKey getPublicKey(String key) {
		try {
			byte[] keyBytes = (Base64.decodeBase64(key));
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将私钥字符串转为私钥对象
	 * 
	 * @param priKey
	 *            私钥字符串
	 * @return 私钥对象
	 */
	public static PrivateKey getPrivateKey(String priKey) {
		try {
			byte[] keyBytes = (Base64.decodeBase64(priKey));
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * rsa公钥加密
	 * 
	 * @param key
	 *            公钥对象
	 * @param data
	 *            待加密数据
	 * @return 加密后的数据
	 */
	public static byte[] rsaEn(PublicKey key, byte[] data) {
		try {
			if (data.length > key.getEncoded().length - 11)
				throw new RuntimeException("加密内容过长,不能大于245个字节");
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("rsa加密异常", e);
		}
	}

	/**
	 * rsa公钥加密
	 * 
	 * @param pubKey
	 *            公钥字符串
	 * @param data
	 *            待加密的数据
	 * @return 加密后的数据
	 */
	public static byte[] rsaEn(String pubKey, byte[] data) {
		PublicKey key = getPublicKey(pubKey);
		return rsaEn(key, data);
	}

	// /**
	// * rsa公钥加密
	// * 该方法容易出现异常-原因是输入的字符串内容转字节时可能会变成两个字节
	// * @param pubKey
	// * @param content
	// * @param charset
	// * @return
	// */
	// public static byte[] rsaEn(String pubKey, String conntent, String
	// charset) {
	// try {
	// PublicKey key = getPublicKey(pubKey);
	// byte[] bytes = conntent.getBytes(charset);
	// return rsaEn(key, bytes);
	// } catch (Exception e) {
	// throw new RuntimeException("字符串转字节时编码异常", e);
	// }
	// }

	/**
	 * rsa 私钥解密
	 * 
	 * @param key
	 *            私钥对象
	 * @param data
	 *            待解密数据
	 * @return 解密后的数据
	 */
	public static byte[] rsaDe(PrivateKey key, byte[] data) {
		try {
			if (data.length > key.getEncoded().length)
				throw new RuntimeException("解密内容过长");
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, key);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("rsa解密异常", e);
		}
	}

	/**
	 * rsa 私钥解密
	 * 
	 * @param priKey
	 *            私钥字符串
	 * @param data
	 *            待解密的数据
	 * @return 解密后的数据
	 */
	public static byte[] rsaDe(String priKey, byte[] data) {
		PrivateKey key = getPrivateKey(priKey);
		return rsaDe(key, data);
	}

	// /**
	// * rsa 私钥解密
	// * 该方法容易出现异常,原因是 字符串转字节时,对于部分字符可能会变成两个字节
	// * @param priKey
	// * @param content
	// * @param charset
	// * @return
	// */
	// public static byte[] rsaDe(String priKey, String content, String charset)
	// {
	// try {
	// PrivateKey key = getPrivateKey(priKey);
	// byte[] bytes = content.getBytes(charset);
	// return rsaDe(key, bytes);
	// } catch (Exception e) {
	// throw new RuntimeException("字符串转字节时编码异常", e);
	// }
	// }

	/**
	 * rsa md5 获取签名
	 * 
	 * @param priKey
	 *            私钥对象
	 * @param data
	 *            待签名的数据
	 * @return 签名
	 */
	public static String rsaMd5Sign(PrivateKey priKey, byte[] data) {
		try {
			Signature sign = Signature.getInstance("MD5WithRSA");
			sign.initSign(priKey);
			sign.update(data);
			return Base64.encodeBase64String(sign.sign());
		} catch (Exception e) {
			throw new RuntimeException("获取rsaMd5签名异常", e);
		}
	}

	/**
	 * rsa md5 获取签名
	 * 
	 * @param priKey
	 *            私钥字符串
	 * @param data
	 *            数据
	 * @return 签名
	 */
	public static String rsaMd5Sign(String priKey, byte[] data) {
		try {
			PrivateKey key = getPrivateKey(priKey);
			return rsaMd5Sign(key, data);
		} catch (Exception e) {
			throw new RuntimeException("字符串转字节是编码异常", e);
		}
	}
//	/**
//	 * rsa md5 获取签名
//	 * 
//	 * @param priKey
//	 * @param content
//	 * @param charset
//	 * @return
//	 */
//	public static String rsaMd5Sign(String priKey, String content, String charset) {
//		try {
//			PrivateKey key = getPrivateKey(priKey);
//			byte[] bytes = content.getBytes(charset);
//			return rsaMd5Sign(key, bytes);
//		} catch (Exception e) {
//			throw new RuntimeException("字符串转字节是编码异常", e);
//		}
//	}
	// ---------------------------------------des----------------------------------------------
	/**
	 * des加密
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] desEn(String key, byte[] data) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("des加密异常", e);
		}
	}

	/**
	 * des解密
	 * 
	 * @param key
	 *            密钥
	 * @param data
	 *            数据
	 * @return  加密后的byte数组 
	 */
	public static byte[] desDe(String key, byte[] data) {
		try {
			SecureRandom random = new SecureRandom();
			DESKeySpec desKey = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKey);
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new RuntimeException("des解密异常", e);
		}
	}
	
}
