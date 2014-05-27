package org.apx.model.handlers;

import org.apx.model.DBObject;
import org.apx.repo.CommonRepo;
import org.apx.repo.handler.IDBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oleg on 3/13/14.
 */
public class DBObjectLazyHandler implements IDBHandler<DBObject> {

	static Logger LOG = LoggerFactory.getLogger(DBObjectLazyHandler.class);
	CommonRepo repo;

	@Override
	public void setContext(CommonRepo repository) {
		repo = repository;
	}

	@Override
	public DBObject process(DBObject entity) {
		LOG.debug("Called handler successfully");
		return entity;
	}
}
