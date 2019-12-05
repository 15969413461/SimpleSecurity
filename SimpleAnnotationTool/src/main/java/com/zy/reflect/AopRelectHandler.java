
package com.zy.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.reflect.analysis.AbstractRelectHandler;
import com.zy.reflect.analysis.AopAnnotationInspectionHandlerImpl;
import com.zy.reflect.analysis.repository.AopAnnoRepositoryImpl;

/**
 * @author zy
 * @Date 2019-11-11 周一 上午 12:35:54
 * @Description 包扫描与注解归类，最后封装为AnnotationRepository对象（成员变量）, 功能性测试类
 * @version
 */
public class AopRelectHandler extends AbstractRelectHandler {
	// slf4j+logback
	@SuppressWarnings("unused")
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private AopAnnotationInspectionHandlerImpl handler = new AopAnnotationInspectionHandlerImpl();
	private AopAnnoRepositoryImpl aopAnnotationRepository;

	
	public AopRelectHandler() {
		aopAnnotationRepository = new AopAnnoRepositoryImpl();
	}


	@Override
	public void finallyInvocation(Map<Class<?>, Set<Annotation>> discernClz,Map<Method, Set<Annotation>> discernMethod) {}

	/**
	 * 类、接口、枚举注解归类
	 * @param discern
	 */	
	@Override
	public void AnnoInspectionClz(Map<Class<?>, Set<Annotation>> discern) {
		// 将aop相关注解归类
		Map<String, List<Class<?>>> aop = handler.InspectionClz(discern);
		aopAnnotationRepository.addAnnoClz(aop);
	}
	
	@Override
	public void AnnoInspectionMethod(Map<Method, Set<Annotation>> discern, Class<?> clz) {
		Map<String, Map<Class<?>, Method>> aop = handler.InspectionMethod(discern,clz);
		// 前置、后置方法...
		aopAnnotationRepository.addAnnoMethod(aop);
		// 切点
		aopAnnotationRepository.addAnnoPointcut(handler.getPointcut());
	}

	/**
	 * 属性注解归类
	 * @param discern
	 */
	@Override
	public void AnnoInspectionField(Map<Field, Set<Annotation>> discern, Class<?> clz) {

	}

	public AopAnnoRepositoryImpl getAnnotationRepository() {
		return aopAnnotationRepository;
	}
}
