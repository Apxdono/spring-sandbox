package org.apx.interaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by oleg on 3/30/14.
 */
@Component
@Scope(value = "singleton")
class RegistryResolver implements Serializable {
	private static final long serialVersionUID = -8193258892322222939L;

    @Value("#{interaction['remote.registy.host']}")
    String registryHost;

    @Value("#{interaction['remote.registy.port']}")
    int registryPort;

    Registry registry;

    private Registry getRegistry() throws RemoteException {
        if(registry == null){
            synchronized (registry){
                registry = LocateRegistry.getRegistry(registryHost,registryPort);
            }
        }

        if(registry == null){
            throw new RemoteException("Could not locate remote registry");
        }

        return registry;
    }

	public void bind(Class<?> interfaceClass, Remote obj) throws RemoteException {
        getRegistry().rebind(interfaceClass.getCanonicalName(), obj);
	}

    public <E extends Remote> E getProxy(Class<E> interfaceClass) throws RemoteException, NotBoundException {
        return (E) getRegistry().lookup(interfaceClass.getCanonicalName());
    }

}
