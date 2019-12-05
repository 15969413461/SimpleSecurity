
package com.zy.reflect.interfaces.repository;

import java.util.List;
import java.util.Map;

/**
* @author zy
* @Date 2019-12-01 周日 上午 01:48:08
* @Description 
* @version 
*/
public interface SecurityAnnotationRepository  {
	void addAnnoClz(Map<String, List<Class<?>>> map);

	void addAnnoMethod(Map<String, String[]> strs);

	void addAnnoField(Map<String, String[]> strs);
}
