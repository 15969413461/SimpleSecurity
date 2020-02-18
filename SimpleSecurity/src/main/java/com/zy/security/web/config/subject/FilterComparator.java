
package com.zy.security.web.config.subject;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import com.zy.security.web.filter.AbstractAuthenticationProcessingFilter;
import com.zy.security.web.filter.AccessManagementFilter;
import com.zy.security.web.filter.AnonymousAuthenticationFilter;
import com.zy.security.web.filter.CsrfCookieFilter;
import com.zy.security.web.filter.CsrfFilter;
import com.zy.security.web.filter.CsrfRequestMsgFilter;
import com.zy.security.web.filter.DefaultLoginPageGeneratingFilter;
import com.zy.security.web.filter.LogoutFilter;
import com.zy.security.web.filter.RememberMeAuthenticationFilter;
import com.zy.security.web.filter.SecurityContextPersistenceFilter;
import com.zy.security.web.filter.SessionManagementFilter;
import com.zy.security.web.filter.UsernamePasswordAuthenticationFilter;

/**
* @author zy
* @Date 2019-11-28 周四 上午 01:21:37
* @Description
* @version 
*/
@SuppressWarnings("deprecation")
public class FilterComparator implements Comparator<Filter> {
	private static final int STEP = 10;
	/** 集合排序的依照 */
	private Map<String, Integer> filterToOrder = new HashMap<>();
	private int order;
	
	public FilterComparator() {
		put(SecurityContextPersistenceFilter.class, STEP);
		
		order = STEP;
		put(CsrfFilter.class, order);
		put(CsrfCookieFilter.class, ++order);
		put(CsrfRequestMsgFilter.class, ++order);
		
		order = STEP*3;
		put(LogoutFilter.class, order);
		
		order = STEP*4;
		put(AbstractAuthenticationProcessingFilter.class, order);
		put(UsernamePasswordAuthenticationFilter.class, ++order);
		
		order = STEP*5;
		put(DefaultLoginPageGeneratingFilter.class, order);
		
		order = STEP*6;
		put(RememberMeAuthenticationFilter.class, order);
		
		order = STEP*7;
		put(AnonymousAuthenticationFilter.class, order);
		
		order = STEP*8;
		put(SessionManagementFilter.class, order);
		
		order = STEP*9;
		put(AccessManagementFilter.class, order);
	}


	@Override
	public int compare(Filter leftFilter, Filter rightFilter) {
		Integer left = getOrder(leftFilter.getClass());
		Integer right = getOrder(rightFilter.getClass());
		return left - right;
	}
	
	private void put(Class<? extends Filter> filter, int position) {
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
	 * 允许在某个已知过滤器(已注册的过滤器)之前添加过滤器
	 * @param filter - 注册的过滤器
	 * @param beforeFilter - 已注册的过滤器
	 */
	public void registerBefore(Class<? extends Filter> filter,Class<? extends Filter> beforeFilter) {
		Integer position = getOrder(beforeFilter);
		if (position == null) {
			throw new IllegalArgumentException("已注册的过滤器排序标识不存在，by：" + beforeFilter);
		}
		put(filter, --position);
	}
	
	/**
	 * 在指定已知过滤器(已注册的过滤器)的所在位置添加过滤器,而原位置的过滤器往后移一位
	 * @param filter - 注册的过滤器
	 * @param atFilter - 已注册的过滤器
	 */
	public void registerAt(Class<? extends Filter> filter,Class<? extends Filter> atFilter) {
		Integer position = getOrder(atFilter);
		if (position == null) {
			throw new IllegalArgumentException("已注册的过滤器排序标识不存在，by：" + atFilter);
		}

		put(filter, position);
		put(atFilter, ++position);
	}
	
	/**
	 * 允许在一个已知的过滤器(已注册的过滤器)之后添加过滤器
	 * @param filter - 要在类型Afterfilter之后注册的过滤器
	 * @param afterFilter - 已知过滤器，存在于过滤器集合中
	 */
	public void registerAfter(Class<? extends Filter> filter,Class<? extends Filter> afterFilter) {
		Integer position = getOrder(afterFilter);
		if (position == null) {
			throw new IllegalArgumentException("已注册的过滤器排序标识不存在，by：" + afterFilter);
		}

		put(filter, ++position);
	}
	
	/**
	 * 允许在过滤器链末尾添加过滤器
	 * @param filter - 要在过滤器链末尾注册的过滤器
	 */
	public void registerEnd(Class<? extends Filter> filter) {
		Integer position = getOrder(filter);
		if (position != null) {
			throw new IllegalArgumentException("此过滤器排序标识已存在，by：" + filter);
		}
		put(filter, ++this.order);
	}

	public Map<String, Integer> getFilterToOrder() {
		return filterToOrder;
	}
}
