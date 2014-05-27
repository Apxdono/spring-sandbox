package org.apx.web.component.request;

import org.apx.annotation.processor.ViewPageProcessor;
import org.apx.utils.JSFUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 28.03.14
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean(name = "navigation")
@RequestScoped
public class NavigationBean implements Serializable {
    private static final long serialVersionUID = 8675654225942206996L;

    public String listPage() {
        return "list.jsf?faces-redirect=true";
    }

    public String newPage() {
        return "new.jsf?faces-redirect=true";
    }

    public String viewPage() {
        Object o = JSFUtils.getAttribute(FacesContext.getCurrentInstance(), "target", Object.class);
        Boolean redirect = JSFUtils.getAttribute(FacesContext.getCurrentInstance(), "redirect", Boolean.class);
        return viewPage(o, redirect);
    }

    public String editPage() {
        String url = viewPage();
        url = url.replace("view.jsf", "edit.jsf");
        return url;
    }


    public String viewPage(Object o, Boolean redirect) {
        if (o == null) {
        }
        if (redirect == null) {
        }
        ViewPageProcessor p = new ViewPageProcessor();
        String url = p.setTarget(o).processAnnotation();
        if (redirect == null || Boolean.TRUE.equals(redirect)) {
            url += "&faces-redirect=true";
        }
        return url;
    }

    public String editPage(Object o, Boolean redirect) {
        String url = viewPage(o, redirect);
        url = url.replace("view.jsf", "edit.jsf");
        return url;
    }
}
