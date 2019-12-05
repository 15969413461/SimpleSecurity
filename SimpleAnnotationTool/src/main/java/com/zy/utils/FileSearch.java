package com.zy.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
* @author zy
* @Date 2019年11月9日  星期六 下午  4:54:33
* @Description 进行文件扫描
* @version 
*/
public class FileSearch {
//	private static Logger logger = LoggerFactory.getLogger(FileSearch.class);
	/**
	 * 进行包扫描,返回其下所有有效的类文件名
	 * @param packageDir
	 * @return
	 */
	public static List<String> search() {
		return search("");
	}
	public static List<String> search(String packageDir) {
		List<String> className = new ArrayList<>();
		String path = FileSearch.class.getResource("/").getPath();
		String realy = null;
		if( packageDir.isEmpty() ){
			realy = path;
		}else {
			packageDir = checkPackageDir(packageDir);
			realy = path + packageDir;
		}
		// 截取包名以外路径的开始索引
		int length = path.length() - 1;
		
		// 搜索
		List<File> foundFile = FileUtil.fileSearch(new File(realy));
		
		String[] arr = null;
		String str = null;
		for (File file : foundFile) {
			str = file.getAbsolutePath().substring(length);
			// 截取文件名,从第一个.开始截取
			arr = str.split("\\.");
			if(arr[1].equals("class")) {
				str = arr[0].replace("\\", ".");
				className.add(str);
			} else {
//				logger.error("此文件名无法进行解析：{}",str);
			}
		}
		return className;
	}
	/**
	 * 扫描路径的前置检查，将路径中的点替换为斜杠
	 * @param packageDir - 扫描路径
	 * @return
	 * @throws IllegalArgumentException 启用的包扫描路径不能为空串!
	 */
	private static String checkPackageDir(String packageDir) {
//		if(packageDir == "") {
//			throw new IllegalArgumentException ("启用的包扫描路径不能为空串!");
//		}
		if(packageDir.indexOf(".") != -1) {
			packageDir = packageDir.replace(".", "/");
		}
		return packageDir;
	}
	
	//------------------------get、set、constructor------------------------
	public FileSearch() {
		super();
	}
}
