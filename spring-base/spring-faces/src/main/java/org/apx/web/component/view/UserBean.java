package org.apx.web.component.view;

import org.apx.repo.CommonRepo;
import org.apx.spring.jsf.scopes.ViewScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Created by oleg on 3/12/14.
 */
@Component
@ManagedBean
@ViewScoped
public class UserBean implements Serializable {

	static Logger LOG = LoggerFactory.getLogger(UserBean.class);

	@Inject
	CommonRepo repo;

	@PostConstruct
	public void init(){
		LOG.debug("View bean {} initialized. Ref id : {}",getClass().getSimpleName(),toString());
	}

	public String getUserName(){
		return "ololo";
	}

}
