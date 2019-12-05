
package com.zy.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.zy.reflect.analysis.AbstractRelectHandler;
import com.zy.reflect.analysis.SecurityAnnoInspectionHandler;
import com.zy.reflect.analysis.repository.SecurityAnnotationRepositoryImpl;

/**
* @author zy
* @Date 2019-12-01 周日 上午 01:43:37
* @Description SimpleSecurity注解解析
* @version  
*/
public class SecurityRelectHandler extends AbstractRelectHandler {
//	private Logger logger = LoggerFactory.getLogger(getClass());

	private SecurityAnnoInspectionHandler handler = new SecurityAnnoInspectionHandler();
	private SecurityAnnotationRepositoryImpl securityAnnotationRepository = new SecurityAnnotationRepositoryImpl();
	
	@Override
	public void finallyInvocation(Map<Class<?>, Set<Annotation>> discernClz,Map<Method, Set<Annotation>> discernMethod) {}

	@Override
	public void AnnoInspectionClz(Map<Class<?>, Set<Annotation>> discern) {
		Map<String, List<Class<?>>> inspectionClz = handler.InspectionClz(discern);
		this.securityAnnotationRepository.addAnnoClz(inspectionClz);
	}

	@Override
	public void AnnoInspectionMethod(Map<Method, Set<Annotation>> discern, Class<?> clz) {
		Map<String, String[]> inspectionMethod = handler.InspectionMethod(discern, clz);
		this.securityAnnotationRepository.addAnnoMethod(inspectionMethod);
	}

	@Override
	public void AnnoInspectionField(Map<Field, Set<Annotation>> discern, Class<?> clz) {
		
	}
	
	public SecurityAnnotationRepositoryImpl getAnnotationRepository() {
		return this.securityAnnotationRepository;
	}
}
