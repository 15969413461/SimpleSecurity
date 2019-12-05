
package com.zy.security.core.userdetails;
/**
* @author zy
* @Date 2019-11-13 周三 下午 23:09:09
* @Description 封装权限字符串数据,后检查传递的权限字符串是否包含实现类对象封装的权限数据
* @version 
*/
public interface RolePermission {
	/**
	 * 对传递RolePermission与方法调用者作比较
	 * @param Role - 需要验证的权限,因为存在字符串通配符的关系，所以需要验证的权限应大于或等于作为对照的权限级别
	 * @return
	 */
	boolean compare(RolePermission Role);
}
