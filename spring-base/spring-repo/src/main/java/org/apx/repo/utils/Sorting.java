package org.apx.repo.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 16:33
 * To change this template use File | Settings | File Templates.
 */
public class Sorting implements IQueryProcessor<String> {
    Map<String,SortOrder> sorting;

    public static Sorting init(String field){
        return init(field,null);
    }

    public static Sorting init(String field, SortOrder order){
        Sorting s = new Sorting();
        s.put(field,order);
        return s;
    }

    public Sorting(){
        sorting = new LinkedHashMap<String, SortOrder>();
    }

    public Sorting put(String field){
        put(field,SortOrder.ASC);
        return this;
    }

    public Sorting put(String field, SortOrder order){
        if(StringUtils.isBlank(field)){
            return this;
        }
        if(order == null){
            order = SortOrder.ASC;
        }
        sorting.put(field,order);
        return this;
    }

    @Override
    public String processQuery(String queryString) {
        if(StringUtils.isBlank(queryString)){
            return queryString;
        }
        StringBuilder sb = new StringBuilder(queryString);
        if(!sorting.isEmpty()){
            sb.append(" ORDER BY ");
            for (Map.Entry<String, SortOrder> entry : sorting.entrySet()) {
                sb.append(entry.getKey()).append(" ").append(entry.getValue().key()).append(", ");
            }
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
}
