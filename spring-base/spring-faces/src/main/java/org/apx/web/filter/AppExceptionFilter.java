package org.apx.web.filter;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by oleg on 3/13/14.
 */
public class AppExceptionFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		try {
			filterChain.doFilter(servletRequest, servletResponse);
		}
		catch (FileNotFoundException e) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
		}
		catch (ServletException e) {
			throw new ServletException(ExceptionUtils.getRootCause(e));
		}
	}

	@Override
	public void destroy() {

	}


}
