package com.lanxi.util.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;


/**
 * 文件操作工具类
 * Created by 1 on 2016/11/9.
 */
public class FileUtil {
	
	private static String testPath="";
	
	/**
	 * 获取指定目录下的文件或目录(可以指定文件后缀名)
	 * 
	 * @param dirPath
	 *            文件路径
	 * @param fileFormat
	 *            文件后缀名
	 * @return File[]   文件数组
	 */
	public static File[] getFiles(String dirPath, String fileFormat) {
		if (dirPath == null || dirPath.trim().equals(""))
			return null;
		File file = new File(dirPath);
		if (!file.isDirectory())
			return null;
		File[] files = null;
		if (file.isDirectory()) {
			files = file.listFiles();
			if (null == fileFormat)
				return files;
			else {
				if (files != null) {
					List<File> tempList = new ArrayList<File>();
					for (File each : files)
						if (each.getName().endsWith(fileFormat))
							tempList.add(each);
					return tempList.toArray(new File[tempList.size()]);
				}
			}
		}
		return files;
	}

	/**
	 * 获取指定目录下的文件或目录(可以指定文件后缀名)
	 * 
	 * @param dirPath
	 *            文件路径
	 * @param fileFormat
	 *            文件后缀名
	 * @return list 文件列表
	 */
	public static List<File> getFileList(String dirPath, String fileFormat) {
		File[] files = getFiles(dirPath, fileFormat);
		return Arrays.asList(files);
	}

	/**
	 * 获取classPath目录下的文件
	 * 
	 * @param path  	相对于classpath的文件路径
	 * @return File 	文件
	 */
	public static File getClassPathFile(String path) {
		try {
			String classPath = FileUtil.class.getClassLoader().getResource("").toURI().getPath();
			path = path.startsWith(File.separator) || path.startsWith("/") ? path : "/" + path;
			if(classPath.contains("test-classes")){
				path=testPath.substring(0, testPath.length()-1)+path;
			}
			File file = new File(classPath + path);
			if (!file.isDirectory())
				return file;
			else
				return null;
		} catch (Exception e) {
			throw new RuntimeException("获取classPath目录下指定文件异常", e);
		}
	}

	/**
	 * 获取classPath指定目录下的目录或文件(可以指定文件后缀名)
	 * 
	 * @param path 		路径
	 * @param format 	文件后缀名
	 * @return File[] 	文件数组
	 */
	public static File[] getClassPathFiles(String path, String format) {
		try {
			return getFiles(getClassPathFile(path).getAbsolutePath(), format);
		} catch (Exception e) {
			throw new RuntimeException("获取classPath目录下指定文件异常", e);
		}
	}

	/**
	 * 获取classPath指定目录下的目录或文件(可以指定文件后缀名)
	 * 
	 * @param path 		路径
	 * @param format 	文件后缀名
	 * @return List 	文件列表
	 */
	public static List<File> getClassPathFileList(String path, String format) {
		File[] files = getClassPathFiles(path, format);
		return Arrays.asList(files);
	}
	
	/**
	 * 相对与classpath获取文件 ..表示上一级目录
	 * 
	 * @param path 		文件路径
	 * @return File 	文件
	 */
	public static File getFileOppositeClassPath(String path) {
		try {
			String classPath = FileUtil.class.getClassLoader().getResource("").toURI().getPath();
			if(classPath.contains("test-classes")){
				classPath=classPath.replace("test-classes", "classes");
			}
			File file = new File(classPath);
			if (path == null || "".equals(path.trim()))
				return file;
			if (path.startsWith("/"))
				path = path.substring(1);

			String[] strs = path.split("/");
			for (String each : strs) {
				if ("..".equals(each))
					file = file.getParentFile();
				else if (file.isDirectory()) {
					file = new File(file.getAbsolutePath() + "/" + each);
				} else
					throw new RuntimeException("路径"+path+"是一个文件而非目录或路径不存在,无法进入下一级");
			}
			return file;
		} catch (Exception e) {
			throw new RuntimeException("相对classPath获取文件异常:请检查是否将配置文件复制到测试资源目录中!", e);
		}
	}
	/**
	 * 获取相对于classpath下的目录中的文件(可以指定后缀名)
	 * @param path 		相对与classpath的文件路径
	 * @param format 	文件后缀名
	 * @return File[] 	文件数组
	 */
	public static File[] getFilesOppositeClassPath(String path, String format) {
		try {
			return getFiles(getFileOppositeClassPath(path).getAbsolutePath(), format);
		} catch (Exception e) {
			throw new RuntimeException("获取相对于classpath目录下指定文件列表异常", e);
		}
	}
	/**
	 * 获取相对于classpath下的目录中的文件(可以指定后缀名)
	 * @param path 			相对classpath 文件路径
	 * @param format		文件后缀名
	 * @return List	文件列表
	 */
	public static List<File> getFileListOppositeClassPath(String path, String format){
		File[] files=getFilesOppositeClassPath(path, format);
		return Arrays.asList(files);
	}
	/**
	 * 以Byte数组形式读取文件并返回
	 * @param file 		文件
	 * @return 			byte[]数组形式的文件内容
	 */
	public static byte[] getFileContentByte(File file){
		try{
			FileInputStream fin=new FileInputStream(file);
			if(file.length()>Integer.MAX_VALUE){
		    	fin.close();
		    	throw new RuntimeException("文件太大无法读取");
		    }
			byte[] bytes=new byte[(int)file.length()];
			fin.read(bytes);
			fin.close();
			return bytes;
		}catch (Exception e) {
			new RuntimeException("获取文件内容以byte形式异常", e);
		}
		return null;
	}
	/**
	 * 以字符串形式读取文件并返回
	 * @param file 				文件
	 * @return 					String形式的文件内容
	 */
	public static String getFileContentString(File file){
		try{
			byte[]  bytes=getFileContentByte(file);
			return new String(bytes);
		}catch (Exception e) {
			new RuntimeException("获取文件内容以String形式异常", e);
		}
		return null;
	}
	/**
	 * java代码格式化
	 * @param str 				java字符串
	 * @return 					格式化后的java字符串
	 */
	public static String javaFormat(String str){
		try {
			String[] strs=str.split("\n");
			StringBuffer buffer=new StringBuffer();
			int tabCount=0;
			List<String> list=Arrays.asList(strs);
			for(String each:list){
				
				if(each.endsWith("}"))
					tabCount-=1;
				
				for(int i=tabCount;i>0;i--)
					buffer.append("\t");
				buffer.append(each+"\n");
				
				if(each.endsWith(")")&&!each.matches("[@*(/*)].*"))
					buffer.append("\t");
				
				if(each.endsWith("{"))
					tabCount+=1;
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new RuntimeException("java代码格式化异常",e);
		}
	}
	/**
	 * xml字符串格式化
	 * @param str 	xml字符串
	 * @return 		格式化后的xml字符串
	 */
	public static String xmlFormat(String str){
		try {
			String[] strs=str.split("\n");
			StringBuffer buffer=new StringBuffer();
			int tabCount=0;
			List<String> list=Arrays.asList(strs);
			for(String each:list){
				each=each.trim();
				if(each.matches("</ *[a-zA-Z]+ *>")||(each.matches(".*\\)")&&!each.matches("\\(.*"))||each.matches("from .+"))
					tabCount-=1;
				
					for(int i=tabCount;i>0;i--)
						buffer.append("\t");
				buffer.append(each+"\n");
				
				if(each.matches("< *[a-zA-Z]+ *>")||each.matches("< *[a-zA-Z]+ *.*\" *>")||(each.matches("\\(.*")&&!each.matches(".*\\)"))||each.matches("select"))
					tabCount+=1;
				
			}
			return buffer.toString();
		} catch (Exception e) {
			throw new RuntimeException("xml代码格式化异常",e);
		}
	}
	
	/**
	 * 将字符串写入到指定文件中
	 * @param str		字符串内容
	 * @param file 		文件
	 * @param charSet 	字符集
	 * @return 			写入内容后的文件
	 */
	public static File writeStrToFile(String str,File file,String charSet){
		try {
			if(file==null)
				throw new NullPointerException("file can't be null");
			if(file.isDirectory())
				throw new IllegalArgumentException("file can't be directory");
			if(!file.exists())
				file.createNewFile();
			charSet=charSet==null?"utf-8":charSet;
			FileOutputStream fos=new FileOutputStream(file);
			OutputStreamWriter writer=new OutputStreamWriter(fos, charSet);
			PrintWriter printer=new PrintWriter(writer);
			printer.print(str);
			printer.flush();
			printer.close();
			return file;
		} catch (Exception e) {
			throw new RuntimeException("写入字符串到文件异常1",e);
		}
	}
	/**
	 * 将字符串写入到指定文件(路径)中
	 * @param str 			字符串
	 * @param filePath 		文件路径及名称
	 * @param charSet 		字符集
	 */
	public static void writeStrToFile(String str,String filePath,String charSet){
		try {
			File file=new File(filePath);
			if(filePath==null)
				throw new NullPointerException("filePath can't be null");
			if(file.isDirectory())
				throw new IllegalArgumentException("This path is directory ! No file name !");
			if(!file.exists())
				file.createNewFile();
			writeStrToFile(str, file, charSet);
		}catch (Exception e) {
			throw new RuntimeException("写入字符串到文件异常2",e);
		}
	}
	/**
	 * 调用swing获取文件或目录
	 * @param fileType 		文件后缀名
	 * @param currentPath   当前目录
	 * @return 				文件
	 */
	public static File loadFile(File currentPath ,final String fileType){
		final String fileFormat=fileType==null||fileType.isEmpty()?"":fileType.startsWith(".")?fileType:"."+fileType;
		JFileChooser chooser=new JFileChooser();
		if(currentPath!=null)
			chooser.setCurrentDirectory(currentPath);
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
		if(fileType!=null&&!fileType.isEmpty())
			chooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return null;
				}
				
				@Override
				public boolean accept(File f) {
					if(f.getName().endsWith(fileFormat))
						return true;
					return false;
				}
			});
		chooser.showDialog(new JLabel(),"选择文件");
		return chooser.getSelectedFile();
	}
	/**
	 * 调用swing加载文件
	 * @param fileType 文件后缀名顾虑过滤
	 * @return 加载的文件
	 */ 
	public static File loadFile(String fileType){
		return loadFile(null, fileType);
	}

	
	
	
	/**
	 * 使用swing 加载目录
	 * @return  加载的目录
	 */
	public static File loadDir(){
		JFileChooser chooser=new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.showDialog(new JLabel(),"选择目录");
		chooser.setAcceptAllFileFilterUsed(true);
		return chooser.getSelectedFile();
	}
	/**
	 * 使用swing加载目录或文件
	 * @return 获取到的文件
	 */
	public static File loadDirOrFile(){
		JFileChooser chooser=new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.showDialog(new JLabel(),"选择文件");
		return chooser.getSelectedFile();
	}
	
	/**
	 * 读取文本文件中指定行内容
	 * @param file      文件
	 * @param index 	行号 从1开始 
	 * @param charset	文件字符集
	 * @return 			若行号大于文件行数返回null,行号小于1也会返回null
	 */
	public static String readFileByLineIndex(File file,int index,String charset){
		try {
			List<String> list=readFileByLineStartAndStop(file, index, index, charset);
			return list==null?null:list.get(0);
		} catch (Exception e) {
			throw new RuntimeException("读取指定行内容异常",e);
		}
	}
	/**
	 * 读取文件中指定行之间的字符串信息
	 * @param file 		被读取的文件
	 * @param start 	开始读取的行下标 1开始计数
	 * @param end 		最后读取的行下标
	 * @param charset 	读取时采用的字符集
	 * @return 			读取的字符串列表list
	 */
	public static List<String> readFileByLineStartAndStop(File file,int start,int end ,String charset){
		try {
			if(start>end){
				int temp=start;
				start=end;
				end=temp;
			}
			FileInputStream fin=new FileInputStream(file);
			InputStreamReader isr=new InputStreamReader(fin, charset==null||charset.isEmpty()?"utf-8":charset);
			BufferedReader reader=new BufferedReader(isr);
			String temp=null;
			List<String> list=new ArrayList<String>();
			int    count=0;
			while((temp=reader.readLine())!=null){
				if(temp.isEmpty())
					continue;
				count++;
				if(count>=start&&count<=end)
					list.add(temp);
			}
			reader.close();
			return list.isEmpty()?null:list;
		} catch (Exception e) {
			throw new RuntimeException("读取指定行内容异常",e);
		}
	}
	/**
	 * 传入一个文件若文件不存在则创建文件的路径中的文件夹以及该文件
	 * @param file 需要创建的文件
	 * @return 创建完成的文件
	 */
	public static File makeDirAndFile(File file){
		try {
			if(file.exists())
				return file;
			String path=file.getAbsolutePath();
			makeDirAndFile(path);
			return file.exists()?file:null;
		} catch (Exception e) {
			throw new RuntimeException("构建路径及文件异常1",e);
		}
	}
	/**
	 * 传入一个路径 构建路径上的目录 若包含.则将作为文件处理,将创建文件
	 * @param path 文件路径
	 */
	public static void makeDirAndFile(String path){
		try {
			if(path==null||path.isEmpty())
				throw new RuntimeException("path can't be null or empty");
			System.out.println(path);
			String[] paths=path.split("\\\\");
			System.out.println(Arrays.asList(paths));
			StringBuffer buffer=new StringBuffer();
			for(String each:paths){
//				if(buffer.toString().contains(".")&&each.contains("."))
//					throw new RuntimeException(buffer+" is file not dir so system  can't create new file ");
				if(buffer.length()==0)
					buffer.append(each);
				else
					buffer.append("\\"+each);
				File temp=new File(buffer.toString());
				if(!temp.exists()){
					if(each.contains("."))
						temp.createNewFile();
					else
						temp.mkdir();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("构建路径及文件异常2",e);
		}
	}
	/**
	 * 将多个文件中的内容合并到一个文件中并返回
	 * @param files 	多个文件
	 * @param newFile 	合并后的文件
	 * @return 
	 */
	public static File fileAppend(File[] files,File newFile,String fileNameMatche){
		try {
			makeDirAndFile(newFile);
			FileOutputStream fout=new FileOutputStream(newFile);
			OutputStreamWriter writer=new OutputStreamWriter(fout, "utf-8");
			PrintWriter printer=new PrintWriter(writer);
			
			for(File each:files){
				if(fileNameMatche!=null&&!fileNameMatche.isEmpty())
					if(!each.getName().matches(fileNameMatche))
						continue;
				FileInputStream fin=new FileInputStream(each);
				InputStreamReader reader= new InputStreamReader(fin,"utf-8");
				BufferedReader buff=new BufferedReader(reader);
				String temp=null;
				while((temp=buff.readLine())!=null)
					printer.println(temp);
				buff.close();
			}
			printer.close();
			return newFile;
		} catch (Exception e) {
			throw new RuntimeException("文件追加异常",e);
		} 
	}
	
	/**
	 * 加载指定路径的class文件
	 * @param path class文件绝对路径
	 * @return class对象
	 */
	public static Class<?> loadClass(String path){
		try {
			if(path.endsWith(".class"))
				return Class.forName(path.replace(".class", ""));
			else
				return Class.forName(path);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("加载类["+path+"]时,未找到该类!");
		}
	}
	/**
	 * 获取classPath路径
	 * @return
	 */
	public static String getClassPath(){
		try {
			ClassLoader loader=FileUtil.class.getClassLoader();
			if(loader.getClass().equals(java.net.URLClassLoader.class))
				loader=sun.misc.Launcher.getLauncher().getClassLoader();
			URL url=loader.getResource("");
			String path = url.toURI().getPath();
			if (path.contains("target"))
				path = path.substring(0, path.indexOf("target")) + "/src/main/resources/";
			else if(path.contains("WEB-INF"))
				path = path.substring(0, path.indexOf("WEB-INF")) + "/WEB-INF/classes/";
			return path;
		} catch (Exception e) {
			throw new RuntimeException("获取classpath路径时发生异常", e);
		}
	}
	

	/**
	 * 获取classpath下的文件
	 * @param fileType 文件后缀名
	 * @return classpath下获取到的文件
	 */
	public static File loadFileInClassPath(final String fileType){
		try {
			String path=SqlUtilForDB.class.getClassLoader().getResource("").toURI().toString();
			if(path.startsWith("file:/"))
				path=path.replace("file:/", "");
			path=path.substring(0,path.indexOf("target"))+"src/main/resources/";
			return loadFile(new File(path),fileType);
		} catch (URISyntaxException e) {
			throw new RuntimeException("加载classpath下文件异常",e);
		}
	}
	
}
