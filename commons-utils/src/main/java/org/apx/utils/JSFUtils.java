package org.apx.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 28.03.14
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class JSFUtils {

    public static <E> E getAttribute(FacesContext context, String attributeKey,Class<E> klazz){
        Object o = UIComponent.getCurrentComponent(context).getAttributes().get(attributeKey);
        return (E) o;
    }

}
