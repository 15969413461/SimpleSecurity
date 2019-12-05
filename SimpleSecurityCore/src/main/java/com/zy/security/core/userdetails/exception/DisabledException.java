
package com.zy.security.core.userdetails.exception;
/**
* @author zy
* @Date 2019-11-15 周五 下午 15:23:34
* @Description 帐号不可用
* @version 
*/
@SuppressWarnings("serial")
public class DisabledException extends AccountStatusException {
	public DisabledException() {}
	public DisabledException(String msg) {
		super(msg);
	}
	public DisabledException(String msg,Throwable t) {
		super(msg,t);
	}
}
