
package com.zy.security.core.authentication.interfaces;

/**
* @author zy
* @Date 2019-11-13 周三 下午 11:02:55
* @Description 封装Token缓存的读取、写入、删除操作
* @version 
*/
public interface UserCache {
	/**
	 * 从缓存中获得用户验证信息
	 * @param username
	 * @return
	 */
	UserDetails getUserFromCache(String username);
	/**
	 * 将封装用户认证信息的UserDetails接口对象保存到缓存中
	 * @param user
	 * @return
	 */
	boolean putUserInCache(UserDetails user);
	/**
	 * 从缓存中删除保存的UserDetails信息
	 * @param username
	 */
	boolean removeUserFromCache(String username);
}
