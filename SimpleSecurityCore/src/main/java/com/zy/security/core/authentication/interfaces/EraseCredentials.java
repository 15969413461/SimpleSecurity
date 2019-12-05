
package com.zy.security.core.authentication.interfaces;
/**
* @author zy
* @Date 2019-11-15 周五 下午 13:49:00
* @Description 凭证清除标记接口，若Token需擦除密码、验证码等信息则需要实现此接口
* @version 
*/
public interface EraseCredentials {
	/**
	 * 清除保密信息，如密码，验证码等
	 */
	void eraseCredentials();
}
