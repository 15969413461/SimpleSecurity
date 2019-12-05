
package com.zy.security.core.authentication.exception;
/**
* @author zy
* @Date 2019-11-13 周三 下午 10:12:08
* @Description 无可用的有效验证器
* @version 
*/
@SuppressWarnings("serial")
public class AuthenticationProviderNotFoundException extends AuthenticationException {
	public AuthenticationProviderNotFoundException() {}
	public AuthenticationProviderNotFoundException(String msg) {
		super(msg);
	}
	public AuthenticationProviderNotFoundException(String msg,Throwable t) {
		super(msg,t);
	}
}
