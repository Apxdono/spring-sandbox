package org.apx.repo;

import org.apache.commons.lang3.StringUtils;
import org.apx.repo.handler.HandlerProcessor;
import org.apx.repo.utils.IQueryProcessor;
import org.apx.repo.utils.Paging;
import org.apx.repo.utils.Parameters;
import org.apx.repo.utils.Sorting;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 06.03.14
 * Time: 18:09
 * To change this template use File | Settings | File Templates.
 */
@Repository
@Transactional(readOnly = true)
public class CommonRepo {

    EntityManager em;

    @PersistenceContext
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public boolean checkConnection() {
        String query = "SELECT 1";
        String expected = "1";
        Object res = em.createNativeQuery(query).getSingleResult();
        if (res == null) return false;
        return StringUtils.equals(expected, res.toString());
    }

	public <E> E find(Object id,Class<E> klass){
		return find(id, klass,false);
	}

	public <E> E find(Object id,Class<E> klass, boolean loadLazy){
		E result = em.find(klass,id);
		if(loadLazy){
			result = HandlerProcessor.processLazyObjects(result,this);
		}
		return result;
	}

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED)
    public <E> E save(E entity) {
        em.persist(entity);
        return entity;
    }

	@Transactional(readOnly = false,propagation = Propagation.REQUIRED)
	public <E> E update(E entity) {
		entity = em.merge(entity);
		return entity;
	}



    public <E> List<E> getResultList(Class<E> klass) {
        return getResultList(klass, null, null);
    }

    public <E> List<E> getResultList(Class<E> klass, String query, Parameters parameters){
        return getResultList(klass, query, parameters, null, null);
    }

    @Transactional(readOnly = true)
    public <E> List<E> getResultList(Class<E> klass, String query, Parameters parameters, Sorting
            sort){
        return getResultList(klass, query, parameters, null, sort);
    }

    @Transactional(readOnly = true)
    public <E> List<E> getResultList(Class<E> klass, String query, Parameters parameters, Paging paging) {
        return getResultList(klass, query, parameters, paging, null);
    }

    public <E> List<E> getResultList(Class<E> klass, String query, Parameters parameters, Paging paging, Sorting
            sort) {
        List<E> result = createQuery(klass,query,parameters,paging,sort).getResultList();
        return result != null ? result : Collections.<E>emptyList();
    }

    public <E> Query createQuery(Class<E> klass, String query, Parameters parameters, Paging paging, Sorting
            sort) {
        if (klass == null && StringUtils.isBlank(query)) {
            return null;
        }

        if (StringUtils.isBlank(query)) {
            query = new StringBuilder("SELECT i FROM ").append(klass.getCanonicalName()).append(" i").toString();
        }

        query = invokeProcessor(sort,query);

        Query q = em.createQuery(query);
        q = invokeProcessor(parameters,q);
        q = invokeProcessor(paging,q);

        return q;
    }

    /**
     * Invokes a specified processor and produces a valid result
     * @param processor processor to invoke (null safe)
     * @param target processor's target
     * @param <K> resulting type
     * @return processor response. May be null if processor's target is null, or invalid.
     */
    protected <K> K invokeProcessor(IQueryProcessor<K> processor, K target){
        if(processor == null){
            return target;
        }
        return processor.processQuery(target);
    }

}
