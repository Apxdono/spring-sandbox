package org.apx.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apx.model.handlers.DBObjectLazyHandler;
import org.apx.model.handlers.UserLazyHandler;
import org.apx.repo.handler.LazyInitHandler;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 14:54
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "users")
@LazyInitHandler(handlerClass = UserLazyHandler.class)
public class User extends DBObject {
	private static final long serialVersionUID = 7724216362592390310L;

}
