
package com.zy.security.core.context;

import com.zy.security.core.token.UsernamePasswordAuthenticationToken;

/**
 * @author zy
 * @Date 2019-11-17 周日 上午 00:03:07
 * @Description
 * @version
 */
public final class SecurityContextThreadLocalHandler implements SecurityContextHandler {
	private static final ThreadLocal<SecurityContext> contextHolder = new ThreadLocal<>();

	@Override
	public SecurityContext getContext() {
		SecurityContext context = contextHolder.get();
		if (context == null) {
			context = this.createEmptyContext();
		}
		return context;
	}

	@Override
	public void saveContext(SecurityContext context) {
		if (context == null) {
			throw new IllegalArgumentException("SecurityContext参数不可为空值");
		}
		contextHolder.set(context);
	}

	@Override
	public void clearContext() {
		contextHolder.remove();;
	}

	@Override
	public SecurityContext createEmptyContext() {
		return new SecurityContextImpl();
	}

	public static void main(String[] args) {
		SecurityContextThreadLocalHandler handler = new SecurityContextThreadLocalHandler();

		SecurityContextImpl securityContextImpl = new SecurityContextImpl();
		securityContextImpl.setAuthentication(new UsernamePasswordAuthenticationToken("张三", "123456"));
		SecurityContextImpl securityContextImpl2 = new SecurityContextImpl();
		securityContextImpl2.setAuthentication(new UsernamePasswordAuthenticationToken("张三2", "123456"));

		handler.saveContext(securityContextImpl);
		handler.saveContext(securityContextImpl2);
		
		System.out.println(handler.getContext());

		// 从新线程中获取
		new Thread() {
			public void run() {
				System.out.println(handler.getContext());
			};
		}.start();
	}
	
}
