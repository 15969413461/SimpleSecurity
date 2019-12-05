
package com.zy.security.core.access.exception;
/**
* @author zy
* @Date 2019-11-16 周六 下午 14:37:28
* @Description 权限不足
* @version 
*/
@SuppressWarnings("serial")
public class InsufficientAuthorityException extends Exception {
	public InsufficientAuthorityException() {}
	public InsufficientAuthorityException(String msg) {
		super(msg);
	}
	public InsufficientAuthorityException(String msg,Throwable t) {
		super(msg,t);
	}
}
