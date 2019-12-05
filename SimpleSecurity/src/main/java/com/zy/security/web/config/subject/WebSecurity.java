
package com.zy.security.web.config.subject;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.interfaces.RequestMatcher;
import com.zy.security.web.util.AntRequestMapping;
import com.zy.security.web.util.DebugEnabled;
import com.zy.security.web.util.HttpMethod;

/**
* @author zy
* @Date 2019-11-27 周三 上午 01:58:02
* @Description
* @version 
*/
@SuppressWarnings("unused")
public class WebSecurity {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private IgnoringConfiguration ignoring;
	
	/**
	 * 进入uri排除设置
	 * @return
	 */
	public IgnoringConfiguration ignoring(){
		if(this.ignoring == null) {
			this.ignoring  = new IgnoringConfiguration(this);
		}
		return this.ignoring;
	}
	
	/**
	 * 是否显示过滤器链debug信息，默认为false,使用 {@link DebugEnabled}类引用此结果<br/>
	 * <p>建议：如果要开启debug模式，请配置为第一项，以获得完整的debug信息<p/>
	 * @param debug - true: 开启 ,false: 关闭
	 * @return
	 */
	public WebSecurity debug(boolean debug) {
		if(debug) {
			DebugEnabled.debug = true;
		}
		return this;
	}
	
	public WebSecurity and() {
		return this;
	}
}
