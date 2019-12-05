
package com.zy.security.core.authentication;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.exception.BadCredentialsException;
import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;

/**
* @author zy
* @Date 2019-11-15 周五 下午 16:37:35
* @Description 密码匹配逻辑和从外部获得UserDetails信息 
* @version 
*/
public class DaoAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		String userDetailsPwd = userDetails.getPassword();
		String authPWd = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(authPWd, userDetailsPwd)) { // 凭证匹配
            throw new BadCredentialsException("凭证错误，by："+userDetails.getUsername());
        }
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		return this.userDetailsService.loadUserByUsername(username);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}
}
