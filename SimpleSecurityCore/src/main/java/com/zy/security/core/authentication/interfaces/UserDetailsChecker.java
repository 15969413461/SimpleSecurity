
package com.zy.security.core.authentication.interfaces;

import com.zy.security.core.userdetails.exception.AccountStatusException;

/**
* @author zy
* @Date 2019-11-13 周三 下午 11:28:53
* @Description 检查UserDetails对象状态，状态异常则抛出对应的异常
* @version 
*/
public interface UserDetailsChecker {
	/**
	 * 检查UserDetails对象状态，状态异常则抛出对应的异常
	 * @param toCheck
	 */
	void preCheck(UserDetails userDetails) throws AccountStatusException;
	/**
	 * 检查密码是否过期
	 * @param toCheck
	 */
	void postCheck(UserDetails userDetails) throws AccountStatusException;
}
