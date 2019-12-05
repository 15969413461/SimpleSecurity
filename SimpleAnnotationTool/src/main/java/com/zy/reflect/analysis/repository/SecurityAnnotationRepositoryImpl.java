
package com.zy.reflect.analysis.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zy.reflect.interfaces.repository.SecurityAnnotationRepository;

/**
* @author zy
* @Date 2019-12-03 周二 下午 15:17:39
* @Description
* @version 
*/
public class SecurityAnnotationRepositoryImpl implements SecurityAnnotationRepository {
	/** class [annoName:clz] */
	private Map<String, List<Class<?>>> annoClz;
	
	/**
	 * method [uri:annoName] <br/>
	 * key与value均来来自于注解
	 */
	private Map<String ,String[]> preAuthorize;
	
	
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
	public void addAnnoMethod(Map<String, String[]> strs) {
		if(strs == null || strs.isEmpty()) {
			return ;
		} else if (preAuthorize == null) {
			preAuthorize = new HashMap<>();
		}
		preAuthorize.putAll(strs);
	}

	@Override
	public void addAnnoField(Map<String, String[]> strs) {
		
	}
	
	
	public Map<String, List<Class<?>>> getAnnoClz() {
		return annoClz;
	}
	public void setAnnoClz(Map<String, List<Class<?>>> annoClz) {
		this.annoClz = annoClz;
	}
	public Map<String, String[]> getAnnoMethod() {
		return preAuthorize;
	}
	public void setAnnoMethod(Map<String, String[]> annoMethod) {
		this.preAuthorize = annoMethod;
	}

	@Override
	public String toString() {
		return "SecurityAnnotationRepositoryImpl [annoClz=" + annoClz + ", annoMethod=" + preAuthorize + "]";
	}
	
}
