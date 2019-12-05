
package com.zy.security.web.util;
/**
* @author zy
* @Date 2019-11-16 周六 下午 16:29:52
* @Description http请求类型
* @version 
*/
public enum HttpMethod {
	GET,POST;
	
	public static HttpMethod get(String name) {
		if(name == "" || name == null) {
			throw new IllegalArgumentException("参数不可为null或空串");
		}
		String name2 = name.toUpperCase();
		if(POST.name().equals(name2)) {
			return POST;
		}
		if(GET.name().equals(name2)) {
			return GET;
		}
		throw new RuntimeException("参数无匹配值，by: "+name);
	}
}
