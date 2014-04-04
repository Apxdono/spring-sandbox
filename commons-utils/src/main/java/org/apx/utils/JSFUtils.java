package org.apx.utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 28.03.14
 * Time: 15:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class JSFUtils {


    public static <E> E currentComponent(Class<E> klass){
        return currentComponent(FacesContext.getCurrentInstance(), klass);
    }

    public static <E> E currentComponent(FacesContext ctx, Class<E> klass){
        return (E) UIComponent.getCurrentComponent(ctx);
    }

    public static <E> E getAttribute(FacesContext context, String attributeKey,Class<E> klazz){
        Object o = UIComponent.getCurrentComponent(context).getAttributes().get(attributeKey);
        return (E) o;
    }

    public static void redirect(FacesContext fc, String redirectPage){
        try {
            if(!fc.isPostback()){
                fc.getExternalContext().redirect(redirectPage);
            } else {
                HttpServletResponse resp = (HttpServletResponse) fc.getExternalContext().getResponse();
                resp.resetBuffer();
                resp.getWriter().append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                        .append(String.format("<partial-response><redirect url=\"%s\"></redirect></partial-response>", redirectPage));
                resp.
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
