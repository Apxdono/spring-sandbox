package org.apx.repo.handler;

import org.apx.repo.CommonRepo;

import java.io.Serializable;

/**
 * Created by oleg on 3/13/14.
 */
public interface IDBHandler<T> extends Serializable {
	void setContext(CommonRepo repository);
	T process(T entity);
}
