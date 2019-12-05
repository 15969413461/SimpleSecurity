
package com.zy.security.core.userdetails;
/**
* @author zy
* @Date 2019-11-25 周一 下午 15:41:40
* @Description
* @version 
*/
public final class WebAttribute  {
	/**
	 * 标记于请求中，存储失效的 {@link SessionInformation}对象，主要由于 {@link ConcurrentSessionControlAuthenticationStrategy}
	 */
	public static final String EXIPRED_SESSION = "expiredSession";
	/**
	 * 标记于请求中，存储会话迁移之前的会话id，主要用于 {@link ChangeSessionIdAuthenticationStrategy}
	 */
	public static final String CHANGER_SESSION = "changerSessionId";
	/**
	 * 标记于请求中，表示过滤器链中的过滤器显式的return
	 * @deprecated
	 */
	public static final String FILTER_HANDLE = "webFilterHandle";
	/**
	 * 标记于session中，存储之前被打断的 {@link HttpServletRequest}对象
	 */
	public static final String REQUEST_CACHE = "RequestCache";
}
