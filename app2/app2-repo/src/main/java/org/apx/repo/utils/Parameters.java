package org.apx.repo.utils;

import org.apache.commons.lang3.StringUtils;

import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 16:59
 * To change this template use File | Settings | File Templates.
 */
public class Parameters implements IQueryProcessor<Query> {

    Map<String,Object> params;

    public static Parameters init(String k, Object v){
        Parameters p = new Parameters();
        p.put(k,v);
        return p;
    }

    protected Parameters(){
        params = new HashMap<String, Object>();
    }

    public Parameters put(String k, Object v){
        if(StringUtils.isBlank(k)){
            return this;
        }
        params.put(k,v);
        return this;
    }


    @Override
    public Query processQuery(Query query) {
        if(query == null || params.isEmpty()){
            return query;
        }

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(),entry.getValue());
        }

        return query;
    }
}
