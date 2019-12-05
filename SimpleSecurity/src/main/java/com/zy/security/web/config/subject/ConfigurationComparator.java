
package com.zy.security.web.config.subject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import com.zy.security.web.config.AbstractHttpConfigurer;
import com.zy.security.web.config.AnonymousConfiguration;
import com.zy.security.web.config.AuthorizationConfiguration;
import com.zy.security.web.config.CsrfConfiguration;
import com.zy.security.web.config.ForLoginConfiguration;
import com.zy.security.web.config.LogoutConfiguration;
import com.zy.security.web.config.RememberMeConfiguration;
import com.zy.security.web.config.SecurityContextPersistenceConfiguration;
import com.zy.security.web.config.SessionManagerConfiguration;

/**
* @author zy
* @Date 2019-11-28 周四 上午 01:21:37
* @Description 配置存储类对象排序,受限于各个配置类的数据流转,以确保根据各个配置存储类正确的创建Filter
* @version 
*/
public class ConfigurationComparator implements Comparator<AbstractHttpConfigurer<HttpSecurity>> {
	private static final int STEP = 3;
	/** 集合排序的依照 */
	private Map<String, Integer> filterToOrder = new HashMap<>();
	
	
	public ConfigurationComparator() {
		int order = STEP;
		put(AnonymousConfiguration.class, order);
		
		order = STEP*2;
		// CsrfConfiguration需要ForLoginConfiguration的errorPage属性
		put(CsrfConfiguration.class, order);
		
		order = STEP*3;
		put(RememberMeConfiguration.class, order);
		
		order = STEP*4;
		// ForLoginConfiguration需要RememberMeConfiguration创建的RememberMeServic实例以进行身份认证成功后的rememberMe操作
		put(ForLoginConfiguration.class, order);
		
		order = STEP*5;
		put(LogoutConfiguration.class, order);
		
		order = STEP*6;
		// SessionManagerConfiguration需要CsrfConfiguration创建的CsrfTokenRepository实例来组织csrf防御
		put(SessionManagerConfiguration.class, order);
		
		order = STEP*7;
		put(SecurityContextPersistenceConfiguration.class, order);
		
		order = STEP*8;
		put(AuthorizationConfiguration.class, order);
	}

	@Override
	public int compare(AbstractHttpConfigurer<HttpSecurity> o1, AbstractHttpConfigurer<HttpSecurity> o2) {
		Integer left = getOrder(o1.getClass());
		Integer right = getOrder(o2.getClass());
		return left - right; // 升序
	}

	private void put(Class<? extends AbstractHttpConfigurer<HttpSecurity>> filter, int position) {
		this.filterToOrder.put(filter.getName(), position);
	}
	
	/**
	 * 根据类名获得排序的序列号
	 * @param claz
	 * @return
	 */
	private Integer getOrder(Class<?> claz) {
		while (claz != null) {
			Integer result = this.filterToOrder.get(claz.getName());
			if (result != null) {
				return result;
			}
			// 获得超类的Class对象
			claz = claz.getSuperclass();
		}
		return null;
	}
	
	/**
	 * 判断此过滤器是否在排序集合中
	 * @param filter
	 * @return
	 */
	public boolean isRegistered(Class<? extends Filter> filter) {
		return getOrder(filter) != null;
	}

	/**
	 * 允许在一个已知的筛选器类(以添加到filter列表中的过滤器)之后添加筛选器
	 * @param filter - 要在类型Afterfilter之后注册的筛选器(需要注册的过滤器)
	 * @param afterFilter - 已知过滤器，存在于过滤器集合中
	 */
//	public void registerAfter(Class<? extends Filter> filter,Class<? extends Filter> afterFilter) {
//		
//		Integer position = getOrder(afterFilter);
//		if (position == null) {
//			throw new IllegalArgumentException("已注册的过滤器排序标识不存在，by：" + afterFilter);
//		}
//
//		put(filter, ++position);
//	}

	/**
	 * 在指定筛选器类的位置添加筛选器
	 * @param filter - 注册的过滤器
	 * @param atFilter - 已注册的过滤器
	 */
//	public void registerAt(Class<? extends Filter> filter,Class<? extends Filter> atFilter) {
//		Integer position = getOrder(atFilter);
//		if (position == null) {
//			throw new IllegalArgumentException("已注册的过滤器排序标识不存在，by：" + atFilter);
//		}
//
//		put(filter, position);
//		put(atFilter, ++position);
//	}

	/**
	 * 允许在某个已知筛选器类之前添加筛选器
	 * @param filter - 注册的过滤器
	 * @param beforeFilter - 已注册的过滤器
	 */
//	public void registerBefore(Class<? extends Filter> filter,Class<? extends Filter> beforeFilter) {
//		Integer position = getOrder(beforeFilter);
//		if (position == null) {
//			throw new IllegalArgumentException("已注册的过滤器排序标识不存在，by：" + beforeFilter);
//		}
//		put(filter, --position);
//	}
}
