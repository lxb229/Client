package com.guse.four_one_nine.filter;
 
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
 
public class CORSFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
        chain.doFilter(request, response);		
	}

	public void destroy() {
		
	}
   
}