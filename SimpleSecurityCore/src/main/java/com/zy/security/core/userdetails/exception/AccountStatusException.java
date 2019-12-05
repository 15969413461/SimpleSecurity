
package com.zy.security.core.userdetails.exception;

import com.zy.security.core.authentication.exception.AuthenticationException;

/**
* @author zy
* @Date 2019-11-13 周三 下午 10:23:28
* @Description 由用户帐户状态（锁定、禁用等）引起的身份验证异常的基类
* @version 
*/
@SuppressWarnings("serial")
public class AccountStatusException extends AuthenticationException {
	public AccountStatusException() {}
	public AccountStatusException(String msg) {
		super(msg);
	}
	public AccountStatusException(String msg,Throwable t) {
		super(msg,t);
	}
}
