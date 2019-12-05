
package com.zy.security.web.config.subject;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.core.authentication.interfaces.AuthenticationManager;
import com.zy.security.web.config.AuthenticationManagerBuilder;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.DebugEnabled;

/**
* @author zy
* @Date 2019-11-27 周三 下午 15:18:39
* @Description 注册为Filter，对所有请求执行过滤器链
* @version 
*/
@WebFilter(filterName="FilterChainProxy",urlPatterns="/*")
public class FilterChainProxy implements Filter {
	private Logger logger = LoggerFactory.getLogger(FilterChainProxy.class);
	
	// 过滤器容器
	private List<Filter> filters;
	private VirtualFilterChain vfc;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		HttpSecurity httpSecurity = new HttpSecurity();
		httpSecurity.init();
		WebSecurity webSecurity = new WebSecurity();
		AuthenticationManagerBuilder authBuilder = new AuthenticationManagerBuilder();
		
		WebSecurityConfigurerAdapter configurerAdapter = null;
		try {
			configurerAdapter = SecurityConfigurerBuilder.builder();
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
		}
		
		// 读取配置类配置
		configurerAdapter.configureParent(authBuilder);
		configurerAdapter.configureParent(httpSecurity);
		configurerAdapter.configureParent(webSecurity);
		
		// 根据配置类配置创建AuthenticationManager对象
		AuthenticationManager authenticationManager = authBuilder.authenticationManagerBuilder(httpSecurity);
		httpSecurity.setAuthenticationManager(authenticationManager);
		
		this.filters = httpSecurity.config();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		if(DebugEnabled.debug) {
			logger.info("当前请求：'{}'，类型：'{}'",req.getRequestURI(),req.getMethod());
		}
		
		boolean isIngoring = isIgnoring(req);
		
		if(this.filters == null || filters.isEmpty() || isIngoring) {
			chain.doFilter(request, response);
		}else {
			if(vfc == null) {
				vfc = new VirtualFilterChain(chain, filters);
			}
			vfc.doFilter(request, response);
			// 在过滤器链执行完毕或返回时重置当前执行过滤器标记
			vfc.currentPosition = 0;
		}
	}

	/**
	 * 当前url是否是忽略的url
	 * @param requestURI
	 * @return
	 */
	private boolean isIgnoring(HttpServletRequest req) {
		for (RequestMatcher mapping : IgnoringConfiguration.ignoredRequests) {
			if(mapping.match(req)) {
				if(DebugEnabled.debug) {
					logger.info("忽略.请求：'{}'，类型：'{}'",req.getRequestURI(),req.getMethod());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {}

	public List<Filter> getFilters() {
		return filters;
	}
	public void setFilters(List<Filter> filters) {
		this.filters = filters;
	}
	
	// 实现过滤器链逻辑
	private static class VirtualFilterChain implements FilterChain {
		// 当前过滤器的索引标识
		private int currentPosition = 0;
		// 真正的FilterChain
		private final FilterChain chain;
		private final List<Filter> filters;
		private final int size;
		
		public VirtualFilterChain(FilterChain chain, List<Filter> filter) {
			super();
			this.chain = chain;
			this.filters = filter;
			this.size = filter.size();
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
			if (currentPosition == size) { // 过滤器链已执行完毕，放行请求到下一个过滤器
				this.chain.doFilter(request, response);
				return;
			}
			Filter nextFilter = this.filters.get(currentPosition++);
			// 按梯度输出使用到的过滤器
			if(DebugEnabled.debug) {
				System.out.println(this.currentPosition+"--"+nextFilter.getClass().getSimpleName());
			}
			nextFilter.doFilter(request, response, this);
		}
	}
}
