package org.apx.web.handlers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apx.utils.JSFUtils;
import org.hibernate.annotations.OptimisticLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import javax.faces.view.ViewDeclarationLanguage;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 04.04.14
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class DefaultExceptionHandler extends ExceptionHandlerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

	private static final String ATTRIBUTE_ERROR_EXCEPTION = "javax.servlet.error.exception";
	private static final String ATTRIBUTE_ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
	private static final String ATTRIBUTE_ERROR_MESSAGE = "javax.servlet.error.message";
	private static final String ATTRIBUTE_ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
	private static final String ATTRIBUTE_ERROR_STATUS_CODE = "javax.servlet.error.status_code";


    private ExceptionHandler wrapped;

    public DefaultExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
	    final Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator();

	    while (i.hasNext()) {
		    ExceptionQueuedEvent event = i.next();
		    ExceptionQueuedEventContext context =
				    (ExceptionQueuedEventContext) event.getSource();
		    // get the exception from context
		    Throwable t = context.getException();
		    final FacesContext fc = FacesContext.getCurrentInstance();
		    final ExternalContext externalContext = fc.getExternalContext();
		    final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
		    final ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
		    //here you do what ever you want with exception
		    try {
			    //redirect error page
			    requestMap.put("exceptionMessage", t);
			    nav.performNavigation("/error.xhtml");
			    fc.renderResponse();
			    // remove the comment below if you want to report the error in a jsf error message
			    //JsfUtil.addErrorMessage(t.getMessage());
		    }
		    finally {
			    //remove it from queue
			    i.remove();             }
	    }
	    //parent hanle
	    getWrapped().handle();
    }

}