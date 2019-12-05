
package com.zy.security.core.context;

/**
* @author zy
* @Date 2019-11-17 周日 上午 00:13:40
* @Description Security上下文操作接口
* @version 
*/
public interface SecurityContextHandler {
	static final String CONTEXT_KEY = "Security_Context_key";
	/**
	 * 获得上下文信息
	 * @return
	 */
	SecurityContext getContext();
	/**
	 * 设置上下文信息
	 * @param context
	 */
	void saveContext(SecurityContext context);
	/**
	 * 清空上下文
	 */
	void clearContext();
	/**
	 * 创建一个空的上下文
	 * @return
	 */
	SecurityContext createEmptyContext() ;
}
