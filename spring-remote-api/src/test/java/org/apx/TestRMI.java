package org.apx;

import org.apx.remote.services.HelloService;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.03.14
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class TestRMI {

    @Test
    public void testRMI() throws RemoteException, NotBoundException {
        String name = "HelloService";
        Registry registry = LocateRegistry.getRegistry("localhost",1299);
        HelloService serv = (HelloService) registry.lookup(name);
        System.out.println("-Hello! -"+serv.sayHello());
        System.out.println("Users count: "+serv.countUsers());
    }
}
