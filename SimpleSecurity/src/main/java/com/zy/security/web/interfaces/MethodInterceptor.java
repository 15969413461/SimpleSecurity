
package com.zy.security.web.interfaces;

import java.lang.reflect.Method;

/**
* @author zy
* @Date 2019-11-16 周六 下午 23:16:14
* @Description 授权注解标记接口
* @version 
*/
public interface MethodInterceptor {
	/**
	 * aop前置方法所调用的方法定义 
	 * @param method
	 * @throws Throwable
	 */
	void invoke(Method method) throws Throwable;
}
