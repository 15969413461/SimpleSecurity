
package com.zy.reflect.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
* @author zy
* @Date 2019-11-10 周日 上午 01:27:25
* @Description 注解解析接口
* @version 
*/
public interface AnnotationAnalysisHandler {
	/**
	 * 从类的Class模板中获得注解信息,针对于ElementType.TYPE
	 * @param clz - 解析类的Class对象
	 */
	Map<Class<?>, Set<Annotation>> discernClz(Class<?> clz);
	/**
	 * 从类的Class模板中获得注解信息,针对于ElementType.METHOD
	 * @param clz - 解析类的Class对象
	 */
	Map<Method, Set<Annotation>> discernMethod(Class<?> clz);
	/**
	 * 从类的Class模板中获得注解信息,针对于ElementType.FIELD
	 * @param clz - 解析类的Class对象
	 */
	Map<Field, Set<Annotation>> discernField(Class<?> clz);
	/**
	 * 从类的Class模板中获得注解信息,针对于ElementType.CONSTRUCTOR 
	 * @param clz - 解析类的Class对象
	 */
	Map<Constructor<?>, Set<Annotation>> discernConstructor (Class<?> clz);
}
