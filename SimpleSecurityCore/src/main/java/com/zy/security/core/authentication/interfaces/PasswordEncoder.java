
package com.zy.security.core.authentication.interfaces;
/**
* @author zy
* @Date 2019-11-15 周五 下午 16:40:01
* @Description 密码加密与密码匹配接口
* @version 
*/
public interface PasswordEncoder {

	/**
	 * 凭证比较
	 * @param authPWd - 比较的密码
	 * @param userDetailsPwd - 外部存储中已加密的密码
	 * @return
	 */
	boolean matches(String authPWd, String userDetailsPwd);
	/**
	 * 密码加密
	 * @param rawPassword - CharSequenc为String、StringBuilder、StringBuffer的父接口，此处接收要进行加密的凭证
	 * @return
	 */
	String encode(CharSequence rawPassword);
}
