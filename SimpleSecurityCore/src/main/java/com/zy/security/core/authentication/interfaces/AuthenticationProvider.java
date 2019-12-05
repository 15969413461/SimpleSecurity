
package com.zy.security.core.authentication.interfaces;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-13 周三 下午 09:53:57
* @Description 验证器,封装用户身份验证逻辑
* @version 
*/
public interface AuthenticationProvider {
	/**
	 * 具体的认证逻辑
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	Authentication authenticate(Authentication authentication) throws AuthenticationException;

	/**
	 * 判别此Token对象的数据格式是否能被本类所支持
	 * @param authentication
	 * @return
	 */
	boolean supports(Class<?> authentication);
}
