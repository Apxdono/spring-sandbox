package org.apx.registry;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Scope;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import org.springframework.stereotype.Component;

import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by oleg on 3/30/14.
 */
public class RegistryInitializer extends RmiRegistryFactoryBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.setSecurityManager(new RMISecurityManager());
        super.afterPropertiesSet();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
