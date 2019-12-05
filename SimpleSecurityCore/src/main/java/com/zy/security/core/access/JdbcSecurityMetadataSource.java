
package com.zy.security.core.access;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.zy.security.core.access.interfaces.SecurityMetadataSource;
import com.zy.security.core.userdetails.PluralisticRolePermission;
import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-16 周六 下午 14:27:36
* @Description - 获取权限集合操作，根据主体的不同而获取不同的数据，此类使用jdbc从数据可中获得权限集合
* @version 
*/
public class JdbcSecurityMetadataSource implements SecurityMetadataSource {
	/**
	 * 是否使用 PluralisticRolePermission 实现类访问权限数据.
	 * 若为false则使用SimpleRolePermission实现类，那么权限需级别相等才会被允许访问
	 */
	private boolean isPluralistic = true;
	
	
	public boolean isPluralistic() {
		return isPluralistic;
	}
	
	public JdbcSecurityMetadataSource() {}
	public JdbcSecurityMetadataSource(boolean isPluralistic) {
		super();
		this.isPluralistic = isPluralistic;
	}


	@Override
	public Collection<RolePermission> getAttributes(Object principal) throws IllegalArgumentException {
		List<RolePermission> list = new ArrayList<>();
		
		if(principal.equals("user")) {
			list.add( new PluralisticRolePermission("user:edit,add") );
		}
		return list;
	}

	@Override
	public Collection<RolePermission> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return false;
	}

}
