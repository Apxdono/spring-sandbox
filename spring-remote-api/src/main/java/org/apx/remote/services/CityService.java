package org.apx.remote.services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 22.05.14
 * Time: 11:42
 * To change this template use File | Settings | File Templates.
 */
public interface CityService extends Serializable , Remote {
    public boolean createCity() throws RemoteException;
    public boolean createCityRollback() throws RemoteException;

}
