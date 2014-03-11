package org.apx.repo.utils;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 16:34
 * To change this template use File | Settings | File Templates.
 */
public enum SortOrder {
    ASC("ASC"),
    DESC("DESC");

    String key;

    SortOrder(String k){
        key = k;
    }

    public String key(){
        return key;
    }
}
