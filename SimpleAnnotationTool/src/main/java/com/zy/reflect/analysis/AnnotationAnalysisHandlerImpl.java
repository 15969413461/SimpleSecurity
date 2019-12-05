
package com.zy.reflect.analysis;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.zy.reflect.interfaces.AnnotationAnalysisHandler;

/**
 * @author zy
 * @Date 2019-11-10 周日 上午 01:50:42
 * @Description 注解解析实现类<br/>
 * 解析位置:<br/>
 * &nbsp;&nbsp;&nbsp; TYPE、METHOD、FIELD、CONSTRUCTOR
 * @version
 */
public class AnnotationAnalysisHandlerImpl implements AnnotationAnalysisHandler {
//	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public Map<Class<?>, Set<Annotation>> discernClz(Class<?> clz) {
		Map<Class<?>, Set<Annotation>> hashMap = new HashMap<>();
		Set<Annotation> hashSet = new HashSet<>();
		
		// 获得标注于类、接口、枚举上的所有有效注解
		Annotation[] annotations = clz.getAnnotations();
		for (Annotation annotation : annotations) {
			hashSet.add(annotation);
		}
		hashMap.put(clz, hashSet);
		return hashMap;
	}

	@Override
	public Map<Method, Set<Annotation>> discernMethod(Class<?> clz) {
		HashMap<Method, Set<Annotation>> hashMap = new HashMap<>();
		
		Method[] methods = clz.getMethods();
		for (Method method : methods) {
			Annotation[] annotations = method.getAnnotations();
			Set<Annotation> hashSet = new HashSet<>();
			for (Annotation annotation : annotations) {
				hashSet.add(annotation);
				hashMap.put(method, hashSet);
			}
		}
		return hashMap;
	}

	@Override
	public Map<Field, Set<Annotation>> discernField(Class<?> clz) {
		HashMap<Field, Set<Annotation>> hashMap = new HashMap<>();
		
		Field[] fields = clz.getFields();
		for (Field field : fields) {
			Annotation[] annotations = field.getAnnotations();
			Set<Annotation> hashSet = new HashSet<>();
			
			for (Annotation annotation : annotations) {
				hashSet.add(annotation);
				hashMap.put(field, hashSet);
//				logger.info("Field："+field.toString());
			}
		}
		return hashMap;
	}

	@Override
	public Map<Constructor<?>, Set<Annotation>> discernConstructor(Class<?> clz) {
		return null;
	}
}
