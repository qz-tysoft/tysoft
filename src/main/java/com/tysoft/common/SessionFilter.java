package com.tysoft.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.thymeleaf.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
@WebFilter(urlPatterns="/*",asyncSupported=true)
public class SessionFilter implements Filter{

	/**
	 * 封装，不需要过滤的list列表
	 */
	protected static List<String> patterns = new ArrayList<String>();

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
		//非法字符过滤器
		String url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
		if (isInclude(url)){
			chain.doFilter(httpRequest, httpResponse);
			return;
		} else {
				HttpSession session = httpRequest.getSession();
				//否则web端验证session
				if (session.getAttribute("SYS_USER") != null){
					chain.doFilter(httpRequest, httpResponse);
					return;
				} else {
					//跳转至登录页面
					httpResponse.sendRedirect(httpRequest.getContextPath()+"/");
				}
			}
		}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//		System.out.println("过滤器初始化");
		patterns.add("/");
		patterns.add("/static/*");
		patterns.add("/login/validate");
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, filterConfig.getServletContext());  
	}


	/**
	 * 是否需要过滤
	 * @param url
	 * @return
	 */
	private boolean isInclude(String url) {
		if (patterns == null || patterns.size() <= 0) {  
			return false;  
		}  
		for (String ex : patterns) {  
			url = url.trim();  
			ex = ex.trim();  
			if (url.toLowerCase().matches(ex.toLowerCase().replace("*",".*")))  
				return true;  
		}  
		return false;
	}
}
