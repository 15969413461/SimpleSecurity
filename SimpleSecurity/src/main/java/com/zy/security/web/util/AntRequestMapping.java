
package com.zy.security.web.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zy.security.web.interfaces.RequestMatcher;

/**
 * @author zy
 * @Date 2019-11-16 周六 下午 16:26:28
 * @Description url定义,通过传入的uri生成正则表达式，以正则表达式匹配
 * @version
 */
public class AntRequestMapping implements RequestMatcher {
	private Logger logger = LoggerFactory.getLogger(AntRequestMapping.class);

	private String url;
	private HttpMethod httpMethod;
	/**
	 * ？：匹配一个字符
	 */
	private final String ONE_CHAR = "?";
	/**
	 * *：匹配零个或多个字符
	 */
	private final String MORE_CHAR = "*";
	/**
	 * **：匹配路径中的零个或多个目录
	 */
	private final String MORE_PATH = "**";
	
	private int flag;
	
	// 匹配uri的正则表达式
	private String regex;

	
	public String getRegex() {
		return regex;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public HttpMethod getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getONE_CHAR() {
		return ONE_CHAR;
	}
	public String getMORE_CHAR() {
		return MORE_CHAR;
	}
	public String getMORE_PATH() {
		return MORE_PATH;
	}
	
	
	public AntRequestMapping(String url, String httpMethod) {
		this(url, HttpMethod.get(httpMethod));
	}
	public AntRequestMapping(String url, HttpMethod httpMethod) {
		super();
		this.url = url;
		analysis(url);
		this.httpMethod = httpMethod;
		if(DebugEnabled.debug) {
			logger.info("AntUrL：{}， \tregex：{}",this.url,this.regex);
		}
	}

	
	/**
	 * 解析uri字符串，将结果存储于成员变量之中
	 * 
	 * @param uri
	 * @return
	 */
	public void analysis(String uri) {
		this.url = uri;

		// 数据引用初始化
		String header, tails = null;
		StringBuilder builder = new StringBuilder();
		int indexOf, indexOf2 = 0;
		String substring,substring2 = null;
		
		//-----------------------------？---------------------------------------
		int oneChar = uri.indexOf(this.ONE_CHAR);
		if (oneChar != -1) {

			header = uri.substring(0, oneChar);
			tails = uri.substring(oneChar + 1);
			builder.append(header);
			
			indexOf = tails.indexOf(".");
			if(indexOf != -1) { // 剩余的字符串含有"."
				builder.append("(?:([a-zA-Z0-9]))");
				substring = tails.substring(indexOf+1); // 截除"."
				builder.append("\\.").append(substring);
				
				this.regex = builder.toString();
				return;
			}
			builder.append("(?:([a-zA-Z0-9.]))").append(tails);
			
			this.regex = builder.toString();
			return ;
		}

		//-----------------------------**---------------------------------------
		int morePath = uri.indexOf(this.MORE_PATH);
		if (morePath != -1) {
			header = uri.substring(0, morePath);
			tails = uri.substring(morePath + 2);
			indexOf = tails.indexOf(this.MORE_CHAR);
			
			if(indexOf != -1) { // 含有“*”
				substring = tails.substring(indexOf+1); // 截除“*”
				builder.append(header);
				
				indexOf2 = substring.indexOf(".");
				if(indexOf2 != -1) { // 剩余的字符串含有"."
					builder.append("(?:([a-zA-Z0-9/]+))");
					substring2 = substring.substring(indexOf2+1); // 截除"."
					builder.append("\\.").append(substring2);
					
					this.regex = builder.toString();
					return;
				}
				builder.append("(?:([a-zA-Z0-9/.]+))");
				builder.append(substring);
			}else { // 不含有“*”
				builder.append(header);
				indexOf = tails.indexOf(".");
				if(indexOf != -1) { // 剩余的字符串含有"."
					builder.append("(?:([a-zA-Z0-9/]+))");
					substring = tails.substring(indexOf+1); // 截除"."
					builder.append("\\.").append(substring);
					
					this.regex = builder.toString();
					return;
				}
				
				builder.append("(?:([a-zA-Z0-9/.]+))");
				builder.append(tails);
			}

			this.regex = builder.toString();
			return ;
		}

		//-----------------------------*---------------------------------------
		int moreChar = uri.indexOf(this.MORE_CHAR);
		if (moreChar != -1) {
			header = uri.substring(0, moreChar);
			tails = uri.substring(moreChar + 1);
			builder.append(header);
			
			indexOf = tails.indexOf(".");
			if(indexOf != -1) { // 剩余的字符串含有"."
				builder.append("(?:([a-zA-Z0-9]+))");
				substring = tails.substring(indexOf+1); // 截除"."
				builder.append("\\.").append(substring);
				
				this.regex = builder.toString();
				return;
			}
			builder.append("(?:([a-zA-Z0-9.]+))");
			builder.append(tails);

			this.regex = builder.toString();
			return ;
		}
		// 不包含“?”,“*”,“**”的uri
		this.regex = uri;
	}

	public boolean match(String mapping,String httpMethod) {
		if(!this.httpMethod.equals(HttpMethod.get(httpMethod))) { // 请求类型不同
			return false;
		}
		
		// 表达式对象
		Pattern compile = Pattern.compile(this.regex);
		// 将正则表达式与匹配字符串相关联
		Matcher matcher = compile.matcher(mapping);

		// matches-将整个字符串进行匹配，若全部匹配则返回true，反之返回false
		boolean matches = matcher.matches();
//		System.out.println("是否匹配：" + matches);

		return matches;
	}

	@Override
	public String toString() {
		return "AntRequestMapping [url=" + url + ", flag=" + flag + ", regex=" + regex + "]";
	}

	@Override
	public boolean match(HttpServletRequest request) {
		boolean meth = this.httpMethod.equals(HttpMethod.get(request.getMethod()));
		if( !meth ) {
			return false;
		}
		// 表达式对象
		Pattern compile = Pattern.compile(this.regex);
		// 将正则表达式与匹配字符串相关联
		Matcher matcher = compile.matcher(request.getRequestURI());

		// matches-将整个字符串进行匹配，若全部匹配则返回true，反之返回false
		boolean matches = matcher.matches();
		// System.out.println("是否匹配：" + matches);

		return matches;
	}
	
	public static void main(String[] args) {
		AntRequestMapping mapping = new AntRequestMapping("/logout/asd/*jsp", "get");
		AntRequestMapping mapping2 = new AntRequestMapping("/logout/?.jsp", "get");
		AntRequestMapping mapping3 = new AntRequestMapping("/logout/**", "get");
		System.err.println(mapping.match("/logout/asd/a.jsp","get"));
		System.err.println(mapping2.match("/logout/a.jsp","get"));
		System.err.println(mapping3.match("/logout/asd/a.jsp","get"));
	}
}
