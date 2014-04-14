package org.apx.commons.jsf.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.*;
import java.util.Date;
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

	public static final String ATTRIBUTE_ERROR_EXCEPTION = "exception.message";
    public static final String ATTRIBUTE_ERROR_EXCEPTION_TYPE = "exception.type";
    public static final String ATTRIBUTE_ERROR_TIME = "exception.time";


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
		    final Map<String, Object> requestMap = fc.getExternalContext().getRequestMap();
		    final ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) fc.getApplication().getNavigationHandler();
		    //here you do what ever you want with exception
		    try {
			    //redirect error page
			    requestMap.put(ATTRIBUTE_ERROR_EXCEPTION, t);
			    requestMap.put(ATTRIBUTE_ERROR_EXCEPTION_TYPE, t.getClass());
			    requestMap.put(ATTRIBUTE_ERROR_TIME, new Date());
			    nav.performNavigation("/error.xhtml");
			    fc.renderResponse();
		    }
		    finally {
			    //remove it from queue
			    i.remove();             }
	    }
	    //parent hanle
	    getWrapped().handle();
    }

}