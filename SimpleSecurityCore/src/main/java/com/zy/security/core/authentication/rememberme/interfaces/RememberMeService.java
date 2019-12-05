
package com.zy.security.core.authentication.rememberme.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zy.security.core.token.Authentication;

/**
* @author zy
* @Date 2019-11-17 周日 下午 13:22:54
* @Description RememberMe服务处理类
* @version 
*/
public interface RememberMeService {
	/**
	 * 从rememberMe cookie中获得用户身份信息并封装为Authentication对象
	 * @param request
	 * @param response
	 * @return
	 */
	Authentication autoLogin(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 检查此请求是否启用了Remember服务,若启用则根据传递的Authentication对象实现类进行个性化处理
	 * @param request
	 * @param response
	 * @param authentication
	 */
	void loginSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication);
	
	/**
	 * 用于设置“记住我”cookie,此方法内部逻辑与Token结果相关。<br/>
	 * 非UsernamePasswordAuthenticationToken需重写此方法与 supports(Class<?> authentication) 以对其他的Token进行适配
	 * @param request
	 * @param response
	 * @param authentication
	 */
	void onLoginSuccess(HttpServletRequest request, HttpServletResponse response,Authentication authentication);
	
	/**
	 * 判别此Token对象的数据格式是否能被本类所支持
	 * @param authentication
	 * @return
	 */
	boolean supports(Class<?> authentication);
}
