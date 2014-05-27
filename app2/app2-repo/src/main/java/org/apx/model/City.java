package org.apx.model;

import org.apx.model.DBObject;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 22.05.14
 * Time: 11:19
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "cities")
public class City extends DBObject {
    private static final long serialVersionUID = -7515026142495614148L;

}
