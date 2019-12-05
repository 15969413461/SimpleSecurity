
package com.zy.reflect.interfaces;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
* @author zy
* @Date 2019-11-11 周一 上午 12:30:23
* @Description 以反射为核心，负责进行包扫描、创建Class对象和反射读取操作注解
* @version 
*/
public interface RelectHandler {
	/**
	 * 包扫描和注解归类的调用者
	 */
	void invoke(String packageDir);
	
	/** 
	 * 进行包扫描,获得其下所有有效的类文件名。然后根据类名转换为Class对象
	 */
//	List<Class<?>> beforeInvocation(String packageDir);
	
	/**
	 * 方法注解归类
	 * @param discern
	 * @param clz
	 */	
	void afterInvocation(List<Class<?>> list);
	
	/**
	 * 注解归类的finally模块，
	 * @param discernClz
	 * @param discernMethod
	 */
	void finallyInvocation(Map<Class<?>, Set<Annotation>> discernClz,Map<Method, Set<Annotation>> discernMethod);

	void invoke();
}
