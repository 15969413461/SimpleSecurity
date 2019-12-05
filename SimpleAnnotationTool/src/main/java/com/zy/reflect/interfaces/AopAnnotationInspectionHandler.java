
package com.zy.reflect.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author zy
* @Date 2019-11-10 周日 下午 02:59:47
* @Description - 查验注解处理接口
* @version 
*/
public interface AopAnnotationInspectionHandler {
	
	/**
	 * 查验注解，并对注解标注的类、接口、枚举进行分类
	 * @param map
	 * @return
	 */
	Map<String, List<Class<?>>> InspectionClz(Map<Class<?>, Set<Annotation>> map);
	/**
	 * 查验注解，并对注解标注的方法进行分类
	 * @param map
	 * @return
	 */
	Map<String, Map<Class<?>,Method>> InspectionMethod(Map<Method, Set<Annotation>> map,Class<?> clz);
	/**
	 * 查验注解，并对注解标注的属性进行分类
	 * @param map
	 * @return
	 */
	Boolean InspectionField(Map<Field, Set<Annotation>> map,Class<?> clz);
	/**
	 * 查验注解，并对注解标注的构造器进行分类
	 * @param map
	 * @return
	 */
	Boolean InspectionConstructor(Map<Constructor<?>, Set<Annotation>> map,Class<?> clz);
	/**
	 * 查验注解，并对注解标注的方法按类进行划分,限@SimplePointcut
	 * @param map
	 * @return
	 */
	void InspectionPointcut(Method meth,Annotation anno);
	/**
	 * 获得切点信息
	 * @return
	 */
	Map<String, Method> getPointcut();
}
