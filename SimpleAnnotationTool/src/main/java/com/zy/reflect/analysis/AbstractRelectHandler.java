
package com.zy.reflect.analysis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.reflect.interfaces.AnnotationAnalysisHandler;
import com.zy.reflect.interfaces.RelectHandler;
import com.zy.utils.FileSearch;

/**
 * @author zy
 * @Date 2019-12-03 周二 下午 16:19:06
 * @Description
 * @version
 */
public abstract class AbstractRelectHandler implements RelectHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 进行包扫描,获得其下所有有效的类文件名。然后根据类名转换为Class对象
	 */
	private final List<Class<?>> beforeInvocation(String packageDir) {
		// 包扫描获得其下class文件名
		List<String> className = FileSearch.search(packageDir);
		logger.info("className: {}", className);
		
		// 将文件名转化为Class对象
		List<Class<?>> list = ClassAnalysishandler.analysis(className);
		
		logger.info("analysis: {}", list);
		return list;
	}

	@Override
	public void invoke(String packageDir) {
		List<Class<?>> beforeInvocation = beforeInvocation(packageDir);
		afterInvocation(beforeInvocation);
	}
 
	@Override
	public void invoke() {
		List<Class<?>> beforeInvocation = beforeInvocation("");
		afterInvocation(beforeInvocation);
	}

	/**
	 * 从类的Class模板中获得注解信息,然后进行归类
	 */
	@Override
	public void afterInvocation(List<Class<?>> list) {
		// 根据Class对象获得类中注解
		AnnotationAnalysisHandler saah = new AnnotationAnalysisHandlerImpl();
		
		for (Class<?> clz : list) {
			Map<Class<?>, Set<Annotation>> discernClz = saah.discernClz(clz);
			Map<Method, Set<Annotation>> discernMethod = saah.discernMethod(clz);
			
			AnnoInspectionClz(discernClz);
			AnnoInspectionMethod(discernMethod, clz);
		}
	}

	/**
	 * 类、接口、枚举注解归类
	 * 
	 * @param discern
	 */
	public abstract void AnnoInspectionClz(Map<Class<?>, Set<Annotation>> discern);

	/**
	 * 方法注解归类
	 * 
	 * @param discern
	 * @param clz
	 */
	public abstract void AnnoInspectionMethod(Map<Method, Set<Annotation>> discern, Class<?> clz);

	/**
	 * 属性注解归类
	 * 
	 * @param discern
	 */
	public abstract void AnnoInspectionField(Map<Field, Set<Annotation>> discern, Class<?> clz);
}
