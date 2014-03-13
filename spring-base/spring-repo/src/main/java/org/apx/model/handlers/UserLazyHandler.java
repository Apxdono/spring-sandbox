package org.apx.model.handlers;

import org.apx.model.User;
import org.apx.repo.CommonRepo;
import org.apx.repo.handler.IDBHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by oleg on 3/13/14.
 */
public class UserLazyHandler implements IDBHandler<User> {

	static Logger LOG = LoggerFactory.getLogger(UserLazyHandler.class);
	CommonRepo repo;

	@Override
	public void setContext(CommonRepo repository) {
		repo = repository;
	}

	@Override
	public User process(User entity) {
		LOG.debug("Called handler successfully");
		return entity;
	}
}
