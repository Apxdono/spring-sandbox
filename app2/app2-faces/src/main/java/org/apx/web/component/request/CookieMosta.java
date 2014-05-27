package org.apx.web.component.request;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 02.04.14
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class CookieMosta implements Serializable {
    private static final long serialVersionUID = 2571294968259438955L;

    public boolean isMenuVisible(){
        Cookie c = (Cookie) FacesContext.getCurrentInstance().getExternalContext().getRequestCookieMap().get("sideMenuVisible");
        if(c == null){
            return true;
        }
        Boolean b = Boolean.valueOf(c.getValue());
        return b;
    }
}
