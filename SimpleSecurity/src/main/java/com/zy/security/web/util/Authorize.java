
package com.zy.security.web.util;

/**
* @author zy
* @Date 2019-12-01 周日 下午 22:39:49
* @Description {@link Authorization} 所必须的权限验证规则标识
* @version 
*/
public final class Authorize {
	/** 不作限制  */
	public static final String permitAll = "permitAll";
	
	/** 任何人都不允许使用  */
	public static final String denyAll = "denyAll";
	
	/** 仅匿名用户允许使用  - AnonymousAuthenticationToken   */
	public static final String anonymous = "anonymous";
	
	/** 仅经过身份验证的用户允许使用  - Token:[authenticated=true]  */
	public static final String authenticated = "authenticated";
	
	/** 已通过身份验证且未被“记住”的用户允许使用  - !RememberMeAuthenticationToken   */
	public static final String fullyAuthenticated = "fullyAuthenticated";
	
	/** 已记住的用户允许使用  - RememberMeAuthenticationToken*/
	public static final String rememberMe = "rememberMe";
	
	/** 使用权限验证的标记  */
	public static final String role = "role";
}
