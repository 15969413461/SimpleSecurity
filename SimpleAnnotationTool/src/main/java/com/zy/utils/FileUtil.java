package com.zy.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	private static List<File> filelist = new ArrayList<File>();
	/**
	 * 查找指定目录下的所有File对象的前置检查
	 * @param src - 查找目录File对象
	 * @return List<File> - 包含文件目录的容器
	 * @throws IOException 
	 */
	public static List<File> fileSearch(File src) {
		if(!src.exists()) {
			try {
				throw new FileNotFoundException("路径不存在: "+src.getAbsolutePath());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return foundFile(src);
	}
	/**
	 * 查找指定目录下的所有File对象
	 * @param src - 查找目录File对象
	 * @return List<File> - 每个List对象包含一个File对象
	 */
	private static List<File> foundFile(File src) {
		//获取 src路径目录下的所有File对象
		File[] array = new File(src.getAbsolutePath()).listFiles();
		if(array!=null && array.length>0) {
			for(int i=0;i<array.length;i++)	{
				if(array[i].isDirectory()) { 
					// 判断是文件还是文件夹
					foundFile(array[i]);
				}else { 
					//如果是文件，直接添加到集合
					filelist.add(array[i]);
				}
			}
		}
		return filelist;
	}
	
	/**
	 * 工具类关闭流
	 * 可变参数: ...  只能形参最后一个位置,处理方式与数组一致
	 * @param stream - 关闭流对象
	 * @throws IOException 
	 */
	public static void close(Closeable ... stream) 	{
		for(Closeable temp : stream)		{
			if(null!=temp)			{
				try {
					temp.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 工具类关闭流 
	 * @param strThrow - 异常信息 
	 * @param stream - 关闭的IO流对象
	 */
	public static void close(String strThrow ,Closeable ... stream) 
	{
		for(Closeable temp : stream) {
			if(null!=temp) {
				try {
					temp.close();
					throw new IOException(strThrow);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
	}
}
