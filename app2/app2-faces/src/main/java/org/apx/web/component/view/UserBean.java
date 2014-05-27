package org.apx.web.component.view;

import org.apx.model.User;
import org.apx.repo.utils.Sorting;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Created by oleg on 3/12/14.
 */
@ManagedBean
@ViewScoped
public class UserBean extends BaseViewBean<User> {

	List<User> model;

	public List<User> getModel() {
		if(model == null){
			model = repo.getResultList(User.class, "", null, Sorting.init("displayName"));
		}
		return model;
	}

	public void setModel(List<User> model) {
		this.model = model;
	}
}
