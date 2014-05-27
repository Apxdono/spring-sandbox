package org.apx.repo.utils;

import javax.persistence.Query;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 15:57
 * To change this template use File | Settings | File Templates.
 */
public class Paging implements IQueryProcessor<Query> {

    int page;
    int perPage;

    public static Paging init(int page, int perPage){
        Paging p = new Paging();
        p.setPage(page);
        p.setPerPage(perPage);
        return p;
    }

    protected Paging(){
        page = 0;
        perPage = 0;
    }

    public int getPage() {
        return page;
    }

    public int getPerPage() {
        return perPage;
    }


    public void setPage(int page) {
        this.page = page;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }



    @Override
    public Query processQuery(Query query) {
        if(query == null || perPage == 0){
            return query;
        }
        query.setFirstResult(page*perPage);
        query.setMaxResults(perPage);
        return query;
    }
}
