
package com.zy.reflect.interfaces.repository;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author zy
 * @Date 2019-12-03 周二 下午 15:08:22
 * @Description 根据注解功能进行分类，辅之Class对象,最终关联起Method、Filed等属性<br/>
 * 即：以Class对象为key,那么同一个类即使为多个方法标注了同一个注解 ,最终在容器中后面的方法会覆盖前面的方法,在一个类中保证了注解的唯一性。
 * @version
 */
public interface AopAnnotationRepository {
	void addAnnoClz(Map<String, List<Class<?>>> map);

	void addAnnoMethod(Map<String, Map<Class<?>, Method>> map);

	void addAnnoField(Map<String, Map<Class<?>, Field>> map);

	void addAnnoPointcut(Map<String, Method> map);
}
