package org.apx.web.component.view;

import org.apache.commons.lang3.StringUtils;
import org.apx.model.DBObject;
import org.apx.repo.CommonRepo;
import org.apx.repo.ICommonRepo;
import org.apx.utils.ReflectionUtils;
import org.apx.web.component.model.LazyModel;
import org.apx.web.component.request.NavigationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
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
    ICommonRepo repo;

    @Inject
    NavigationBean nav;

    LazyModel<T> lazyModel;

    @PostConstruct
    public void init() {
        logger.debug("Initialized bean '{}'", toString());
        entityClass = (Class<T>) ReflectionUtils.getParameterClassSimple(getClass(), 0);
        processParameters();
        if (entity == null) {
            entity = ReflectionUtils.newInstance(entityClass);
        }
    }

    protected void processParameters() {
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (StringUtils.isNotBlank(id)) {
            entity = repo.find(id,entityClass);
        } else {

        }

    }

    public LazyModel<T> getLazyModel() {
        if(lazyModel == null){
            lazyModel = new LazyModel<T>(repo,entityClass);
        }

        return lazyModel;
    }

    public void setLazyModel(LazyModel<T> lazyModel) {
        this.lazyModel = lazyModel;
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public String save() {
        repo.save(entity);
        return nav.listPage();
    }

    public String update() {
        repo.update(entity);
        return nav.viewPage(entity,true);
    }

    public void delete(){
        entity.setActive(false);
        repo.update(entity);
    }

}
