package org.apx.web.component.converters;

import org.apache.log4j.Level;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 04.04.14
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */

@FacesConverter(forClass = Level.class)
@RequestScoped
@ManagedBean
public class Log4jLevelConverter implements Converter {
    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String s) {

        return Level.toLevel(s);
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        return o.toString();
    }
}
