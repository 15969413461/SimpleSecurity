
package com.zy.security.core.authentication.interfaces;

import java.io.Serializable;
import java.util.Collection;

import com.zy.security.core.userdetails.RolePermission;

/**
* @author zy
* @Date 2019-11-13 周三 下午 11:03:23
* @Description 封装从外部加载的用户认证数据所有相关状态（账户是否锁定、密码是否过期等）
* @version 
*/
public interface UserDetails extends Serializable {
	String getPassword();
	String getUsername();
	
	/**
	 * 获得权限集合
	 * @return
	 */
	Collection<? extends RolePermission> getAuthorities();
	 /**
     * 帐户是否过期<br/>
     * 如果用户帐户有效（即未过期），则为true；如果不再有效（即过期），则为false
     * @return
     */
    boolean isAccountNonExpired() ;

    /**
     * 帐户是否被冻结<br/>
     * 如果用户未被锁定（冻结），则为true，否则为false
     * @return
     */
    public boolean isAccountNonLocked() ;

    /**
     * 帐户密码是否过期，一般有的密码要求性高的系统会使用到，每隔一段时间就要求用户重置密码<br/>
     * 如果用户的凭据有效（即未过期），则为true；如果不再有效（即已过期），则为false
     * @return
     */
    public boolean isCredentialsNonExpired() ;

    /**
     * 帐号是否可用<br/>
     * 如果启用了用户，则为true；否则为false
     * @return
     */
    public boolean isEnabled() ;
}
