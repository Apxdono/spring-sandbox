package org.apx.utils;

import org.apache.commons.lang3.StringUtils;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 04.04.14
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public abstract class ELUtils {

    public static ValueExpression valueExpr(UIComponent comp, String key){
        return comp.getValueExpression(key);
    }

    public static String valueExprContent(UIComponent comp, String key){
        return valueExprContent(valueExpr(comp,key));
    }

    public static String valueExprContent(ValueExpression ve){
        if(ve == null){
            return "";
        }
        String expr = ve.getExpressionString();
        if(StringUtils.isBlank(expr)){
            return "";
        }
        int index = expr.indexOf("#{");
        while (index > -1){
            expr = expr.replace("#{","");
            expr = expr.replace("}","");
            index = expr.indexOf("#{");
        }
        return expr;
    }

}