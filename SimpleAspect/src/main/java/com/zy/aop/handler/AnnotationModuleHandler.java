
package com.zy.aop.handler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.aop.interfaces.anno.AnnotaionModule;
import com.zy.reflect.analysis.repository.AopAnnoRepositoryImpl;
import com.zy.security.annotation.aop.After;
import com.zy.security.annotation.aop.AfterReturning;
import com.zy.security.annotation.aop.AfterThrowing;
import com.zy.security.annotation.aop.Around;
import com.zy.security.annotation.aop.Before;

/**
 * @author zy
 * @Date 2019-11-11 周一 下午 11:36:44
 * @Description 将切面方法织入切点以生成切面
 * @version
 * 
 */
public class AnnotationModuleHandler implements AnnotaionModule {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 每个String存储一个切点方法value属性值,每个class对象代表一个切面类，其中List容器空间为5.
	 * ,其中的method对象依次代表@Before、@Around、@After、@AfterReturning、@AfterThrowing
	 */
	private Map<String, Map<Class<?>, List<Method>>> aspect;
	
	
	@Override
	public Map<String, Map<Class<?>, List<Method>>> invoke(AopAnnoRepositoryImpl annotationRepository) {
		aspect = new HashMap<>();
		Map<String, List<Class<?>>> annoClz = annotationRepository.getAnnoClz();
		List<Class<?>> list = annoClz.get("simpleAspect");
		
		Map<String, Map<Class<?>, Method>> annoMeth = annotationRepository.getAnnoMethod();
		Map<Class<?>, Method> befores = annoMeth.get("before");
		Map<Class<?>, Method> arounds = annoMeth.get("around");
		Map<Class<?>, Method> afters = annoMeth.get("after");
		Map<Class<?>, Method> afterReturnings = annoMeth.get("afterReturning");
		Map<Class<?>, Method> afterThrowings = annoMeth.get("afterThrowing");
		
		for (Class<?> clz : list) {
			// 按序存储至多5个切面方法
			List<Method> arrayList = new ArrayList<>();
			// 存储切点方法与切面类的映射关系
			Map<Class<?>,List<Method>> mapz = new HashMap<>();
			mapz.put(clz, arrayList);
			
			String before = new String();
			String around = new String();
			String after = new String();
			String ar = new String();
			String at = new String();
			
			if(befores == null) {
				arrayList.add(null );	
			}
			else {
				before = addAspectMapForBefore(befores.get(clz),arrayList);
			}
			
			if(arounds == null) {
				arrayList.add(null );	
			}else {
				around = addAspectMapForAround(arounds.get(clz),arrayList);
			}
			
			if(afters == null) {
				arrayList.add(null );			
			} else {
				after = addAspectMapForAfter(afters.get(clz),arrayList);
			}
			
			if(afterReturnings == null) {
				arrayList.add(null );	
			}else {
				ar = addAspectMapForAfterReturning(afterReturnings.get(clz),arrayList);
			}	
			
			if(afterThrowings == null) {
				arrayList.add(null );	
			}else {
				at = addAspectMapForAfterThrowing(afterThrowings.get(clz),arrayList);
			}
			
			if(before.hashCode() != 0) {
				aspect.put(before, mapz);
			}
			if(around.hashCode() != 0) {
				aspect.put(before, mapz);
			}
			if(after.hashCode() != 0) {
				aspect.put(after, mapz);
			}
			if(ar.hashCode() != 0) {
				aspect.put(ar, mapz);
			}
			if(at.hashCode() != 0) {
				aspect.put(at, mapz);
			}
		}
		logger.info("切点方法与切面类映射完成.");
		return aspect;
	}

	
	/**
	 * 将 @before 注解所标注的切点方法与切点所关联
	 * @param meth - 存储标注@After、@Before等注解的方法对象
	 * @param clz - 切面方法类Class对象
	 * @param arrayList - 存储同一切点的各个切点方法
	 */
	private String addAspectMapForBefore(Method meth,List<Method> list) {
		list.add(meth);
		String value = "";
		Before before = meth.getAnnotation(Before.class);
		if (before != null) {
			value = before.value();
		}
		return value;
	}
	
	/**
	 * 将 @After 注解所标注的切点方法与切点所关联
	 * @param meth - 存储标注@After、@Before等注解的方法对象
	 * @param clz - 切面方法类Class对象
	 * @param arrayList - 存储同一切点的各个切点方法
	 */
	private String addAspectMapForAfter(Method meth,List<Method> list) {
		list.add(meth);
		String value = "";
		After after = meth.getAnnotation(After.class);
		if (after != null) {
			value = after.value();
		}
		return value;
	}
	
	/**
	 * 将 @Around 注解所标注的切点方法与切点所关联
	 * @param meth - 存储标注@After、@Before等注解的方法对象
	 * @param clz - 切面方法类Class对象
	 * @param arrayList - 存储同一切点的各个切点方法
	 */
	private String addAspectMapForAround(Method meth,List<Method> list) {
		list.add(meth);
		String value = "";
		Around around = meth.getAnnotation(Around.class);
		if (around != null) {
			value = around.value();
		}
		return value;
	}
	
	/**
	 * 将 @AfterReturning 注解所标注的切点方法与切点所关联
	 * @param meth - 存储标注@After、@Before等注解的方法对象
	 * @param clz - 切面方法类Class对象
	 * @param arrayList - 存储同一切点的各个切点方法
	 */
	private String addAspectMapForAfterReturning(Method meth,List<Method> list) {
		list.add(meth);
		String value = "";
		AfterReturning ar = meth.getAnnotation(AfterReturning.class);
		if (ar != null) {
			value = ar.value();
		}
		return value;
	}
	
	/**
	 * 将 @AfterThrowing 注解所标注的切点方法与切点所关联
	 * @param meth - 存储标注@After、@Before等注解的方法对象
	 * @param clz - 切面方法类Class对象
	 * @param arrayList - 存储同一切点的各个切点方法
	 */
	private String addAspectMapForAfterThrowing(Method meth,List<Method> list) {
		list.add(meth);
		String value = "";
		AfterThrowing at = meth.getAnnotation(AfterThrowing.class);
		if (at != null) {
			value = at.value();
		}
		return value;
	}
}
