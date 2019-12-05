
package com.zy.security.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.handler.AccessDeniedHandlerImpl;
import com.zy.security.web.interfaces.AccessDeniedHandler;
import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.session.interfaces.CsrfToken;
import com.zy.security.web.session.interfaces.CsrfTokenRepository;
import com.zy.security.web.util.DebugEnabled;
import com.zy.utils.Assert;

/**
* @author zy
* @Date 2019-11-22 周五 下午 21:27:12
* @Description csrf过滤
* @version 
*/
public abstract class CsrfFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	protected final CsrfTokenRepository tokenRepository;
	// 访问拒绝处理程序
	private AccessDeniedHandler accessDeniedHandler;
	// 被过滤的uri，被@Csrf注解标记或配置文件中配置的uri
	private List<RequestMatcher> list = new ArrayList<>();
	// 最大比对次数
	private int maxCsrfTokenCount;
	
	/**
	 * 构造器，默认使用AccessDeniedHandlerImpl进行访问拒绝处理,最大比对次数5次
	 * @param tokenRepository
	 */
	public CsrfFilter(CsrfTokenRepository tokenRepository) {
		this(tokenRepository, new AccessDeniedHandlerImpl(),5);
	}
	public CsrfFilter(CsrfTokenRepository tokenRepository,int maxCsrfTokenCount) {
		this(tokenRepository, new AccessDeniedHandlerImpl(),maxCsrfTokenCount);
	}
	public CsrfFilter(CsrfTokenRepository tokenRepository, AccessDeniedHandler accessDeniedHandler) {
		this(tokenRepository, new AccessDeniedHandlerImpl(),5);
	}
	/**
	 * 默认构造器
	 * @param tokenRepository
	 * @param accessDeniedHandler - 访问拒绝处理程序
	 * @param maxCsrfTokenCount - 最大比对次数，小于此值则csrf凭证不做变化，若等于-1则csrf永远不做变化。
	 */
	public CsrfFilter(CsrfTokenRepository tokenRepository, AccessDeniedHandler accessDeniedHandler,int maxCsrfTokenCount) {
		Assert.notNull(tokenRepository, "CsrfTokenRepository 不可为null");
		Assert.notNull(accessDeniedHandler, "AccessDeniedHandler 不可为null");
		// 设置下限,-1：不作限制
		maxCsrfTokenCount = maxCsrfTokenCount < 1 && maxCsrfTokenCount != -1 ? 1 : maxCsrfTokenCount;
		this.tokenRepository = tokenRepository;
		this.accessDeniedHandler = accessDeniedHandler;
		this.maxCsrfTokenCount = maxCsrfTokenCount;
	}

	public AccessDeniedHandler getAccessDeniedHandler() {
		return accessDeniedHandler;
	}
	public void setAccessDeniedHandler(AccessDeniedHandler accessDeniedHandler) {
		this.accessDeniedHandler = accessDeniedHandler;
	}
	public CsrfTokenRepository getTokenRepository() {
		return tokenRepository;
	}
	public List<RequestMatcher> getList() {
		return list;
	}
	public void setList(List<RequestMatcher> list) {
		this.list = list;
	}
	public int getMaxCsrfTokenCount() {
		return maxCsrfTokenCount;
	}
	public void setMaxCsrfTokenCount(int maxCsrfTokenCount) {
		this.maxCsrfTokenCount = maxCsrfTokenCount;
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		
		for (RequestMatcher matcher : list) {
			if(matcher.match(req)) { // 需要csrf防御
				// csrfToken: csrf凭证以此为准
				CsrfToken csrfToken = this.tokenRepository.loadToken(req);
				
				boolean supports = this.supports(this.tokenRepository.getClass());
				String token = null;
				if(supports) {
					token = this.attemptCstfToken(req, resp, csrfToken);
				}
				
				if(token == null || !csrfToken.getToken().equals(token)) { // 没有通过csrf防御
					this.accessDeniedHandler.handle(req, resp);
					
					if(DebugEnabled.debug) {
						logger.info("此请求未通过csrf防御，方法返回，by：{}",req.getRequestURL());
					}
					return;
				}
				int count = csrfToken.getCount();
				++count ;
				// 比对成功次数大于等于maxCsrfTokenCount的值时更新csrf数据
				if(count >= maxCsrfTokenCount && maxCsrfTokenCount != -1) {
					this.tokenRepository.refreshSessionInformationAttrVal(req);
				}
			}
		}
		// 将通过csrf防御和未受拦截到url放行到下一个过滤器
		chain.doFilter(req, resp);
	}

	/**
	 * 获取csrfToken值，子类通过此方法以从cookie中或请求信息中获得。
	 * @param req
	 * @param resp
	 * @param csrfToken 
	 * @return csrfToken字符串
	 */
	protected abstract String attemptCstfToken (HttpServletRequest req, HttpServletResponse resp, CsrfToken csrfToken) ;
	
	/**
	 * 根据CsrfTokenRepository的具体实现决定CsrfToken的保存位置，或许是cookie中，亦或许是请求报文中
	 * @param csrfTokenRepository
	 * @return
	 */
	protected abstract boolean supports(Class<?> csrfTokenRepository);
	
	@Override
	public void destroy() {}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
