package com.lanxi.util.entity;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MyClassLoader extends ClassLoader {
	/**
	 * 重写类查找方法,改为从name去查找
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException{
		byte[] classByte=null;
		Class<?> clazz=null;
		try {
			Path path=Paths.get(name);
			classByte=Files.readAllBytes(path);
			clazz=defineClass(null,classByte, 0, classByte.length);
			System.out.println(clazz.getName());
			return clazz;
		} catch (Exception e) {
			throw new RuntimeException("加载指定路径中的class文件时发生异常", e);
		}
	}
	/**
	 * 从文件路径加载class文件
	 * @param path class文件所在路径
	 * @return 加载后的class文件
	 * @throws ClassNotFoundException 为找到该类
	 */
	public Class<?> loadPathClass(String path) throws ClassNotFoundException{
		if(!path.endsWith(".class"))
			throw new IllegalArgumentException("path ["+path+"] must be end with .class !");
		File file=new File(path);
		if(!file.exists())
			throw new IllegalArgumentException("no such file ["+path+"] !");
		if(file.isDirectory())
			throw new IllegalArgumentException("path ["+path+"] must be a file !");
		return findClass(path);
	}
	/**
	 * 从目录路径加载该目录下所有class文件
	 * @param path class文件所在目录
	 * @return class文件列表
	 * @throws ClassNotFoundException
	 */
	public List<Class<?>> loadPathClasses(String path) throws ClassNotFoundException{
		File file=new File(path);
		if(!file.exists())
			throw new IllegalArgumentException("no such directory ["+path+"] !");
		if(!file.isDirectory())
			throw new IllegalArgumentException("path ["+path+"] must be a directory !");
		List<Class<?>> classes=new ArrayList<>();
		File[] files=file.listFiles();
		for(File each:files){
			if(each.getName().endsWith(".class")){
				Class<?> clazz=loadPathClass(each.getAbsolutePath());
				classes.add(clazz);
			}else
				continue;
		}
		return classes;
	}
	/**
	 * 从文件目录加载目录及子目录下的所有class文件
	 * @param path 文件目录
	 * @return class列表
	 * @throws ClassNotFoundException
	 */
	public List<Class<?>> loadPathClassAndSubDirClass(String path) throws ClassNotFoundException{
		File file=new File(path);
		if(!file.exists())
			throw new IllegalArgumentException("no such directory ["+path+"] !");
		if(!file.isDirectory())
			throw new IllegalArgumentException("path ["+path+"] must be a directory !");
		List<Class<?>> classes=new ArrayList<>();
		File[] files=file.listFiles();
		for(File each:files){
			if(each.getName().endsWith(".class")){
				Class<?> clazz=loadPathClass(each.getAbsolutePath());
				classes.add(clazz);
			}else if(each.isDirectory()){
				classes.addAll(loadPathClassAndSubDirClass(each.getAbsolutePath()));
			}else continue;
		}
		return classes;
	}
}
