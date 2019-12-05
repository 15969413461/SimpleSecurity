
package com.zy.security.core.token;

import java.io.Serializable;
import java.util.Collection;

import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-13 周三 下午 09:33:43
* @Description 令牌接口,封装了用户认证状态与认证的必要数据（用户名-principal、密码等）
* @version 
*/
public interface Authentication extends Serializable{
	/**
	 * 是否已经过验证
	 * @return
	 */
	boolean isAuthenticated();
	
	/**
	 * 获得用户主体标识，如用户名，主要用于授权时获得用户标识
	 * @return
	 */
	Object getPrincipal();
	
	/**
	 * 获得有关身份验证请求的其他详细信息，比如：IP、证书序列号
	 * @return
	 */
	Object getDetails();
	/**
	 * 设置有关身份验证请求的其他详细信息，比如：IP、证书序列号
	 * @return
	 */
	Object setDetails(Object details);
	
	/**
	 * 获得用户的权限集合
	 * @return
	 */
	Collection<? extends RolePermission> getAuthorities();
}
