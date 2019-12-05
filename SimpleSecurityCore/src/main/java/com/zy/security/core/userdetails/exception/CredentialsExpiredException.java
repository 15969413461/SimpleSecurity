
package com.zy.security.core.userdetails.exception;
/**
* @author zy
* @Date 2019-11-15 周五 下午 15:23:34
* @Description 帐户的密码已过期
* @version 
*/
@SuppressWarnings("serial")
public class CredentialsExpiredException extends AccountStatusException {
	public CredentialsExpiredException() {}
	public CredentialsExpiredException(String msg) {
		super(msg);
	}
	public CredentialsExpiredException(String msg,Throwable t) {
		super(msg,t);
	}
}
