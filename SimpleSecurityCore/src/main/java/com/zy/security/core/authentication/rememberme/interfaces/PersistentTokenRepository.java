
package com.zy.security.core.authentication.rememberme.interfaces;

import java.util.Date;

import com.zy.security.core.token.PersistentRememberMeToken;

/**
* @author zy
* @Date 2019-11-25 周一 下午 13:29:00
* @Description 持久令牌存储库
* @version 
*/
public interface PersistentTokenRepository {
	
	void createNewToken(PersistentRememberMeToken token);

	void updateToken(String series, String tokenValue, Date lastUsed);

	PersistentRememberMeToken getTokenForSeries(String seriesId);

	void removeUserTokens(String username);
}
