
package com.zy.security.core.authentication.exception;
/**
* @author zy
* @Date 2019-11-13 周三 下午 10:12:08
* @Description 用户身份验证失败所引起的异常基类
* @version 
*/
@SuppressWarnings("serial")
public class AuthenticationException extends Exception {
	public AuthenticationException() {}
	public AuthenticationException(String msg) {
		super(msg);
	}
	public AuthenticationException(String msg,Throwable t) {
		super(msg,t);
	}
}
