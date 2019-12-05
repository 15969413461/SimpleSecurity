
package com.zy.security.web.config.subject;

import java.util.List;

import javax.servlet.Filter;

import com.zy.security.web.config.AnonymousConfiguration;
import com.zy.security.web.config.AuthorizationConfiguration;
import com.zy.security.web.config.CsrfConfiguration;
import com.zy.security.web.config.ForLoginConfiguration;
import com.zy.security.web.config.LogoutConfiguration;
import com.zy.security.web.config.RememberMeConfiguration;
import com.zy.security.web.config.SessionManagerConfiguration;

/**
* @author zy
* @Date 2019-11-27 周三 上午 00:55:27
* @Description
* @version 
 * @param <H>
* 
* @param
* 
*/
public interface AbstractHttpSecurity {
	/**
	 * 配置个性化登录属性
	 */
	ForLoginConfiguration formLogin();
	
	/**
	 * 配置用户退出操作
	 * @return
	 */
	LogoutConfiguration logout(); 
	
	/**
	 * 配置匿名用户主体标识与拥有权限
	 * @return
	 */
	AnonymousConfiguration anonymous();
	
	/**
	 * 配置RememberMe服务
	 * @return
	 */
	RememberMeConfiguration rememberMe();
	
	/**
	 * 配置csrf服务
	 * @return
	 */
	CsrfConfiguration csrf();
	
	/**
	 * 配置session属性与策略
	 * @return
	 */
	SessionManagerConfiguration SessionManager();

	/**
	 * 配置请求级别的权限限制，配置之外的请求则不对其权限作限制。默认对所有的请求不作权限限制
	 * <p>配置的结果是：此处配置结果集与注解配置结果集的并集，且此处配置类结果值优先级最高<p/>
	 * @return
	 */
	AuthorizationConfiguration authorizeRequests();
	
	/**
	 * 通过各个配置存储类类对象创建Filter
	 * @return
	 */
	List<Filter> config();
	
}
