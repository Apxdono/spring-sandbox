package org.apx.web.component.model;

import org.apx.model.DBObject;
import org.apx.repo.ICommonRepo;
import org.apx.repo.utils.Paging;
import org.apx.repo.utils.Parameters;
import org.apx.repo.utils.Sorting;
import org.apx.utils.ELUtils;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
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
    static final Logger LOG = LoggerFactory.getLogger(LazyModel.class);
    ICommonRepo repo;
    Class<T> entityClass;
    DataTable tableRef;

    public DataTable getTableRef() {
        return tableRef;
    }

    public void setTableRef(DataTable tableRef) {
        this.tableRef = tableRef;
    }

    public LazyModel(ICommonRepo repository,Class<T> eClass){
        repo = repository;
        entityClass = eClass;
    }

    @Override
    public List<T> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, String> filters) {

        List<T> result = repo.getResultList(entityClass, null, null, Paging.init(first/pageSize,pageSize));
        setWrappedData(result);
        setRowCount((int) repo.getResultCount(entityClass));
        load(null,null,null,null);
        return (List<T>) getWrappedData();
    }


    protected void load(String query,Parameters parameters,Sorting sort, Paging paging){
        processFilterColumns();

    }

    protected void processFilterColumns(){
        LOG.info("TableRef: {}",tableRef);
        List<UIComponent> cols = tableRef.getChildren();
        for (UIComponent col : cols) {
            String headerText = ELUtils.valueExprContent(col,"headerText");
            String filterExpr = ELUtils.valueExprContent(col,"filterBy");
            String filterMatchMode = ELUtils.valueExprContent(col,"filterMatchMode");
            LOG.info("Column '{}', field '{}', match mode '{}'",headerText,filterExpr,filterMatchMode);
        }

    }


}
