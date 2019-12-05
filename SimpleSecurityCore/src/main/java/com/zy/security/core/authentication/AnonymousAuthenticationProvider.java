
package com.zy.security.core.authentication;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.exception.BadCredentialsException;
import com.zy.security.core.authentication.interfaces.AuthenticationProvider;
import com.zy.security.core.token.AnonymousAuthenticationToken;
import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-15 周五 下午 14:35:45
* @Description 对AnonymousAuthenticationToken的验证
* @version 
*/
public class AnonymousAuthenticationProvider implements AuthenticationProvider {
	// 标识匿名对象key的hash值
	private final Integer keyHash;
	
	public AnonymousAuthenticationProvider(String key) {
		if(key ==null || key == "") {
			throw new IllegalArgumentException("构造器参数不可为null或空串");
		}
		this.keyHash = key.hashCode();
	}

	public Integer getKey() {
		return keyHash;
	}

	/** 
	 * 若key相同则验证成功否则抛出异常
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException{
		if (!supports(authentication.getClass())) {
			return null;
		}
		if(this.keyHash != ((AnonymousAuthenticationToken) authentication).getKey()) {
			// 不是原本关联的匿名Token或已被篡改
			throw new BadCredentialsException();
		}
		return authentication; // 是原本关联的匿名Token
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (AnonymousAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
