package org.apx.web.component.session;

import org.apx.repo.CommonRepo;
import org.apx.repo.ICommonRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
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

    @Inject
    ICommonRepo repo;

    boolean renderHidden;

    @PostConstruct
    public void init(){
        LOG.debug("Bean: {}, DB connected? {}", toString(), repo.checkConnection());
        renderHidden = false;
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
        Map sessionMap = FacesContext.getCurrentInstance().getExternalContext().getRequestMap();
        Throwable ex = (Throwable) sessionMap.get("javax.servlet.error.exception");
        StringWriter stringWriter = new StringWriter();
        ex.printStackTrace(new PrintWriter(stringWriter, true));
        LOG.info("found exception : {}",ex);
        return stringWriter.toString();
    }
}
