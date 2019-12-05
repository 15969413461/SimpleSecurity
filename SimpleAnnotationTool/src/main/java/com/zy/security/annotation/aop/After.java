
package com.zy.security.annotation.aop;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(METHOD)
/**
* @author zy
* @Date 2019-11-10 周日 下午 03:08:47
* @Description
* @version 
*/
public @interface After {
	/**
	 * 切点方法调用名
	 * 使用此值将切点与环绕方法绑定,(根据此类名查找Class对象) <br/>
	 */
	String value() default "";
}
