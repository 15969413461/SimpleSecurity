
package com.zy.security.core.token;

import java.util.Date;

/**
* @author zy
* @Date 2019-11-25 周一 下午 13:36:29
* @Description 持久化令牌凭证
* @version 
*/
public class PersistentRememberMeToken {
	private final String username;
	private final String series;
	private final String tokenValue;
	private final Date date;

	public PersistentRememberMeToken(String username, String series, String tokenValue,
			Date date) {
		this.username = username;
		this.series = series;
		this.tokenValue = tokenValue;
		this.date = date;
	}

	public String getUsername() {
		return username;
	}

	public String getSeries() {
		return series;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public Date getDate() {
		return date;
	}
}
