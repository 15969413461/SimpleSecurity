
package com.zy.security.core.authentication.compare;

import com.zy.security.core.authentication.interfaces.PasswordEncoder;

/**
* @author zy
* @Date 2019-11-29 周五 下午 16:51:46
* @Description 默认的凭证比对
* @version 
*/
public class DefaultPasswordEncoder implements PasswordEncoder {

	@Override
	public boolean matches(String authPWd, String userDetailsPwd) {
		return authPWd.equals(userDetailsPwd);
	}

	@Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

}
