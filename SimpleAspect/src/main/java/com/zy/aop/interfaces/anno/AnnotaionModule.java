
package com.zy.aop.interfaces.anno;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import com.zy.reflect.analysis.repository.AopAnnoRepositoryImpl;

/**
* @author zy
* @Date 2019-11-11 周一 下午 11:33:07
* @Description 根据切点方法注解的value值与切点的value值将切点与切点方法相关联。
* <p>若某切面方法类下的所有切点都未指定value值,那么将关联失败<p/>
* @version 
*/
public interface AnnotaionModule {
	/**
	 * 
	 * 类拥有@SimpleAspect注解为切面方法类,此类下的切面方法注解（@Before、@After等）才会生效。<br/>
	 * 拥有@SimplePointcut注解的类为切面类，此注解只支持定义切点方法。且@SimplePointcut的value值与切面方法注解的value值是二者的连绳
	 * @see com.zy.aop.handler.AnnotationModuleHandler.aspect（切面类容器）
	 * @return 存储固定顺序的切面类容器
	 */
	Map<String, Map<Class<?>, List<Method>>> invoke(AopAnnoRepositoryImpl annotationRepository);
}
