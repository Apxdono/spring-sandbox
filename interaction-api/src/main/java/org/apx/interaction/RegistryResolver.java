package org.apx.interaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.Property;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

/**
 * Created by oleg on 3/30/14.
 */
@Component
@Scope(value = "singleton")
class RegistryResolver implements Serializable {
	private static final long serialVersionUID = -8193258892322222939L;

    final Set<Class> registered = new HashSet<Class>();

    private class ClientSocketFactory implements RMIClientSocketFactory {
        @Override
        public Socket createSocket(String host, int port) throws IOException {
            return new Socket(host,port);
        }
    }

    @Autowired
    @Qualifier("registrySettings")
    Properties registrySettings;

    Registry registry;

    private Registry getRegistry() throws RemoteException {
        if(registry == null){
            synchronized (this){
                String host = registrySettings.getProperty("remote.registy.host");
                int port = Integer.parseInt(registrySettings.getProperty("remote.registy.port"));
                registry = LocateRegistry.getRegistry(host,port);
            }
        }

        if(registry == null){
            throw new RemoteException("Could not locate remote registry");
        }

        return registry;
    }

	public void bind(Class<?> interfaceClass, Remote obj) throws RemoteException, AlreadyBoundException {
        Remote objStub = UnicastRemoteObject.exportObject(obj, 1099);
        Registry r = getRegistry();
        r.rebind(interfaceClass.getCanonicalName(), objStub);
        registered.add(interfaceClass);
	}

    public <E extends Remote> E getProxy(Class<E> interfaceClass) throws RemoteException, NotBoundException {
        return (E) getRegistry().lookup(interfaceClass.getCanonicalName());
    }

    public void detachRegistry() throws RemoteException, NotBoundException {
        for (Class aClass : registered) {
            getRegistry().unbind(aClass.getCanonicalName());
        }
        registry = null;
    }

}
