
package com.zy.security.core.context;

import java.io.ObjectStreamException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.zy.security.core.token.Authentication;

/**
 * @author zy
 * @Date 2019-11-20 周三 下午 14:01:04
 * @Description 对SecurityContext的操作进行静态引用,ThreadLocal只是
 * @version
 */
@SuppressWarnings("serial")
public class SecurityContextStrategy implements Serializable {
	private static SecurityContextHandler handler = new SecurityContextThreadLocalHandler();
	
	private SecurityContextStrategy() {
		if (handler != null) {
			throw new IllegalArgumentException("单例对象已创建");
		}
	}

	// 防止反序列化创建对象,实际是一种回调
	public Object readResolve() throws ObjectStreamException {
		return SecurityContextStrategy.handler;
	}

	/**
	 * 更改实现类,默认使用 {@link SecurityContextThreadLocalHandler}
	 * @param handler
	 */
	public static void setHandler(SecurityContextHandler handler) {
		SecurityContextStrategy.handler = handler;
	}

	/**
	 * 获得上下文信息存储库中获得SecurityContext对象,若没有存储库的引用则新建一个SecurityContext对象
	 * @return
	 */
	public static SecurityContext loadContext(HttpServletRequest request) {
		HttpSession httpSession = request.getSession(false);
		if(httpSession == null) {
			return null;
		}
		return (SecurityContext) httpSession.getAttribute(SecurityContextHandler.CONTEXT_KEY);
	}
	/**
	 * ThreadLocal设置初始化的上下文信息,在单次请求内有效
	 */
	public static void setContext(SecurityContext context) {
		handler.saveContext(context);
	}
	
	/**
	 * 从ThreadLocal中上下文信息
	 * @return 返回的SecurityContext必定不为null
	 */
	public static SecurityContext getContext() {
		return handler.getContext();
	}

	/**
	 * 将上下文信息存储到上下文存储库中，在单次会话中有效
	 * @param context - 存储的SecurityContext对象
	 * @param request - 当前HttpServletRequest对象
	 */
	public static void saveContext(SecurityContext context, HttpServletRequest request) {
		if(context == null) {
			return ;
		}
		SecurityContextStrategy.handler.saveContext(context);
		
		// 返回与此请求关联的当前会话，或者如果请求没有会话，则创建一个会话
		HttpSession session = request.getSession();
		session.setAttribute(SecurityContextHandler.CONTEXT_KEY, context);
	}
	/**
	 * 将ThreadLocal之中的上下文信息存储到存储库中,需
	 * @param request - 当前HttpServletRequest对象
	 * @param auth - 存储的Authentication对象
	 */
	public static void saveContext(HttpServletRequest request,Authentication auth) {
		SecurityContext context = getContext();
		if(context == null) {
			return ;
		}
		context.setAuthentication(auth);
		
		// 返回与此请求关联的当前会话，或者如果请求没有会话，则创建一个会话
		HttpSession session = request.getSession();
		session.setAttribute(SecurityContextHandler.CONTEXT_KEY, context);
	}
	/**
	 * 清空ThreadLocal之中的上下文信息
	 */
	public static void clearContext() {
		SecurityContextStrategy.handler.clearContext();
	}
	/**
	 * 清空存储库中的上下文信息
	 */
	public static void clearRepositoryContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) {
			return ;
		}
		session.removeAttribute(SecurityContextHandler.CONTEXT_KEY);
	}

	/**
	 * 创建一个空的上下文
	 * @return
	 */
	public static SecurityContext createEmptyContext() {
		return SecurityContextStrategy.handler.createEmptyContext();
	}

	/**
	 * 判断当前请求所关联的上下文存储库中是否有与之对应的SecurityContext
	 * @param request
	 * @return true:包含，false:不包含
	 */
	public static boolean containsContext(HttpServletRequest request) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			return false;
		}

		return session.getAttribute(SecurityContextHandler.CONTEXT_KEY) != null;
	}

}
