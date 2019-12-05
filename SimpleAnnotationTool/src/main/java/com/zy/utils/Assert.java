
package com.zy.utils;

/**
* @author zy
* @Date 2019-11-23 周六 下午 14:27:03
* @Description
* @version 
*/
public class Assert {
	/**
	 * 非空检查
	 * @param request
	 * @param string
	 */
	public static void notNull(Object obj, String message) {
		if(obj == null) {
			throw new IllegalArgumentException(message);
		}
	}
}
