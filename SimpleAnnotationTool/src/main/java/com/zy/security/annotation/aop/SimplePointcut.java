
package com.zy.security.annotation.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author zy
* @Date 2019-11-10 周日 上午 01:22:31
* @Description 标记切点方法,与@Pointcut作用类似,但只可标记于最终的子类上
* @version 
*/
@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SimplePointcut {
	/**
	 * 值定义与spring中切点或切面定义表达式相同（暂不支持）或使用简单格式定义。<br/>
	 * 默认可不指定,此种情况下则只代表标注的方法为切点
	 * @see com.zy.aop.handler.SimplePointCutAnalysisStrategy
	 */
	String value() default "";
}
