package org.apx.registry;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Starts registry on a specified port
 */
public class Main {
    public static int PORT = 1199;

    public static void main(String[] args) throws RemoteException {
        LocateRegistry.createRegistry(PORT);
        while (true){

        }
    }
}
