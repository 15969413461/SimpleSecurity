
package com.zy.security.core.authentication;


import java.util.List;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.exception.AuthenticationException;
import com.zy.security.core.authentication.exception.AuthenticationProviderNotFoundException;
import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.core.authentication.interfaces.AuthenticationProvider;
import com.zy.security.core.authentication.interfaces.EraseCredentials;
import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-13 周三 下午 09:46:52
* @Description 封装使用认证器的逻辑
* @version 
*/
public class ProviderManager implements AuthenticationManager {
//	private static Logger logger = LoggerFactory.getLogger(ProviderManager.class);
	
	// 验证器集合
	private List<AuthenticationProvider> authProviderLst;
	// 对其他AuthenticationManager实现类的引用
	private AuthenticationManager parent;
	// 是否清除已认证Token的凭证信息（如：密码、验证码等）,默认为true-需清除密码信息
	private boolean eraseCredentialsAfterAuthentication;
	
	
	public List<AuthenticationProvider> getAuthProviderLst() {
		return authProviderLst;
	}
	public void setAuthProviderLst(List<AuthenticationProvider> authProviderLst) {
		this.authProviderLst = authProviderLst;
	}
	public AuthenticationManager getParent() {
		return parent;
	}
	public void setParent(AuthenticationManager parent) {
		this.parent = parent;
	}
	public boolean isEraseCredentialsAfterAuthentication() {
		return eraseCredentialsAfterAuthentication;
	}
	public void setEraseCredentialsAfterAuthentication(boolean eraseCredentialsAfterAuthentication) {
		this.eraseCredentialsAfterAuthentication = eraseCredentialsAfterAuthentication;
	}
	
	
	/**
	 * 创建一个空壳对象，属性之后进行补充
	 */
	public ProviderManager() {
		super();
	}
	/**
	 * 构造方法，保证每个ProviderManager都持有有效的验证器
	 * @param authProviderLst
	 * @param parent
	 * @param eraseCredentialsAfterAuthentication
	 * @throws AuthenticationProviderNotFoundException
	 */
	public ProviderManager(List<AuthenticationProvider> authProviderLst, AuthenticationManager parent) 
			throws AuthenticationProviderNotFoundException {
		if(authProviderLst == null) {
			throw new AuthenticationProviderNotFoundException("无可调用的验证器(AuthenticationProvider)");
		}
		this.authProviderLst = authProviderLst;
		this.parent = parent;
	}
	
	/** 
	 * 具体的认证逻辑实现
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Class<? extends Authentication> auth = authentication.getClass();
		// 最终返回给上层方法的Token对象
		Authentication authenticate = null;
		/*
		 * 迭代验证器进行验证，若验证通过则将for循环终止，返回Token对象到上层调用者
		 * 若未通过则认证失败，抛出对应的异常
		 */
		for (AuthenticationProvider authenticationProvider : this.authProviderLst) {
			if( !authenticationProvider.supports(auth) ) { // 当前验证器无法验证此Token
				continue;
			}
			authenticate = authenticationProvider.authenticate(authentication);
			if(authenticate != null) { // 当前验证器已验证成功
				setTokenDetails(authentication, authenticate);
				break;
			}
		}
		// 本对象的验证器无法验证此Token对象，使用其他AuthenticationManager实现类的验证器
		if(authenticate == null ) {
			try {
				authenticate = this.parent.authenticate(authentication);
			} catch (NullPointerException e) { // 再无可使用的验证器,或对象内部属性为null
				throw new AuthenticationException();
			}
		}
		// 进行密码清除
		if(authenticate != null && eraseCredentialsAfterAuthentication
				&& authenticate instanceof EraseCredentials) {
			((EraseCredentials) authenticate).eraseCredentials();
		}
		return authenticate;
	}
	
	/**
	 * 将需认证Token中的其他详细信息（IP、证书序列号）填充到已认证的Token
	 * @param authentication
	 * @param returnAuthen
	 */
	private void setTokenDetails(Authentication authentication,Authentication returnAuthen){
		returnAuthen.setDetails(authentication.getDetails());
	}
}
