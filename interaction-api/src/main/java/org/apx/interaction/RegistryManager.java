package org.apx.interaction;

import org.apx.interaction.enums.InteractionPropKeys;
import org.apx.interaction.enums.RemoteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.timer.TimerTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.beans.beancontext.BeanContext;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.04.14
 * Time: 15:32
 * To change this template use File | Settings | File Templates.
 */

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RegistryManager implements Serializable {
    private static final long serialVersionUID = 6349487206550409439L;
    static Logger LOG = LoggerFactory.getLogger(RegistryManager.class);
    static int RETRY_TIMEOUT = 10;
    private static RegistryManager INSTANCE;

    @Autowired @Qualifier("registrySettings")
    Properties registrySettings;

    @Autowired @Qualifier("taskManager")
    ThreadPoolTaskScheduler taskManager;

    Registry registry;

    public static RegistryManager inst(){
        return INSTANCE;
    }

    public boolean isActive(){
        if(registry == null){
            return false;
        }

        try {
            registry.list();
        } catch (RemoteException e) {
            LOG.error("Connection to registry is lost.");
            return false;
        }

        return true;
    }

    @PostConstruct
    public void init(){
        LOG.debug("Initializing Registry Manager");
        INSTANCE = this;
        String timeout = registrySettings.getProperty(InteractionPropKeys.TIMEOUT_KEY);
        if(timeout != null && !"".equals(timeout)){
            RETRY_TIMEOUT = Integer.parseInt(timeout);
        }
        if(locateRegistry()){

        }
    }

    @PreDestroy
    public void destroy(){

    }

    protected boolean locateRegistry(){
        if(isActive()){
            return true;
        }
        synchronized (this){
            String host = registrySettings.getProperty(InteractionPropKeys.HOST_KEY);
            int port = Integer.parseInt(registrySettings.getProperty(InteractionPropKeys.PORT_KEY));
            try {
                registry = LocateRegistry.getRegistry(host, port);
                registry.list();
                LOG.debug("RMI Registry found");
                return true;
            } catch (RemoteException e) {
                LOG.error(RemoteMessage.REGISTRY_NOT_RUNNING);
                locateRegistryLater();
            }
            return false;
        }
    }


    protected void locateRegistryLater(){
        LOG.debug("Will try to reconnect to RMI registry in {} seconds", RETRY_TIMEOUT);
        Date d = new Date();
        Date schedueled = new Date(d.getTime() + RETRY_TIMEOUT*1000);
        taskManager.schedule(new PeriodicReconnect(this), schedueled);
//        taskManager.execute();
    }


    protected class PeriodicReconnect implements Runnable{
        Logger log = LoggerFactory.getLogger(getClass());
        RegistryManager manager;

        public PeriodicReconnect(RegistryManager m){
            manager = m;
        }

        @Override
            public void run() {
            log.debug("Retrying connection to RMI Registry");
            manager.locateRegistry();
        }
    }

}
