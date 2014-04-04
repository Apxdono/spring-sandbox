package org.apx.web.handlers;

import org.apx.utils.JSFUtils;
import org.hibernate.annotations.OptimisticLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.SystemEvent;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 04.04.14
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class DefaultExceptionHandler extends ExceptionHandlerWrapper {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    public static final String MESSAGE_DETAIL_KEY = "ip.client.jsftoolkit.messageDetail";

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
        FacesContext fc = FacesContext.getCurrentInstance();
        for (Iterator<ExceptionQueuedEvent> i = getUnhandledExceptionQueuedEvents().iterator(); i.hasNext(); ) {
            ExceptionQueuedEvent event = i.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            Throwable cause = context.getException();
            if(!processThrowable(cause)){
                JSFUtils.redirect(fc,handleUnexpected(fc,cause));

            }
            i.remove();
        }
    }


    protected String handleUnexpected(FacesContext facesContext, final Throwable t) {
        LOG.error("An unexpected internal error has occurred", t.getMessage());
        facesContext.getExternalContext().getRequestMap().put("javax.servlet.error.exception",t);
        return facesContext.getExternalContext().getRequestContextPath()+"/error.jsf";
    }

    protected boolean processThrowable(Throwable t) {
        if (t == null) {
            return false;
        }
        if (t instanceof OptimisticLockException){
            LOG.debug("HOLY MAMA WE ARE LOCKED");
            FacesMessage message = new FacesMessage();
            message.setSummary("Запись была изменена другим пользователем. Пожалуйста обновите страницу и повторите ввод");
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, message);
            return true;
        }

        return processThrowable(t.getCause());
    }
}