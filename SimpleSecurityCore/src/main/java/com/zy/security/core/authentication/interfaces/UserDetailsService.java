
package com.zy.security.core.authentication.interfaces;
/**
* @author zy
* @Date 2019-11-15 周五 下午 16:40:10
* @Description 从外部加载用户验证所需数据
* @version 
*/
public interface UserDetailsService {

	/**
	 * 根据用户名定位用户
	 * @param username
	 * @return
	 */
	UserDetails loadUserByUsername(String username);

}
