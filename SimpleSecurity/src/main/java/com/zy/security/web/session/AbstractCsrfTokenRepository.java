
package com.zy.security.web.session;

import java.util.UUID;

import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;

/**
 * @author zy
 * @Date 2019-11-18 周一 下午 15:41:42
 * @Description
 * @version
 */
public abstract class AbstractCsrfTokenRepository implements CsrfTokenRepository{
	/** 
	 * 针对于post请求，表单提交时此值为csrf字符串的key，存储于请求参数 - ["_csrf"] 
	 */
	protected String parameterName;
	/** 
	 * 对于GET请求，发送请求时此值为csrf字符串的key，存储于请求头 - ["XSRF-TOKEN"]<br/>
	 */
	protected String headerName;
	/**
	 * 对于Cookie，此值为Cookie name 
	 */
	protected String cookieName;
	
	/**
	 * 依据此值判断session中是否关联了CsrfToken 
	 */
	protected String attributeName = this.getClass().getName();
	
	public String getHeaderName() {
		return headerName;
	}
	public String getCookieName() {
		return cookieName;
	}
	public String getAttributeName() {
		return attributeName;
	}
	@Override
	public void setParameName(String name) {
		if(name == null || name.isEmpty()) {
			return ;
		}
		this.parameterName = name;
	}
	@Override
	public void setHeaderName(String name) {
		if(name == null || name.isEmpty()) {
			return ;
		}
		this.headerName = name;
	}
	
	
	public AbstractCsrfTokenRepository() {
		super();
	}
	/**
	 * 针对于SessionCsrfTokenRepository的构造器
	 * @param parameterName
	 * @param headerName
	 * @param attributeName
	 */
	public AbstractCsrfTokenRepository(String parameterName, String headerName) {
		if(parameterName == null || headerName == null || parameterName.isEmpty() || headerName.isEmpty() ) {
			throw new IllegalArgumentException("构造参数不可为null或空串");
		}
		this.parameterName = parameterName;
		this.headerName = headerName;
	}
	/**
	 * 针对于CookieCsrfTokenRepository的构造器
	 * @param cookieName
	 * @param attributeName
	 */
	public AbstractCsrfTokenRepository(String cookieName) {
		if(cookieName == null || cookieName.isEmpty()) {
			throw new IllegalArgumentException("构造参数不可为null或空串");
		}
		this.cookieName = cookieName;
	}
	
	
	@Override
	public CsrfToken generateToken() {
		return new DefaultCsrfToken(createNewToken(), this.parameterName, this.headerName);
	}
	
	/**
	 * 随机创建一个csrf字符串
	 * @return
	 */
	protected String createNewToken() {
		return UUID.randomUUID().toString();
	}

	@Override
	public void setCookieName(String name) {
		if(name == null || name.isEmpty()) {
			return ;
		}
		this.cookieName = name;
	}
}
