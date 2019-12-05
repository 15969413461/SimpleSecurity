
package com.zy.security.core.access.interfaces;

import java.util.Collection;

import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-16 周六 下午 14:13:42
* @Description 权限集合获取操作的抽象接口
* @version 
*/
public interface SecurityMetadataSource {
	/**
	 * 从外部数据源中获得参数对象所拥有的权限数据
	 * @param principal - 用户主体标识，一般为用户名
	 * @return
	 * @throws IllegalArgumentException
	 */
	Collection<RolePermission> getAttributes(Object principal) throws IllegalArgumentException;
	
	/**
	 * 获得所有的权限数据
	 * @return
	 */
	Collection<RolePermission> getAllConfigAttributes() ;
	
	/**
	 * 判别此权限封装类对象的数据格式是否能被本类所支持
	 * @param clazz - RolePermission实现类
	 * @return
	 */
	boolean supports(Class<?> clazz);
}
