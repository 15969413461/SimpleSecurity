
package com.zy.reflect.analysis.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.reflect.interfaces.repository.AopAnnotationRepository;

/**
 * @author zy
 * @Date 2019-11-10 周日 下午 10:36:14
 * @Description 根据注解功能进行分类，辅之Class对象,最终关联起Method、Filed等属性<br/>
 * 即：以Class对象为key,那么同一个类即使为多个方法标注了同一个注解 ,最终在容器中后面的方法会覆盖前面的方法,在一个类中保证了注解的唯一性。
 * @version
 */
public class AopAnnoRepositoryImpl implements AopAnnotationRepository {
	// class
	private Map<String, List<Class<?>>> annoClz;
	/**
	 * 以Class对象为key,那么同一个类即使为多个方法标注了同一个注解
	 * ,最终在容器中后面的方法会覆盖前面的方法,在一个类中保证了注解的唯一性
	 */
	// method
	private Map<String, Map<Class<?>,Method>> annoMethod;
	// field
	private Map<String, Map<Class<?>,Field>> annoField;
	
	// pointcut: value - method对象
	private Map<String, Method>  pointcut;

	// ------------------------------method------------------------------
	@Override
	public void addAnnoClz(Map<String, List<Class<?>>> map) {
		if(map == null || map.isEmpty()) {
			return ;
		} else if (annoClz == null) {
			annoClz = new HashMap<>();
		}
		annoClz.putAll(map);
	}

	@Override
	public void addAnnoMethod(Map<String, Map<Class<?>, Method>> map) {
		if(map == null || map.isEmpty()) {
			return ;
		} else if (annoMethod == null) {
			annoMethod = new HashMap<>();
		}
		annoMethod.putAll(map);
	}

	@Override
	public void addAnnoField(Map<String, Map<Class<?>, Field>> map) {
		if(map == null || map.isEmpty()) {
			return ;
		} else if (annoField == null) {
			annoField = new HashMap<>();
		}
		annoField.putAll(map);
	}
	
	public void addAnnoPointcut(Map<String, Method> map) {
		if (pointcut == null) {
			pointcut = new HashMap<>();
		}
		pointcut.putAll(map);
	}
	// ------------------------------get、set、Constructor------------------------------
	public Map<String, List<Class<?>>> getAnnoClz() {
		return annoClz;
	}
	public Map<String, Map<Class<?>, Method>> getAnnoMethod() {
		return annoMethod;
	}
	public Map<String, Map<Class<?>, Field>> getAnnoField() {
		return annoField;
	}
	public Map<String, Method>  getPointcut() {
		return pointcut;
	}

	public AopAnnoRepositoryImpl() {}

	@Override
	public String toString() {
		return "AopAnnoRepositoryImpl [annoClz=" + annoClz + ", annoMethod=" + annoMethod + ", annoField=" + annoField
				+ ", pointcut=" + pointcut + "]";
	}
	
}
