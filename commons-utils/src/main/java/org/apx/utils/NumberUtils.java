package org.apx.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 16.04.14
 * Time: 12:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class NumberUtils {

    public static double parseDouble(String s, double defaultValue){
        double result = defaultValue;
        if(StringUtils.isNotBlank(s)){
            try{
                s = s.replace(",",".");
                result = Double.parseDouble(s);
            } catch (NumberFormatException e){
                result = defaultValue;
            }
        }
        return result;
    }

    public static int parseInt(String s, int defaultValue){
        int result = defaultValue;
        if(StringUtils.isNotBlank(s)){
            try{
                result = Integer.parseInt(s);
            } catch (NumberFormatException e){
                result = defaultValue;
            }
        }
        return result;
    }
}
