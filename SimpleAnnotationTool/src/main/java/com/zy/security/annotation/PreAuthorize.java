
package com.zy.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author zy
* @Date 2019-11-10 周日 上午 01:22:18
* @Description 
* @version 
*/
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreAuthorize {
	/**
	 * 进入此方法所需的权限
	 * @return
	 */
	String[] value();
	/**
	 * 此方法映射的uri，必须绝对路径
	 * @return
	 */
	String url();
}
