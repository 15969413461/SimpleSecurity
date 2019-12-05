
package com.zy.security.web.config.subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.zy.security.core.token.Authentication;
import com.zy.security.core.userdetails.PluralisticRolePermission;
import com.zy.security.core.userdetails.RolePermission;
import com.zy.security.core.userdetails.SimpleRolePermission;
import com.zy.security.web.config.AnonymousConfiguration;
import com.zy.security.web.config.AuthorizationConfiguration;
import com.zy.security.web.util.Authorize;

/**
 * @author zy
 * @Date 2019-12-01 周日 下午 16:04:02
 * @Description 每一个Authorization都代表着与之所关联的uri授权验证机制。<br/>
 * 通过引用的 Authorize静态变量或设置的权限字符串匹配规则进行区分.<br/>
 * <p>
 * 大体上分为用户身份验证和用户所拥有的权限验证.<br/>
 * 身份验证只需对认证存储对象（{@link Authentication}实现类）进行类型比较即可得到验证结果.<br/>
 * 而权限验证需同从外部加载的权限数据进行比较 ，具体的验证逻辑分为"与"（权限缺一不可）,"或"（得其一者得天下）,"非"（得其一者失天下）三种逻辑。
 * <p/>
 * @version
 */
public final class Authorization {
	AuthorizationConfiguration authConfig;
	
	private String authorizeAttr;
	
	//------------------------------权限验证所需引用数据---------------------------------------
	/**
	 *  存储权限字符串,缓冲区（用户自定义配置的各项目优先级不固定，可能加载AuthorizationConfiguration之前还未加载过AnonymousConfiguration）
	 *  为使用户的RolePermission配置落实到每一个RolePermission实例上，所以此处定义的权限数据先保存与此容器
	 */
	private List<String> list = new ArrayList<>();
	/**
	 *  存储权限字符串，作为之后权限认证的依据,在configuration经过排序之后将缓冲区中的数据写入到此容器
	 */
	private List<RolePermission> Rolelist = new ArrayList<>();
	
	
	// 通过此值区分权限验证的类型 0-与，1-或，2-非
	private int tag;
	
	//-------------------------getter、setter-------------------------------------------
	public AuthorizationConfiguration getAuthConfig() {
		return authConfig;
	}
	public void setAuthConfig(AuthorizationConfiguration authConfig) {
		this.authConfig = authConfig;
	}
	public List<RolePermission> getRolelist() {
		return Rolelist;
	}
	public void setRolelist(List<RolePermission> rolelist) {
		Rolelist = rolelist;
	}
	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public void setAuthorizeAttr(String authorizeAttr) {
		this.authorizeAttr = authorizeAttr;
	}
	public String getAuthorizeAttr() {
		return authorizeAttr;
	}
	
	/**
	 * 构造器
	 * @param authorizationConfiguration
	 */
	public Authorization(AuthorizationConfiguration authorizationConfiguration) {
		super();
		this.authConfig = authorizationConfiguration;
	}
	
	/**
	 * 设置不作限制的uri
	 * @return
	 */
	public AuthorizationConfiguration permitAll(){
		this.authorizeAttr = Authorize.permitAll;
		return authConfig;
	}
	
	/**
	 * 设置仅匿名用户允许使用的uri
	 * @return
	 */
	public AuthorizationConfiguration anonymous(){
		this.authorizeAttr = Authorize.anonymous;
		return authConfig;
	}
	
	/**
	 * 设置仅经过身份验证的用户允许使用的uri
	 * @return
	 */
	public AuthorizationConfiguration authenticated(){
		this.authorizeAttr = Authorize.authenticated;
		return authConfig;
	}
	
	/**
	 * 设置已记住的用户允许使用的uri
	 * @return
	 */
	public AuthorizationConfiguration rememberMe(){
		this.authorizeAttr = Authorize.rememberMe;
		return authConfig;
	}
	
	/**
	 * 设置任何人都不允许使用 的uri
	 * @return
	 */
	public AuthorizationConfiguration denyAll(){
		this.authorizeAttr = Authorize.denyAll;
		return authConfig;
	}
	
	/**
	 * 设置已通过身份验证且未被“记住”的用户允许使用的uri
	 * @return
	 */
	public AuthorizationConfiguration fullyAuthenticated(){
		this.authorizeAttr = Authorize.fullyAuthenticated;
		return authConfig;
	}
	
	/**
	 * 指定访问此URL需要的所有权限,此处的权限模式为“与”
	 * @return
	 */
	public AuthorizationConfiguration hasAnyRole(String ... rules){
		this.authorizeAttr = Authorize.role;
		this.tag = 0;
		this.list = Arrays.asList(rules);
		return authConfig;
	}
	/**
	 * 指定访问此URL需要的任意权限,此处的权限模式为“或”
	 * @param rules
	 * @return
	 */
	public AuthorizationConfiguration hasOrRole(String ... rules){
		this.authorizeAttr = Authorize.role;
		this.tag = 1;
		this.list = Arrays.asList(rules);
		return authConfig;
	}
	/**
	 * 指定访问此URL不能拥有的权限,此处的权限模式为“非”
	 * @param rules
	 * @return
	 */
	public AuthorizationConfiguration hasNotRole(String ... rules){
		this.authorizeAttr = Authorize.role;
		this.tag = 2;
		this.list = Arrays.asList(rules);
		return authConfig;
	}
	
	/**
	 * 将权限字符串转化为 {@link RolePermission}实例
	 * <p>在具体的请求需要权限认证时被调用 <p/>
	 * @return - 存储转化 {@link RolePermission} 实例的ArrayList容器
	 */
	public List<RolePermission> writeRolePermission() {
		if(!this.Rolelist.isEmpty()) { // 已转化
			return this.Rolelist;
		}
		
		for (String role : list) {
			if(AnonymousConfiguration.isPluralistic) {
				this.Rolelist.add(new PluralisticRolePermission(role, AnonymousConfiguration.case_sensitiver));
			}else {
				this.Rolelist.add(new SimpleRolePermission(role, AnonymousConfiguration.case_sensitiver));
			}
		}
		return this.Rolelist;	
	}
	@Override
	public String toString() {
		return "Authorization [authorizeAttr=" + authorizeAttr + ", list=" + list + ", Rolelist=" + Rolelist + ", tag="
				+ tag + "]";
	}
}
