
package com.zy.security.core.authentication.interfaces;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-13 周三 下午 09:45:34
* @Description 执行身份验证的逻辑入口，将传递Token到其下的各个验证器进行验证操作
* @version 
*/
public interface AuthenticationManager {
	Authentication authenticate(Authentication authentication) throws AuthenticationException;
}
