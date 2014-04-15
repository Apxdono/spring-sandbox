package org.apx.remote.services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 15.04.14
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
public interface GreetingsService  extends Remote, Serializable {
    String sayHi() throws RemoteException;
}
