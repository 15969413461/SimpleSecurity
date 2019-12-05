
package com.zy.security.core.userdetails.exception;
/**
* @author zy
* @Date 2019-11-15 周五 下午 15:23:34
* @Description 帐户已过期
* @version 
*/
@SuppressWarnings("serial")
public class AccountExpiredException extends AccountStatusException {
	public AccountExpiredException() {}
	public AccountExpiredException(String msg) {
		super(msg);
	}
	public AccountExpiredException(String msg,Throwable t) {
		super(msg,t);
	}
}
