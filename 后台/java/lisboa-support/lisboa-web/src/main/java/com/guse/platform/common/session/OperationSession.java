package com.guse.platform.common.session;

import javax.servlet.http.HttpServletRequest;

/**
 * 封装session会话
 * 
 * @author nbin
 * 
 */
public class OperationSession{
	/**
	 * 把信息存入session
	 * @param request
	 * @param attributeKey
	 * @param obj
	 */
	public static void putSession(final HttpServletRequest request, final String attributeKey, final Object obj) {
		request.getSession().setAttribute(attributeKey, obj);
	}
	
	/**
	 * 从session中取得信息
	 * @param request
	 * @param attributeKey
	 * @return
	 */
	public static Object getSession(final HttpServletRequest request, final String attributeKey) {
		return request.getSession().getAttribute(attributeKey);
	}
	
	/**
	 * 从request或session中移除信息
	 * @param request
	 * @param key
	 */
	public static void remove(HttpServletRequest request, String key) {
		request.removeAttribute(key);
		request.getSession().removeAttribute(key);
	}
}
