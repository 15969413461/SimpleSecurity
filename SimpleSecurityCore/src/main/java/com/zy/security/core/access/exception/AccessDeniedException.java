
package com.zy.security.core.access.exception;
/**
* @author zy
* @Date 2019-11-16 周六 下午 14:39:34
* @Description 当前用户无法访问某资源的异常(访问拒绝)
* @version 
*/
@SuppressWarnings("serial")
public class AccessDeniedException extends Exception {
	public AccessDeniedException() {}
	public AccessDeniedException(String msg) {
		super(msg);
	}
	public AccessDeniedException(String msg,Throwable t) {
		super(msg,t);
	}
}
