
package com.zy.security.web.session.interfaces;

import java.io.Serializable;

/**
* @author zy
* @Date 2019-11-18 周一 下午 15:10:01
* @Description Csrf凭证poji类
* @version 
*/
public interface CsrfToken extends Cloneable,Serializable {

	String getParameterName();
	String getHeaderName();
	String getCookieName();
	String getToken();
	int getCount();
}
