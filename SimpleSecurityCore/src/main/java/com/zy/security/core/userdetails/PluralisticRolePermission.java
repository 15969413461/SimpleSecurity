
package com.zy.security.core.userdetails;

import java.util.ArrayList;
import java.util.List;

/**
* @author zy
* @Date 2019-11-14 周四 上午 00:05:40
* @Description 对多元化的权限字符串进行封装。<br/>
* <p>
*  1. 多层次(AA:aa) - 第一个令牌是正在操作的域，第二个令牌是正在执行的操作。<br/>
*  可以简单地授予用户“ newsletter: view ,edit ,create”权限，使他们可以在newsletter域中执行查看、编辑和创建操作。<br/>
*  然后，您可以通过"newsletter: create"进行验证，结果为true。<br/>
*  <p/>
*  <p>
*  2. 实例级访问控制（AA:aa:1,2,3），通配符权限的另一个常见用法是对实例级访问控制列表建模。<br/>
*  在这种情况下，您使用三个令牌-第一个令牌是域，第二个令牌是操作，第三个令牌是您正在操作的实例。<br/>
*  比如：<br/>
*  （1）. “newsletter: edit: 12 ,13 ,18”，假设第三个令牌是新闻稿的系统ID。这将允许用户编辑新闻稿12、13和18。<br/>
*  （2）. "newsletter: *: 13" - 授予用户新闻稿13的所有操作 <br/>
*  （3）. "newsletter: view、create、edit: *" - 允许用户查看、创建或编辑任何新闻稿 <br/>
*  （4）. "newsletter： *" - 允许用户对任何新闻稿执行任何操作 <br/>
*  <em>其中“：”分割权限层次，“*”代表所有。<em/>
*  <p>
* @version 
* @see org.apache.shiro.authz.permission.WildcardPermission
*/
public class PluralisticRolePermission implements RolePermission {
//	private static Logger logger = LoggerFactory.getLogger(PluralisticRolePermission.class);
	
	// 令牌通配符
	private static String WILDCARD_TOKEN = "*";
	// 令牌分隔符
	private static String DIVIDER_TOKEN = ":";
	// 子令牌分隔符
	private static String DIVIDER_CHILD_TOKEN = ",";
	// 是否区分大小写,设置为true则代表不区分，在解析时将会把权限字符串转为小写
	private boolean CASE_SENSITIVE;
	
	// 第一层令牌
	private List<String> realm;
	// 第二层令牌
	private List<String> operation;
	// 第三层令牌
	private List<String> example;
	
	public PluralisticRolePermission(String rootRole) {
		this(rootRole,false);
		
	}
	public PluralisticRolePermission(String rootRole,boolean case_sensitiver) {
		if(rootRole.indexOf("_") != -1) {
			throw new IllegalArgumentException("不支持的权限字符串，by： "+rootRole);
		}
		this.CASE_SENSITIVE = case_sensitiver;
		this.realm = new ArrayList<>();
		this.operation = new ArrayList<>();
		this.example = new ArrayList<>();
		analysis(rootRole);
	}
	
	/**
	 * 解析权限字符串，将结果存储于成员变量之中
	 * @param rootRole
	 * @return 
	 */
	public void analysis(String rootRole){
		if(rootRole == "") {
			throw new IllegalArgumentException("权限不可可空串,by: "+rootRole);
		}
		if(rootRole.indexOf("\\:") != -1) {
			throw new IllegalArgumentException("不适合处理此权限,by: "+rootRole);
		}
		
		if(CASE_SENSITIVE) { // 
			rootRole = rootRole.trim().toLowerCase();
//			logger.info(rootRole);
		}
		
		String[] split = rootRole.split(DIVIDER_TOKEN);
		int len = split.length;
		for (int i = 0; i < len; i++) {
			String[] split2 = split[i].split(DIVIDER_CHILD_TOKEN);
			for (int j = 0; j < split2.length; j++) {
				switch (i) {
					case 0:		realm.add(split2[j]);     break; // 第一层令牌
					case 1:		operation.add(split2[j]); break; // 第二层令牌
					case 2:		example.add(split2[j]);          // 第三层令牌
				}
			}
		}
	}
	
	@Override
	public boolean compare(RolePermission role) {
		PluralisticRolePermission RolePermission = (PluralisticRolePermission) role;
		// 验证结果的引用
		boolean contains = false;
		
		List<String> realmz = RolePermission.getRealm();
		for (String str : realmz) { // 第一层令牌比较
			contains = this.realm.contains(str);
			if(!contains) { // 若结果为falsr则直接返回结果
				return contains;
			}
		}
		
		List<String> operationz = RolePermission.getOperation();
		for (String stri : operationz) { // 第二层令牌比较
			if(stri.equals(WILDCARD_TOKEN)) { // 若遇到通配符则不比较第二层令牌，直接比较第三层令牌
				break ;
			} else {
				contains = this.operation.contains(stri);
				if(!contains) {
					return contains;
				}
			}
		}
		
		List<String> examplez = RolePermission.getExample();
		if(examplez == null) {
			return contains ;
		}
		
		for (String strin : examplez) { // 第三层令牌比较
			if(strin.equals(WILDCARD_TOKEN)) { // 若遇到通配符则不比较第二层令牌，直接返回true
				return true;
			} else {
				contains = this.example.contains(strin);
				if(!contains) { // 提升运行效率，只要验证结果为false则立即返回，不再执行下一次循环
					return false;
				}
			}
		}
		return true;
	}
	
	@Override
		public boolean equals(Object obj) {
			return this.compare((RolePermission) obj);
		}
	
	/**
	 * 第三层令牌比较
	 * @param examplez - 需验证的权限字符串第三层令牌
	 * @param contains - 之前验证的结果
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean compareExampleToken(List<String> examplez,boolean contains) {
		if(examplez.isEmpty()) { // 若第三层无内容则直接返回之前验证的结果
			return contains;
		}
		
		for (String string : examplez) { // 第三层令牌验证
			if(string.equals(WILDCARD_TOKEN)) {
				return true;
			}
			contains = this.example.contains(string);
			if(!contains) { // 提升运行效率，只要验证结果为false则立即返回，不再执行下一次循环
				return false;
			}
		}
		return true; // 第三层令牌不符
	}
	
	public List<String> getRealm() {
		return realm;
	}
	public List<String> getOperation() {
		return operation;
	}
	public List<String> getExample() {
		return example;
	}
	public void setRealm(List<String> realm) {}
	public void setOperation(List<String> operation) {}
	public void setExample(List<String> example) {}

	@Override
	public String toString() {
		return "PluralisticRolePermission [CASE_SENSITIVE=" + CASE_SENSITIVE + ", realm=" + realm + ", operation="
				+ operation + ", example=" + example + "]";
	}
	
	
	public static void main(String[] args) {
		RolePermission permission  = new PluralisticRolePermission("A,B:a,c:1,2,3");
		RolePermission permission2 = new PluralisticRolePermission("A:a,c:1,2,3");
		// permission封装的权限需大于或等于permission2封装的权限，那么permission.compare(permission2)才会返回true
		// 所以permission2封装的一般是资源所要求的最低访问权限，而permission所封装的是用户自身所拥有的权限-(this > parame)
		// 所以此句代码的意思为：permission代表的事物是否拥有permission2代表事物的访问权限，拥有则返回true，反之则访问拒绝
		boolean compare = permission.compare(permission2);
		System.out.println(compare); // true
		
		RolePermission permission3 = new PluralisticRolePermission("user:add,edit,delete,select");
		RolePermission permission4 = new PluralisticRolePermission("user:add,edit");
		boolean compare2 = permission3.compare(permission4);
		System.out.println(compare2); // true
		
		RolePermission rolePermission = new SimpleRolePermission("ROLE_ADMIN");
		RolePermission rolePermission2 = new SimpleRolePermission("ROLE_ADMIN");
		boolean compare3 = rolePermission.compare(rolePermission2);
		System.out.println(compare3);  // true
		
		RolePermission permissionLower = new PluralisticRolePermission("A,B:A,c",true);
		RolePermission permissionLower2 = new PluralisticRolePermission("A,b:a,C",true);
		boolean compare4 = permissionLower.compare(permissionLower2);
		System.out.println(compare4); // true
	}
}
