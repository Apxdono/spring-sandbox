package org.apx.web.impl.scope;

import java.util.Map;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;


/**
 * This makes @ViewScoped annotation work
 */
public class ViewScope implements Scope {

    static Logger LOG = LoggerFactory.getLogger(ViewScope.class);

    public Object get(String name, ObjectFactory<?> objectFactory) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap();
        Object viewScopedBean = viewMap.get(name);

        if (viewScopedBean == null) {
            viewScopedBean = objectFactory.getObject();
            viewMap.put(name, viewScopedBean);
        }

        LOG.debug("Retrieved viewscope bean {}",viewScopedBean);

        return viewScopedBean;
    }

    public void registerDestructionCallback(String name, Runnable callback) {
    }

    public Object remove(String name) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> viewMap = facesContext.getViewRoot().getViewMap();
        LOG.debug("Removed viewscope bean {}",name);
        return viewMap.remove(name);
    }

    public Object resolveContextualObject(String key) {

        return null;
    }

    public String getConversationId() {

        return null;
    }

}
