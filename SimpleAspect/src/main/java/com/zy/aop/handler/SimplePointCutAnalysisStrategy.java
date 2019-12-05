
package com.zy.aop.handler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.aop.interfaces.anno.PointcutMessage;

/**
 * @author zy
 * @Date 2019-11-12 周二 上午 01:14:07
 * @Description 简易切点属性解析策略.<br/>
 * 	格式：类名%切点方法名称 。<br/>
 * 	如：某包"com.zy"下切点方法为test(),切点方法所在类的类名为Test,则value=“com.zy.Test%test()”。
 * @version
 */
public class SimplePointCutAnalysisStrategy implements PointcutMessage {

//	@Override 
	public Map<String,String> analysis(List<String> pointcutVals) {
		// com.text.Test$test()
		Map<String,String> map = new HashMap<>();
		String[] split = null;
		for (String str : pointcutVals) {
			split = str.split("%");
			map.put(split[0], split[1]);
		}
		return map;
	}
	public List<String> analysis(String pointcutVal) {
		String[] split = pointcutVal.split("\\$");
		return Arrays.asList(split);
	}
}
