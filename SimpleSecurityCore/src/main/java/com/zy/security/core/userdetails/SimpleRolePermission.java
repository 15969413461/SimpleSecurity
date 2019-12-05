
package com.zy.security.core.userdetails;

/**
* @author zy
* @Date 2019-11-13 周三 下午 11:53:06
* @Description 基本的权限字符串格式，如：Role_edit
* @version 
*/
public class SimpleRolePermission implements RolePermission {
	private String role;
	private String prefix = "Role_";
	
	// 是否区分大小写,设置为true则代表不区分，在解析时将会把权限字符串转为小写,默认为false
	private boolean case_sensit;
	
	public SimpleRolePermission(String role) {
		if(role == "") {
			throw new IllegalArgumentException("权限不可可空串");
		}
		if(role.indexOf(":") != -1) {
			throw new IllegalArgumentException("不支持的权限字符串，by： "+role);
		}
		this.role = checkRole(role);
	}
	
	public SimpleRolePermission(String role, boolean case_sensit) {
		super();
		this.role = role;
		this.case_sensit = case_sensit;
	}

	public SimpleRolePermission(String role, String prefix,boolean case_sensit) {
		super();
		this.role = role;
		this.prefix = prefix;
		this.case_sensit = case_sensit;
	}


	@Override
	public boolean compare(RolePermission Role) {
		return equals(Role);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof SimpleRolePermission) {
			return role.equals(((SimpleRolePermission) obj).getRole());
		}
		return false;
	}
	@Override
	public int hashCode() {
		return this.role.hashCode();
	}
	
	/**
	 * 对获得的权限字符串进行检查，若带有配置的前缀则截取前缀之后的内容，否则静默返回原字符串
	 * @param role
	 * @return
	 */
	public String checkRole(String role){
		if(role.indexOf(this.prefix) != -1) {
			int len = this.prefix.length();
			return role.substring(len);
		}
		return role;
	}
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public boolean isCase_sensit() {
		return case_sensit;
	}
	public void setCase_sensit(boolean case_sensit) {
		this.case_sensit = case_sensit;
	}
}
