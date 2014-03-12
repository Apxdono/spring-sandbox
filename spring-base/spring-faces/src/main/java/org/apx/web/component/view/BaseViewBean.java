package org.apx.web.component.view;

import org.apx.model.DBObject;
import org.apx.repo.CommonRepo;
import org.apx.utils.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by oleg on 3/13/14.
 */
public abstract class BaseViewBean<T extends DBObject> implements Serializable {
	private static final long serialVersionUID = 3273486737438981080L;

	Logger logger = LoggerFactory.getLogger(getClass());
	T entity;
	Class<T> entityClass;

	@Inject
	CommonRepo repo;

	@PostConstruct
	public void init(){
		logger.debug("Initialized bean '{}'",toString());
		entityClass = (Class<T>) ReflectionUtils.getParameterClassSimple(getClass(), 0);
		processParameters();
		if(entity == null){
			entity = ReflectionUtils.newInstance(entityClass);
		}
	}

	protected void processParameters(){

	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
}
