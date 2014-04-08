package org.apx.interaction;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 08.04.14
 * Time: 18:42
 * To change this template use File | Settings | File Templates.
 */
public abstract class ProxyUtils {

    public static <E extends Remote> E lookup(Class<E> interfaceClass) throws RemoteException, NotBoundException {
        RegistryResolver resolver = (RegistryResolver) SpringContext.getApplicationContext().getBean("RegistryResolver");
        return resolver.getProxy(interfaceClass);
    }

    public static void bind(Class<? extends Remote> interfaceClass, Remote object) throws RemoteException {
        RegistryResolver resolver = (RegistryResolver) SpringContext.getApplicationContext().getBean("RegistryResolver");
        resolver.bind(interfaceClass,object);
    }
}
