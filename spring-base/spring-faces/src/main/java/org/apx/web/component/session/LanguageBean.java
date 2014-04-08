package org.apx.web.component.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by oleg on 4/6/14.
 */
@ManagedBean(name = "language")
@SessionScoped
public class LanguageBean implements Serializable {
	private static final long serialVersionUID = -1668636863042191847L;
    static Logger LOG = LoggerFactory.getLogger(LanguageBean.class);

    private Locale locale;

    @PostConstruct
    public void init(){
        locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("locale",locale);
        LOG.debug("Setting session locale to {}",locale);
    }

    public Locale getLocale() {
        return locale;
    }

    public String getLanguage() {
        return locale.getLanguage();
    }

    public void setLanguage(String language) {
        this.locale = new Locale(language);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("locale",locale);
        LOG.debug("Setting locale to {}",locale);
    }
}
