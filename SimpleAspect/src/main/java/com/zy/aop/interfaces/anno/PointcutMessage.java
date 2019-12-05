
package com.zy.aop.interfaces.anno;

import java.util.List;


/**
* @author zy
* @Date 2019-11-12 周二 上午 01:08:32
* @Description 切点定义表达式解析接口，可指定解析策略
* @version 
* @see com.zy.aop.handler.SimplePointCutAnalysisStrategy
*/
public interface PointcutMessage {
	/**
	 * 解析切点定义表达式获得切点所在类的限定名与切点方法名
	 * @param pointcutVal - 切点注解value属性值
	 * @return // list:1 --> [class,method]
	 */
//	Map<String,String> analysis(List<String> pointcutVals);
	/**
	 * 解析切点定义表达式获得切点所在类的限定名与切点方法名
	 * @param pointcutVal - 切点注解value属性值
	 * @return list:1 --> [class] <br/>
	 * 		   list:2 --> [method]
	 */
	List<String> analysis(String pointcutVal);
}
