package org.apx.repo;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 06.03.14
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CommonRepo {

    EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public Object count(){
        String query = "SELECT Count(1) from task";
        return em.createNativeQuery(query).getSingleResult();
    }
}
