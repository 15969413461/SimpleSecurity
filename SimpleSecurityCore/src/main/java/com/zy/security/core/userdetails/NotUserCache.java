
package com.zy.security.core.userdetails;

import com.zy.security.core.authentication.interfaces.UserCache;
import com.zy.security.core.authentication.interfaces.UserDetails;

/**
* @author zy
* @Date 2019-11-14 周四 上午 12:18:13
* @Description
* @version 
*/
public class NotUserCache implements UserCache {

	@Override
	public UserDetails getUserFromCache(String username) {
		return null;
	}

	@Override
	public boolean putUserInCache(UserDetails user) {
		return true;
	}

	@Override
	public boolean removeUserFromCache(String username) {
		return true;
	}

}
