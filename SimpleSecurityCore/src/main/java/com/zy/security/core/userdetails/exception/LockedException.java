
package com.zy.security.core.userdetails.exception;
/**
* @author zy
* @Date 2019-11-15 周五 下午 15:23:34
* @Description 账户不可用
* @version 
*/
@SuppressWarnings("serial")
public class LockedException extends AccountStatusException {
	public LockedException() {}
	public LockedException(String msg) {
		super(msg);
	}
	public LockedException(String msg,Throwable t) {
		super(msg,t);
	}
}
