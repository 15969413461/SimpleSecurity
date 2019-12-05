
package com.zy.security.web.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AnyRequestMapping;
import com.zy.security.web.util.DebugEnabled;
import com.zy.security.web.util.HttpMethod;

/**
* @author zy
* @Date 2019-11-20 周三 下午 16:40:36
* @Description 构建用户登录界面
* @version 
*/
public class DefaultLoginPageGeneratingFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private String usernameParameter;
	private String passwordParameter;
	private String rememberMeParameter;
	
	// 身份验证uri
	private String authenticationUrl;
	// 登录界面uri
	private RequestMatcher loginPageUrl;
	
	// 身份验证失败跳转的uri
	private String loginError;
	// 退出成功跳转的uri
	private String logoutSuccessUrl;
	// session失效跳转的uri
	private String sessionInvalidUri;
	// 访问被拒绝跳转的uri
	private String accessDenied;
	
	private final String row = "\n\t";
	private final String tabRow = "\n\t\t";
	private final String tabsRow = "\n\t\t\t";
	
	
	public DefaultLoginPageGeneratingFilter(String loginPageUrl) {
		this.loginPageUrl = new AnyRequestMapping(loginPageUrl, HttpMethod.GET);
	}
	/**
	 * 默认登录界面：/login-GET
	 */ 
	public DefaultLoginPageGeneratingFilter() {
		this.loginPageUrl = new AnyRequestMapping("/login", HttpMethod.GET);
	}
	
	
	public DefaultLoginPageGeneratingFilter(String usernameParameter, String passwordParameter,
			String rememberMeParameter, String authenticationUrl, String loginPageUrl, String loginError,
			String logoutSuccessUrl) {
		this(loginPageUrl);
		this.usernameParameter = usernameParameter;
		this.passwordParameter = passwordParameter;
		this.rememberMeParameter = rememberMeParameter;
		this.authenticationUrl = authenticationUrl;
		this.loginError = loginError;
		this.logoutSuccessUrl = logoutSuccessUrl;
	}
	
	
	public String getFailureUrl() {
		return loginError;
	}
	public void setFailureUrl(String failureUrl) {
		this.loginError = failureUrl;
	}
	public String getLogoutSuccessUrl() {
		return logoutSuccessUrl;
	}
	public void setLogoutSuccessUrl(String logoutSuccessUrl) {
		this.logoutSuccessUrl = logoutSuccessUrl;
	}
	public String getUsernameParameter() {
		return usernameParameter;
	}
	public void setUsernameParameter(String usernameParameter) {
		this.usernameParameter = usernameParameter;
	}
	public String getPasswordParameter() {
		return passwordParameter;
	}
	public void setPasswordParameter(String passwordParameter) {
		this.passwordParameter = passwordParameter;
	}
	public String getRememberMeParameter() {
		return rememberMeParameter;
	}
	public void setRememberMeParameter(String rememberMeParameter) {
		this.rememberMeParameter = rememberMeParameter;
	}
	public String getAuthenticationUrl() {
		return authenticationUrl;
	}
	public void setAuthenticationUrl(String authenticationUrl) {
		this.authenticationUrl = authenticationUrl;
	}
	public RequestMatcher getLoginPageUrl() {
		return loginPageUrl;
	}
	public void setLoginPageUrl(RequestMatcher loginPageUrl) {
		this.loginPageUrl = loginPageUrl;
	}
	public String getLoginError() {
		return loginError;
	}
	public void setLoginError(String loginError) {
		this.loginError = loginError;
	}
	public String getSessionInvalidUri() {
		return sessionInvalidUri;
	}
	public void setSessionInvalidUri(String sessionInvalidUri) {
		this.sessionInvalidUri = sessionInvalidUri;
	}
	public String getAccessDenied() {
		return accessDenied;
	}
	public void setAccessDenied(String accessDenied) {
		this.accessDenied = accessDenied;
	}
	
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse resp = (HttpServletResponse) response;
		
		boolean logoutSuccess = isLogoutSuccess(req);
		boolean loginError = isErrorPage(req);
		boolean isInvalid = isSessionInvalid(req);
		boolean isError = isAccessDenied(req);
		if(this.loginPageUrl.match(req)) {
			
			String pageHtml = generatePageHtml(loginError,logoutSuccess,isInvalid,isError);

			response.setContentType("text/html;charset=UTF-8");
			response.setContentLength(pageHtml.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(pageHtml);
			
			if(DebugEnabled.debug) {
				logger.info("DefaultLoginPageGeneratingFilter return{返回登录页面}");
			}
			
			return ;
		}
		chain.doFilter(request, response);
	}
	
	/**
	 * 根据参数动态的创建html页面
	 * @param request
	 * @param logoutSuccess
	 * @param loginError
	 * @param isError 
	 * @return
	 */
	private String generatePageHtml(boolean loginError,boolean logoutSuccess,boolean isInvalid, boolean isError) {
		StringBuilder build = new StringBuilder();
		build.append("<html>\n\t<head>\n\t\t<title>登录界面</title>\n\t\t<meta charset='UTF-8'/>\n"); 
		build.append("\t\t<style>._left{text-align: right;}</style>\n\t</head>\n\t");// head标签完成
		build.append("<body>").append(tabRow);
		if(loginError) {
			build.append("<h4 style='color: #737373;'>用户名或密码错误</h4>");
		}else if(logoutSuccess) {
			build.append("<h4 style='color: #737373;'>退出成功</h4>");
		}else if(isInvalid) {
			build.append("<h4 style='color: #737373;'>登录状态过期</h4>");
		}else if(isError) {
			build.append("<h4 style='color: #737373;'>无权访问,请登陆其他账户</h4>");
		}else {
			build.append("<h4 style='color: #737373;'>请输入用户名和密码</h3>");
		}// 提示标语完成
		build.append(tabRow);
		build.append("<form action='").append(this.authenticationUrl).append("' method='post'>"); 
		build.append(tabsRow);
		build.append("<table>");
		build.append(tabsRow);
		build.append("\t<tr><td class='_left'>用户名:</td><td ><input type='text' name='").append(this.usernameParameter).append("'/></td></tr>");
		build.append(tabsRow);
		build.append("\t<tr><td class='_left'>密码:</td><td><input type='text' name='").append(this.passwordParameter).append("'/></td>");
		build.append(tabsRow);
		build.append("\t<tr><td class='_left'>记住我:</td><td>  <input type='checkbox' name='").append(this.rememberMeParameter).append("'/></td></tr>");
		build.append(tabsRow);
//		build.append("\t<tr><td></td><td><input type='hidden' name='").append(this.csrfKey).append("' value='").append(this.csrfValue).append("'/></td></tr>");
//		build.append(tabsRow);
		build.append("\t<tr><td></td><td><input type='submit' value='提交'/></td></tr>");
		build.append(tabRow);
		build.append("</form>"); 
		build.append(row);
		build.append("</body>\n"); 
		build.append("</html>"); 
		return build.toString();
	}
	
	private boolean isLogoutSuccess(HttpServletRequest request) {
		return logoutSuccessUrl != null && matches(request, logoutSuccessUrl);
	}
	private boolean isErrorPage(HttpServletRequest request) {
		return loginError != null && matches(request, loginError);
	}
	private boolean isSessionInvalid(HttpServletRequest request) {
		return sessionInvalidUri != null && matches(request, sessionInvalidUri);
	}
	private boolean isAccessDenied(HttpServletRequest request) {
		return accessDenied != null && matches(request, accessDenied);
	}
	private boolean matches(HttpServletRequest request, String url) {
		if (!"GET".equals(request.getMethod()) || url == null) {
			return false;
		}
		String requestURI = request.getRequestURI();
//		StringBuffer requestURL = request.getRequestURL();
		// 获得？之后的字符串
		String queryString = request.getQueryString();
		StringBuilder builder = new StringBuilder();
		builder.append(requestURI).append("?").append(queryString);
		if(url.equals(builder.toString())) {
			return true;
		}
		return false;
	}
	
	
	@Override
	public void destroy() {}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}
}
