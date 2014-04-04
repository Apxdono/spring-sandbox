package org.apx.repo;

import org.apx.repo.utils.Paging;
import org.apx.repo.utils.Parameters;
import org.apx.repo.utils.Sorting;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

/**
 * Created by oleg on 3/16/14.
 */
public interface ICommonRepo extends Serializable {
	@PersistenceContext
	void setEm(EntityManager em);

	boolean checkConnection();

	<E> E find(Object id, Class<E> klass);

	<E> E find(Object id, Class<E> klass, boolean loadLazy);

	@Transactional(readOnly = false,propagation = Propagation.REQUIRED)
	<E> E save(E entity);

	@Transactional(readOnly = false,propagation = Propagation.REQUIRED)
	<E> E update(E entity);

	<E> List<E> getResultList(Class<E> klass);

	<E> List<E> getResultList(Class<E> klass, String query, Parameters parameters);

	@Transactional(readOnly = true)
	<E> List<E> getResultList(Class<E> klass, String query, Parameters parameters, Sorting
			sort);

	@Transactional(readOnly = true)
	<E> List<E> getResultList(Class<E> klass, String query, Parameters parameters, Paging paging);

	<E> List<E> getResultList(Class<E> klass, String query, Parameters parameters, Paging paging, Sorting
			sort);

    long getResultCount(Class<?> klass);

    long getResultCount(Class<?> klass, String query, Parameters parameters);




	<E> Query createQuery(Class<E> klass, String query, Parameters parameters, Paging paging, Sorting
			sort);
}
