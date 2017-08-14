package com.lanxi.util.utils;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
/**
 * http访问工具类
 * @author yangyuanjian
 *
 */
public class HttpUtil {
	/** 默认编码字符集 */
	public static final String defEnCharset = "utf-8";
	/** 默认解码字符集 */
	public static final String defDeCharset = "utf-8";
	/** 默认超时时间 */
	public static final Integer defTimeout = 10000;

	/**
	 * 私有化构造方法 避免被实例化
	 */
	private HttpUtil() {

	};
	/**
	 * 对输入的url进行url编码处理
	 * @param urlStr	输入的url
	 * @return	url编码后的url
	 */
	public static String urlEncode(String urlStr){
		try{
			return URLEncoder.encode(urlStr,"utf-8");
		}catch (Exception e) {
			throw new RuntimeException("url编码异常",e);
		}
	}
	
	/**
	 * 发送请求
	 * 
	 * @param content
	 *            请求内容
	 * @param outStream
	 *            输出流
	 * @param charset
	 *            编码字符集 默认utf-8
	 */
	private static void post(String content, OutputStream outStream, String charset) {
		try {
			charset = charset == null ? defEnCharset : charset;
			OutputStreamWriter writer;
			writer = new OutputStreamWriter(outStream, charset);
			PrintWriter printer = new PrintWriter(writer);
			printer.println(content);
			printer.close();
		} catch (Exception e) {
			throw new RuntimeException("发送post请求异常", e);
		}
	}

	/**
	 * 接受请求
	 * 
	 * @param inStream
	 *            输入流
	 * @param charset
	 *            解码字符集 默认utf-8
	 * @return 请求内容
	 */
	private static String receive(InputStream inStream, String charset) {
		try {
			charset = charset == null ? defDeCharset : charset;
			InputStreamReader reader = new InputStreamReader(inStream, charset);
			BufferedReader buffReader = new BufferedReader(reader);
			String temp;
			StringBuffer reply = new StringBuffer();
			while ((temp = buffReader.readLine()) != null)
				reply.append(temp);
			buffReader.close();
			return reply.toString();
		} catch (Exception e) {
			throw new RuntimeException("接收数据异常", e);
		}
	}

	/**
	 * 通过给定的url发送内容,并返回接收方返回的内容,实现了https
	 *
	 * @param content
	 *            发送的内容
	 * @param Url
	 *            接收方地址
	 * @param charset
	 *            编码解码字符集 默认utf-8
	 * @param type
	 *            发送内容格式
	 * @param timeout
	 *            超时时间
	 * @return 接收方返回的内容
	 */
	public static String post(String content, String Url, String charset, String type, Integer timeout) {
		try {
			System.out.println("url:"+Url);
			charset = charset == null ? defEnCharset : charset;
			timeout = timeout == null ? defTimeout : timeout;
			URL url = new URL(Url);
			HttpsURLConnection conns=null;
			HttpURLConnection  conn 	= (HttpURLConnection) url.openConnection();
			if(Url.toLowerCase().startsWith("https"))
				conns=(HttpsURLConnection)conn;
			if(conns==null){
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setRequestMethod("POST");
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
				if (type != null)
					conn.setRequestProperty("Content-Type", type + ";Charset=" + charset);
				conn.connect();
				post(content, conn.getOutputStream(), charset);
				if (conn.getResponseCode() == 200)
					return receive(conn.getInputStream(), charset);
			}else{
				conns.setDoOutput(true);
				conns.setDoInput(true);
				conns.setRequestMethod("POST");
				conns.setConnectTimeout(timeout);
				conns.setReadTimeout(timeout);
				if (type != null)
					conns.setRequestProperty("Content-Type", type + ";Charset=" + charset);
				conns.connect();
				post(content, conns.getOutputStream(), charset);
				if (conns.getResponseCode() == 200)	
					return receive(conns.getInputStream(), charset);
			}
		} catch (Exception e) {
			throw new RuntimeException("发送post请求异常", e);
		}
		return null;
	}


	/**
	 * 通过给定的url发送内容,并返回接收方返回的内容
	 * 
	 * @param content
	 *            发送的内容
	 * @param Url
	 *            接收方地址
	 * @param charset
	 *            编码解码字符集 默认utf-8
	 * @param type
	 *            发送内容格式
	 * @return 接收方返回的内容
	 */
	public static String post(String content, String Url, String charset, String type) {
		return post(content, Url, charset, type, defTimeout);
	}

	/**
	 * 通过给定的url发送内容,并返回接收方返回的内容
	 * 
	 * @param content
	 *            发送的内容
	 * @param res
	 *            Servlet的响应
	 * @param charset
	 *            编码解码字符集 默认utf-8
	 * @param type
	 *            发送内容格式
	 * @return 接收方返回的内容
	 */
	public static String post(String content, HttpServletResponse res, String charset, String type) {
		try {
			charset = charset == null ? defEnCharset : charset;
			res.setCharacterEncoding(charset);
			res.setContentType(type + ";charset=" + charset);
			post(content, res.getOutputStream(), charset);
			if (res.getStatus() == 200)
				return "0";
		} catch (Exception e) {
			throw new RuntimeException("发送xml文档异常", e);
		}
		return "1";
	}

	/**
	 * 发送字符串信息
	 * 
	 * @param str
	 *            字符串内容
	 * @param url
	 *            接收方地址
	 * @param charset
	 *            编码字符集 默认 utf-8
	 * @param timeout
	 *            超时时间
	 * @return 接收方返回的内容
	 */
	public static String postStr(String str, String url, String charset, Integer timeout) {
		return post(str, url, charset, null, timeout);
	}

	/**
	 * 发送字符串信息
	 *
	 * @param str
	 *            字符串内容
	 * @param url
	 *            接收方地址
	 * @param charset
	 *            编码字符集 默认 utf-8
	 * @return 接收方返回的内容
	 */
	public static String postStr(String str, String url, String charset) {
		return postStr(str, url, charset, defTimeout);
	}

	/**
	 * 发送字符串信息
	 * 
	 * @param str
	 *            字符串内容
	 * @param res
	 *            servelet的响应
	 * @param charset
	 *            编码字符集 默认 utf-8
	 * @return 接收方返回的内容
	 */
	public static String postStr(String str, HttpServletResponse res, String charset) {
		return post(str, res, charset, "txt/plain");
	}

	/**
	 * 发送xml文档数据
	 * 
	 * @param xml
	 *            xml字符串
	 * @param url
	 *            接收方的地址
	 * @param charset
	 *            编码字符集 默认utf-8
	 * @param timeout
	 *            超时时间
	 * @return 发送xml的响应
	 */
	public static String postXml(String xml, String url, String charset, Integer timeout) {
		return post(xml, url, charset, "txt/html", timeout);
	}

	/**
	 * 发送xml文档数据
	 *
	 * @param xml
	 *            xml字符串
	 * @param url
	 *            接收方的地址
	 * @param charset
	 *            编码字符集 默认utf-8
	 * @return 发送xml的响应
	 */
	public static String postXml(String xml, String url, String charset) {
		return postXml(xml, url, charset, null);
	}

	/**
	 * 发送xml文档数据
	 * 
	 * @param xml
	 *            xml字符串
	 * @param res
	 *            servelet的响应
	 * @param charset
	 *            编码字符集 默认utf-8
	 * @return 0 发送成功 1 发送失败
	 */
	public static String postXml(String xml, HttpServletResponse res, String charset) {
		return post(xml, res, charset, "txt/xml");
	}

	/**
	 * 发送键值对
	 * 
	 * @param params
	 *            键值对
	 * @param url
	 *            目标url
	 * @param charset
	 *            编解码字符集
	 * @return 发送键值对的响应
	 */
	public static String postKeyValue(Map<String, String> params, String url, String charset) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpPost post = new HttpPost(url);
			List<NameValuePair> keyValue = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> each : params.entrySet())
				keyValue.add(new BasicNameValuePair(each.getKey(), each.getValue()));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(keyValue);
			entity.setContentEncoding(charset);
			post.setEntity(entity);
			HttpResponse res = client.execute(post);

			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(res.getEntity().getContent(), charset));
			StringBuffer strBuff = new StringBuffer();
			String temp = null;
			while ((temp = buffReader.readLine()) != null)
				strBuff.append(temp);
			return strBuff.toString();

		} catch (Exception e) {
			throw new RuntimeException("发送键值对异常", e);
		}
	}

	/**
	 * 发送get 请求
	 * 
	 * @param url
	 *            地址+直接参数跟随
	 * @param charset
	 *            字符集
	 * @return get到的字符串
	 */
	public static String get(String url, String charset) {
		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpGet get = new HttpGet();
			get.setURI(new URI(url));
			HttpResponse res = client.execute(get);
			BufferedReader buffReader = new BufferedReader(
					new InputStreamReader(res.getEntity().getContent(), charset));
			StringBuffer strBuff = new StringBuffer();
			String temp = null;
			while ((temp = buffReader.readLine()) != null)
				strBuff.append(temp);
			return strBuff.toString();
		} catch (Exception e) {
			throw new RuntimeException("发送get请求异常", e);
		}
	}


	/**
	 * 发送get请求
	 * 
	 * @param url
	 *            目标url
	 * @param param
	 *            参数map
	 * @param charset
	 *            字符集
	 * @return http get方法的返回字符串
	 */
	public static String get(String url, Map<String, Object> param, String charset) {
		StringBuffer params = new StringBuffer("?");
		for (Map.Entry<String, Object> each : param.entrySet())
			params.append(each.getKey() + "=" + each.getValue() + "&");
		return get(url + params.substring(0, params.length()), charset);
	}
	
	/**
	 * 获取servlet请求中的ip地址
	 * @param req servlet请求
	 * @return 字符串ip地址
	 */
	public static String getRealIp(HttpServletRequest req){
		String ip=null;
		ip=req.getHeader("x-forwarded-for");
		if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
			ip=req.getHeader("Proxy-Client-IP");
		}
		if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
			ip=req.getHeader("WL-Proxy-Client-IP");
		}
		if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
			ip=req.getRemoteAddr();
		}
		if(ip==null||ip.length()==0||"unknown".equalsIgnoreCase(ip)){
			ip="NONE";
		}
		return ip;
	}
	/**
	 * 将map中的值以key-value的形式发送到给定的url
	 * @param map
	 * @param uri
	 * @return
	 */
	public static String postMap(Map<Object,Object> map,String uri){
		try {
			StringBuilder builder=new StringBuilder();
			for(Map.Entry<Object,Object> each:map.entrySet()){
				builder.append(""+each.getKey() + "=" + each.getValue()+"&");
			}
			return postStr(builder.substring(0,builder.length()-1),uri,"utf-8");
		}catch (Exception e){
			throw new RuntimeException("post map 时发生异常",e);
		}
	}
	/**
	 * 为servlet-request设置字符集
	 * @param req
	 * @param charset
	 */
	public static void setEncode(HttpServletRequest req,String charset){
		try {
			if(charset==null)
				req.setCharacterEncoding("utf-8");
			else
				req.setCharacterEncoding(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取servletRequst中用户的访问路径
	 * @param req
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest req) {
		String path=req.getRequestURI();
		path=path.substring(12); 
		return path;
	}
}
