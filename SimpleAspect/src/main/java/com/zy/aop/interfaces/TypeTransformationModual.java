
package com.zy.aop.interfaces;

import java.lang.reflect.Parameter;

import javassist.CtClass;

/**
* @author zy
* @Date 2019-11-13 周三 下午 02:41:35
* @Description java基本数据类型与javassist数据类型的转换
* @version 
*/
public interface TypeTransformationModual {
	/**
	 * java基本数据类型与javassist数据类型的转换
	 * @param clzs
	 * @return
	 */
	CtClass[] Transformation(Class<?> ... clzs);
	
	/**
	 * java基本数据类型与javassist数据类型的转换
	 * @param clz
	 * @return
	 */
	CtClass Transformation(Class<?> clz) throws Exception;
	
	/**
	 * java基本数据类型与javassist数据类型的转换
	 * @param clz
	 * @return
	 */
	CtClass[] Transformation(Parameter[] parames);
}
