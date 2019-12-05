
package com.zy.reflect.analysis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.reflect.interfaces.SecurityAnnotationInspectionHandler;
import com.zy.security.annotation.EnableSecurityConfiguration;
import com.zy.security.annotation.PreAuthorize;
import com.zy.utils.AnnotaionAttribute;

/**
 * @author zy
 * @Date 2019-11-10 周日 下午 03:32:49
 * @Description - 查验注解类型,对SimpleSecurity相关注解进行处理
 * @version
 */
public class SecurityAnnoInspectionHandler implements SecurityAnnotationInspectionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Map<String, List<Class<?>>> InspectionClz(Map<Class<?>, Set<Annotation>> map) {
		Map<String, List<Class<?>>> hashMap = new HashMap<>();
		
		for (Class<?> clz : map.keySet()) {
			for (Annotation anno : map.get(clz)) {
				if(enableSecurityConfiguration(anno)) {
					
					if(!hashMap.containsKey(AnnotaionAttribute.enableSecurityConfiguration)) {
						List<Class<?>> arrayList = new ArrayList<>();
						arrayList.add(clz);
						
						logger.info("@EnableSecurityConfiguration：{}",clz);
						
						// 在存储于最终容器时，会发生值覆盖，保证了类级注解的唯一性，若允许多个存在则不宜使用AnnotaionAttribute的标记，比如使用类名映射
						hashMap.put(AnnotaionAttribute.enableSecurityConfiguration,arrayList);
						continue;
					} 
				}
			}
		}
		return hashMap;
	}

	@Override
	public Map<String, String[]> InspectionMethod(Map<Method, Set<Annotation>> map,Class<?> clz) {
		Map<String, String[]> hashMap = new HashMap<>();
		
		for (Method meth : map.keySet()) {
			for (Annotation annotation : map.get(meth)) {
				
				if(preAuthorize(annotation)) {
					PreAuthorize pre = (PreAuthorize)annotation;
					if(!hashMap.containsKey(pre.url())) {
						hashMap.put(pre.url(),pre.value());
						
						logger.info("@PreAuthorize：{}",clz);
						
						continue;
					} 
					// 在此做注解的存在唯一性判断（同一个类中）
//					logger.error("@XXX 不唯一,by: {}",meth);
//					throw new RuntimeException("@XXX 不唯一,by: "+meth);
				}
				// 紧接着是其他方法级注解的分类
			}
		}
		return hashMap;
	}

	@Override
	public Boolean InspectionField(Map<Field, Set<Annotation>> map, Class<?> clz) {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Boolean InspectionConstructor(Map<Constructor<?>, Set<Annotation>> map, Class<?> clz) {
		// TODO 自动生成的方法存根
		return null;
	}


	//-------------------------------注解类型判断-------------------------------------
	private boolean preAuthorize(Annotation anno) {
		if (anno instanceof PreAuthorize) {
			return true;
		}
		return false;
	}
	private boolean enableSecurityConfiguration (Annotation anno) {
		if (anno instanceof EnableSecurityConfiguration) {
			return true;
		}
		return false;
	}
}
