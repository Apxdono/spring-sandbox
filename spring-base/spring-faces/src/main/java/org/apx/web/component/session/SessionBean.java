package org.apx.web.component.session;

import org.apx.repo.CommonRepo;
import org.apx.repo.ICommonRepo;
import org.apx.web.filter.SessionExpirationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.naming.AuthenticationException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 12.03.14
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {

    static Logger LOG = LoggerFactory.getLogger(SessionBean.class);
	Locale locale;

    @Inject
    ICommonRepo repo;

    boolean renderHidden;

    @PostConstruct
    public void init(){
        LOG.debug("Bean: {}, DB connected? {}", toString(), repo.checkConnection());
        renderHidden = false;
	    locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
    }

    public String getGreeting(){
	    FacesContext fCtx = FacesContext.getCurrentInstance();
	    HttpSession session = (HttpSession) fCtx.getExternalContext().getSession(false);
	    String sessionId = session.getId();
	    LOG.debug("Session ID: {}",sessionId);
	    return "Hello from session bean.";
    }

    public void tryMe() {
        renderHidden = !renderHidden;
	    LOG.debug("Bean: {}, show message?? {}",toString(),renderHidden);
    }

	public void changeLogLevelToDebug(AjaxBehaviorEvent e){


	}

	public void changeLogLevelToInfo(AjaxBehaviorEvent e){

	}
	

    public boolean isRenderHidden() {
        return renderHidden;
    }

    public void setRenderHidden(boolean renderHidden) {
        this.renderHidden = renderHidden;
    }

    public void makeItNull(){
        String alala = "";
        alala = null;
        alala.toString();
    }

    public String getErrorInformation(){
        Map map = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        Throwable ex = (Throwable) map.get("exceptionMessage");
	    if (ex == null) return "No error info";
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter, true));
        LOG.info("found exception : {}",ex);
	    String stackTrace = stringWriter.toString();
	    stackTrace = stackTrace.replace(System.getProperty("line.separator"), "<br/>\n")
			    .replace("\t","&#160;&#160;&#160;&#160;&#160;&#160;&#160;&#160;");
        return stackTrace;
    }

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession httpSession = (HttpSession)facesContext.getExternalContext().getSession(false);
        httpSession.invalidate();
        HttpServletResponse res = (HttpServletResponse) facesContext.getExternalContext().getResponse();

        res.reset();
        res.setStatus(401);
        res.setContentType("application/json");
        res.setContentLength(SessionExpirationFilter.ERROR_MESSAGE.getBytes().length);
        try {
            res.getWriter().write(SessionExpirationFilter.ERROR_MESSAGE);
            res.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
