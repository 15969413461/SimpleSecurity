
package com.zy.reflect.analysis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zy.reflect.interfaces.AopAnnotationInspectionHandler;
import com.zy.security.annotation.aop.After;
import com.zy.security.annotation.aop.AfterReturning;
import com.zy.security.annotation.aop.AfterThrowing;
import com.zy.security.annotation.aop.Around;
import com.zy.security.annotation.aop.Before;
import com.zy.security.annotation.aop.SimpleAspect;
import com.zy.security.annotation.aop.SimplePointcut;

/**
 * @author zy
 * @Date 2019-11-10 周日 下午 03:32:49
 * @Description - 查验注解类型,对aop注解进行分类处理
 * @version
 */
public class AopAnnotationInspectionHandlerImpl implements AopAnnotationInspectionHandler {
//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<String, Method> pointMap = new HashMap<>();;
	
	
	@Override
	public Map<String, List<Class<?>>> InspectionClz(Map<Class<?>, Set<Annotation>> map) {
		Map<String, List<Class<?>>> hashMap = new HashMap<>();
		Set<Annotation> set = null;
		List<Class<?>> list = new ArrayList<Class<?>>();
		for (Class<?> clz : map.keySet()) {
			set = map.get(clz);

			for (Iterator<Annotation> iterator = set.iterator(); iterator.hasNext();) {

				Annotation annotation = (Annotation) iterator.next();
				if (simpleAspect(annotation)) { // 分类
					list.add(clz);
					hashMap.put("simpleAspect", list);
				}
			}
		}
		return hashMap;
	}

	@Override
	public Map<String, Map<Class<?>,Method>> InspectionMethod (Map<Method, Set<Annotation>> map,Class<?> clz) {
		Map<String, Map<Class<?>,Method>> hashMap = new HashMap<>();
		Set<Annotation> set = null;

		for (Method meth : map.keySet()) {
			set = map.get(meth);
			Map<Class<?>,Method> hm = new HashMap<>();

			for (Iterator<Annotation> iterator = set.iterator(); iterator.hasNext();) {
				Annotation annotation = (Annotation) iterator.next();
				if (after(annotation)) {
					// 保证方法要么是后置方法，要么是前置方法,若同一个类有两个前置方法则后一个对前一个覆盖
					hm.put(clz,meth);  // 方法与Class相关联
					hashMap.put("after", hm); // 注解作用与方法相关联
					continue;
				} else if (before(annotation)) {
					hm.put(clz,meth);
					hashMap.put("before", hm);
					continue;
				}else if (afterReturning(annotation)) {
					hm.put(clz,meth);
					hashMap.put("afterReturning", hm);
					continue;
				}else if (afterThrowing(annotation)) {
					hm.put(clz,meth);
					hashMap.put("afterThrowing", hm);
					continue;
				}else if (around(annotation)) {
					hm.put(clz,meth);
					hashMap.put("around", hm);
					continue;
				}else if (simplePointcut(annotation)) {
					InspectionPointcut(meth,annotation);
					continue;
				}
//				logger.warn("未找到合适的分类,by：[class：{}，  method：{}] ",clz.getSimpleName(),meth.getName());
			}
		}
		return hashMap;
	}
	
	@Override
	public void InspectionPointcut(Method meth,Annotation point) {
		String str = ((SimplePointcut) point).value();
		pointMap.put(str, meth);
	}
	
	@Override
	public Boolean InspectionField(Map<Field, Set<Annotation>> map,Class<?> clz) {
		return null;
	}

	@Override
	public Boolean InspectionConstructor(Map<Constructor<?>, Set<Annotation>> map,Class<?> clz) {
		return null;
	}

	
	public Map<String, Method> getPointcut(){
		return this.pointMap;
	}
	
	private boolean after(Annotation anno) {
		if (anno instanceof After) {
			return true;
		}
		return false;
	}

	private boolean afterReturning(Annotation anno) {
		if (anno instanceof AfterReturning) {
			return true;
		}
		return false;
	}

	private boolean before(Annotation anno) {
		if (anno instanceof Before) {
			return true;
		}
		return false;
	}

	private boolean afterThrowing(Annotation anno) {
		if (anno instanceof AfterThrowing) {
			return true;
		}
		return false;
	}

	private boolean around(Annotation anno) {
		if (anno instanceof Around) {
			return true;
		}
		return false;
	}

	private boolean simpleAspect(Annotation anno) {
		if (anno instanceof SimpleAspect) {
			return true;
		}
		return false;
	}

	private boolean simplePointcut(Annotation anno) {
		if (anno instanceof SimplePointcut) {
			return true;
		}
		return false;
	}

}
