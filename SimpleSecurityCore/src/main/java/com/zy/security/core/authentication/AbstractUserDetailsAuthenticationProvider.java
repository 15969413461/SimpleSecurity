
package com.zy.security.core.authentication;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.interfaces.AuthenticationProvider;
import com.zy.security.core.authentication.interfaces.PasswordEncoder;
import com.zy.security.core.authentication.interfaces.UserCache;
import com.zy.security.core.authentication.interfaces.UserDetails;
import com.zy.security.core.authentication.interfaces.UserDetailsChecker;
import com.zy.security.core.authentication.interfaces.UserDetailsService;
import com.zy.security.core.token.Authentication;
import com.zy.security.core.token.UsernamePasswordAuthenticationToken;
import com.zy.security.core.userdetails.exception.AccountStatusException;

/**
* @author zy
* @Date 2019-11-13 周三 下午 11:23:54
* @Description 抽象的用户（非匿名、RememberMe用户）身份认证处理类.<br/>
* 自定义用户身份验证逻辑建议实现此抽象类,使用父类提供的 {@link PasswordEncoder} 和 {@link UserDetailsService} 引用即可，框架在启动时会自动设置其具体实现类
* @version 
*/
public abstract class AbstractUserDetailsAuthenticationProvider implements AuthenticationProvider {
	
	private UserCache userCache;
	private UserDetailsChecker checker;
	
	protected PasswordEncoder passwordEncoder;
	protected UserDetailsService userDetailsService;
	
	public UserCache getUserCache() {
		return userCache;
	}
	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}
	public UserDetailsChecker getChecker() {
		return checker;
	}
	public void setChecker(UserDetailsChecker checker) {
		this.checker = checker;
	}
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}
	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
	
	/**
	 * 默认构造器
	 * @param passwordEncoder
	 * @param userDetailsService
	 */
	public AbstractUserDetailsAuthenticationProvider() {}
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if ( !this.supports(authentication.getClass()) ) {
			return null;
		}
		// 用户主体标识
		String principal =  authentication.getPrincipal().toString();
		
		UserDetails userDetails = this.userCache.getUserFromCache(principal);
		boolean isCache = true;
		UsernamePasswordAuthenticationToken userToken = ((UsernamePasswordAuthenticationToken) authentication);
		
		if(userDetails == null) { // 缓存中无对应的UserDetails信息
			isCache = false;
			try {
				userDetails = retrieveUser(principal, userToken);
			} catch (AuthenticationException e) {
				e.printStackTrace();
			}
		}
		
		try {
			// 验证此UserDetails对象的有效性
			this.checker.preCheck(userDetails);
			// 进行密码验证
			additionalAuthenticationChecks(userDetails, userToken);
		} catch (AccountStatusException e) {
			if(isCache) { // 从缓存中获得UserDetails对象无效或密码错误
				// 执行以下对应方法时，若发生异常则对外抛出
				userDetails = retrieveUser(principal, userToken);
				this.checker.preCheck(userDetails);
				additionalAuthenticationChecks(userDetails, userToken);
			}else {
				throw e;
			}
		}
		if(isCache) {// 更新缓存中的UserDetails信息
			this.userCache.putUserInCache(userDetails);
		}
		return	new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
				userDetails.getPassword(),authentication.getAuthorities());
	}

	/**
	 * 密码验证逻辑，若验证成功则静默返回，若验证失败则抛出对应的AuthenticationException子类异常
	 * @param userDetails
	 * @param authentication
	 * @throws AuthenticationException
	 */
	protected abstract void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)	throws AuthenticationException;
	
	/**
	 * 根据用户名和Token获得 UserDetails 对象
	 * @param username
	 * @param authentication
	 * @return
	 * @throws AuthenticationException
	 */
	protected abstract UserDetails retrieveUser(String username,UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException;
	
	/* 
	 * 由其实现类控制此方法
	 */
	public abstract boolean supports(Class<?> authentication);
}
