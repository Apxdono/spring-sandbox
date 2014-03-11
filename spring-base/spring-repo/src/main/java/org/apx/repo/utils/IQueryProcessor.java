package org.apx.repo.utils;

import javax.persistence.Query;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 11.03.14
 * Time: 16:28
 * To change this template use File | Settings | File Templates.
 */
public interface IQueryProcessor<T> extends Serializable {
    T processQuery(T object);

}
