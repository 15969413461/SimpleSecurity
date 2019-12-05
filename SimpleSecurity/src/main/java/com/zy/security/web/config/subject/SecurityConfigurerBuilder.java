
package com.zy.security.web.config.subject;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import com.zy.reflect.SecurityRelectHandler;
import com.zy.reflect.analysis.repository.SecurityAnnotationRepositoryImpl;
import com.zy.utils.AnnotaionAttribute;

/**
* @author zy
* @Date 2019-12-03 周二 下午 14:07:27
* @Description 负责创建{@link WebSecurityConfigurerAdapter}的子类，如果没有则直接创建WebSecurityConfigurerAdapter对象
* @version 
*/
public class SecurityConfigurerBuilder {
	public static WebSecurityConfigurerAdapter builder() throws InstantiationException, IllegalAccessException, 
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		SecurityRelectHandler handler = new SecurityRelectHandler();
		// 扫描根目录下的注解 
		handler.invoke();
		SecurityAnnotationRepositoryImpl annotationRepository = handler.getAnnotationRepository();
		
		// 获得@EnableSecurityConfiguration标注的Class对象
		Map<String, List<Class<?>>> annoClz = annotationRepository.getAnnoClz();
		List<Class<?>> list = annoClz.get(AnnotaionAttribute.enableSecurityConfiguration);
		
		// 获得uri与role的映射关系
		Map<String, String[]> annoMethod = annotationRepository.getAnnoMethod();
		
		if(list == null || list.isEmpty()) { // 默认配置环境
			return new WebSecurityConfigurerAdapter();
		}
		
		Class<?> configClz = list.get(0);
		WebSecurityConfigurerAdapter newInstance = (WebSecurityConfigurerAdapter) configClz.getDeclaredConstructor().newInstance();
		newInstance.setMap(annoMethod);
		return newInstance;
		
	}
}	
