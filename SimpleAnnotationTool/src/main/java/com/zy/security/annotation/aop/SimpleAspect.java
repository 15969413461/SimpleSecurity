
package com.zy.security.annotation.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author zy
* @Date 2019-11-10 周日 上午 01:22:31
* @Description - 标记AOP逻辑方法类,与@Aspect作用类似,但只可标记于最终的子类上
* @version 
*/
@Documented
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SimpleAspect {
}
