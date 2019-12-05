
package com.zy.security.core.authentication;

import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.authentication.interfaces.UserDetailsChecker;
import com.zy.security.core.userdetails.exception.AccountExpiredException;
import com.zy.security.core.userdetails.exception.AccountStatusException;
import com.zy.security.core.userdetails.exception.CredentialsExpiredException;
import com.zy.security.core.userdetails.exception.DisabledException;
import com.zy.security.core.userdetails.exception.LockedException;

/**
* @author zy
* @Date 2019-11-13 周三 下午 11:31:07
* @Description UserDetails对象状态检查
* @version 
*/
public class DefaultUserDetailsChecker implements UserDetailsChecker {

	@Override
	public void preCheck(UserDetails userDetails) throws AccountStatusException {
		if(userDetails == null) {
			throw new AccountStatusException("未找到有效的用户信息");
		}
		if(!userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException("账户已过期,by: "+userDetails.getUsername());
		}
		if(!userDetails.isAccountNonLocked()) {
			throw new  LockedException("账户已被冻结,by: "+userDetails.getUsername());
		}
		if(!userDetails.isEnabled()) {
			throw new DisabledException("账户不可用,by: "+userDetails.getUsername());
		}
	}
	@Override
	public void postCheck(UserDetails userDetails) throws AccountStatusException {
		if(!userDetails.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException("密码已过期,by: "+ userDetails.getUsername().toString());
		}
	}

}
