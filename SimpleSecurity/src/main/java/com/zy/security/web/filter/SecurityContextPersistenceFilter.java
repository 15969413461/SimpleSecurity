
package com.zy.security.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.zy.security.core.context.SecurityContext;
import com.zy.security.core.context.SecurityContextStrategy;

/**
* @author zy
* @Date 2019-11-18 周一 下午 23:40:43
* @Description 负责从 SecurityContextStrategy中获取SecurityContext,
* @version 
*/
public class SecurityContextPersistenceFilter implements Filter {
//	private Logger logger = LoggerFactory.getLogger(getClass());
	
	// 已处理标识
	static final String FILTER_APPLIED = "security_context_init_filter";
	
	public SecurityContextPersistenceFilter() {}
	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		logger.info("SecurityContextInitFilter  -  init");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if(req.getAttribute(FILTER_APPLIED) != null) {
			// 此请求已经过此过滤器
			chain.doFilter(request, response);
			return ;
		}
		// 为第一次经过此过滤器的请求添加已处理标识
		req.setAttribute(FILTER_APPLIED, true);
		// 从session中获得上下文信息
		SecurityContext loadContext = SecurityContextStrategy.loadContext(req);
		if(loadContext == null) {
			loadContext = SecurityContextStrategy.createEmptyContext();
		}
		try {
			// 为当前线程关联一个SecurityContext
			SecurityContextStrategy.setContext(loadContext);
			chain.doFilter(req,resp);
		} finally {
			// 清除ThreadLocal之中的历史记录
			SecurityContextStrategy.clearContext();
			request.removeAttribute(FILTER_APPLIED);
		}
	}
	
	@Override
	public void destroy() {}
}
