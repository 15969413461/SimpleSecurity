
package com.zy.security.web.session.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author zy
* @Date 2019-11-17 周日 下午 16:10:21
* @Description Csrf凭证操作接口
* @version 
*/
public interface CsrfTokenRepository {

	/**
	 * 根据CsrfToken对象的有无，决定请求中的Session或Coolie是否与CsrfToken解绑还是相关联.<br/>
	 * 即token为null时，为解绑，token不为null则绑定 <br/>
	 * <p> 在用户身份认证成功后会被调用存储CsrfToken <p/>
	 * @param token - CsrfToken对象
	 * @param request - 当前请求的HttpServletRequest对象
	 * @param response - 当前请求的HttpServletResponse对象
	 */
	void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 从当前请求中的Cookie或Session中获得CsrfToken对象信息,以此为基准进行比对
	 * <p> 在需要进行csrf凭证比对是会被调用 </>
	 * @param request
	 * @return
	 */
	CsrfToken loadToken(HttpServletRequest request);

	/**
	 * 创建一个CsrfToken对象
	 * @param request
	 * @return
	 */
	CsrfToken generateToken();
	
	/**
	 * 更新SessionInformation之中的csrf凭证数据
	 * @param
	 * @return
	 */
	boolean refreshSessionInformationAttrVal(HttpServletRequest request);
	
	void setCookieName(String name);
	void setParameName(String name);
	void setHeaderName(String name);
	
}
