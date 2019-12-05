
package com.zy.security.web.config;

/**
 * @author zy
 * @Date 2019-11-25 周一 上午 00:50:43
 * @Description 此抽象类属性由各个实现类控制其默认值与结果值
 * @version
 * ForLoginConfiguration
 */
public abstract class AbstractHttpConfigurer<H> {
	H http;

	/**
	 * 构造器
	 * @param securityBuilder
	 */
	public AbstractHttpConfigurer(H securityBuilder) {
		this.http = securityBuilder;
	}
	
	/**
	 * 调用各个子实现类相关属性装配到参数http中
	 * @param http
	 */
	public abstract void config();
	
	public abstract H and();
}
