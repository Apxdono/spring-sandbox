package org.apx.web.component.model;

import org.apx.model.DBObject;
import org.apx.repo.CommonRepo;
import org.apx.utils.ReflectionUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 01.04.14
 * Time: 15:52
 * To change this template use File | Settings | File Templates.
 */
public class LazyModel<T extends DBObject> extends LazyDataModel<T> implements Serializable {
    private static final long serialVersionUID = 919113363828105281L;

    CommonRepo repo;
    Class<T> entityClass;

    public LazyModel(CommonRepo repository,Class<T> eClass){
        repo = repository;
        entityClass = eClass;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {


        List<T> result = (List<T>) repo.getResultList(entityClass);
        setWrappedData(result);
        setRowCount(result.size());

        return (List<T>) getWrappedData();
    }
}
