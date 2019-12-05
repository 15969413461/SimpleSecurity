
package com.zy.security.web.interceptor;

import java.lang.reflect.Method;

import com.zy.security.core.access.AbstractSecurityInterceptor;
import com.zy.security.web.interfaces.MethodInterceptor;

/**
* @author zy
* @Date 2019-11-16 周六 下午 16:19:44
* @Description 使用aop实现权限限制
* @version 
*/
public class MethodSecurityInterceptor extends AbstractSecurityInterceptor implements MethodInterceptor {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Method.class.isAssignableFrom(clazz);
	}

	@Override
	public void invoke(Method method) throws Throwable {
		try {
			super.beforeInvocation(method);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
}
