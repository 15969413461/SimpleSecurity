
package com.zy.security.web.session;

import com.zy.security.web.session.interfaces.CsrfToken;

/**
* @author zy
* @Date 2019-11-18 周一 下午 15:30:07
* @Description Csrf凭证poji类
* @version 
*/
@SuppressWarnings("serial")
public class DefaultCsrfToken implements CsrfToken {
	/** csrf字符串 */
	private final String token;
	/** form表单中的csrf字符串key  */
	private final String parameterName;
	/** 请求头中的csrf字符串key */
	private final String headerName;
	private int count;
	
	public DefaultCsrfToken(String token, String parameterName, String headerName) {
		if(token == null || parameterName == null || headerName == null
				|| token.isEmpty() || parameterName.isEmpty() || headerName.isEmpty() ) {
			throw new IllegalArgumentException("构造参数不可为null或空串");
		}
		this.token = token;
		this.parameterName = parameterName;
		this.headerName = headerName;
	}

	@Override
	public String getParameterName() {
		return this.parameterName;
	}
	@Override
	public String getHeaderName() {
		return this.headerName;
	}
	@Override
	public String getToken() {
		return this.token;
	}

	@Override
	public String getCookieName() {
		return this.headerName;
	}

	@Override
	public int getCount() {
		return this.count;
	}
	
	
}
