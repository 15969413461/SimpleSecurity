
package com.zy.reflect.analysis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* @author zy
* @Date 2019-11-10 周日 上午 01:34:09
* @Description 类名解析实现类
* @version 
*/
public class  ClassAnalysishandler  {
	private static Logger logger = LoggerFactory.getLogger(ClassAnalysishandler.class);
	
	public static List<Class<?>> analysis(List<String> list) {
		List<Class<?>> arrayList = new ArrayList<>();
		for (String string : list) {
			try {
				Class<?> forName = Class.forName(string);
				arrayList.add(forName);
			} catch (ClassNotFoundException e) {
				logger.error("此类名无法生成类模板：{}",string);
				e.printStackTrace();
			}
		}
		return arrayList;
	}
	
}
