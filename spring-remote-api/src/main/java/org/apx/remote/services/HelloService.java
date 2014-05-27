package org.apx.remote.services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Simple remote service
 */
public interface HelloService extends Remote, Serializable {
    String sayHello() throws RemoteException;
    int countUsers() throws RemoteException;
    boolean createCityAndUser(boolean fail) throws RemoteException;
}
