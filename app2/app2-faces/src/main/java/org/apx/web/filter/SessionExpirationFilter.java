package org.apx.web.filter;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by oleg on 3/13/14.
 */
public class SessionExpirationFilter implements Filter {

    public static final String ERROR_MESSAGE = "{\"error\":\"sessionExpired\"}";

    @Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) servletRequest;
		HttpServletResponse res = (HttpServletResponse) servletResponse;

        if(isAJAXRequest(req) && req.getRequestedSessionId() != null && !req.isRequestedSessionIdValid()){
            res.reset();
            res.setStatus(401);
            res.setContentType("application/json");
            res.setContentLength(ERROR_MESSAGE.getBytes().length);
            res.getWriter().write(ERROR_MESSAGE);
            res.flushBuffer();
            return;
        }
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {

	}

    private boolean isAJAXRequest(HttpServletRequest request) {
        boolean check = false;
        String facesRequest = request.getHeader("Faces-Request");
        if (facesRequest != null && facesRequest.equals("partial/ajax")) {
            check = true;
        }
        return check;
    }
}
