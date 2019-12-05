
package com.zy.security.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(ElementType.TYPE)
/**
* @author zy
* @Date 2019-12-01 周日 上午 00:49:41
* @Description 标注于配置类
* @version 
*/
public @interface EnableSecurityConfiguration {
	
}
