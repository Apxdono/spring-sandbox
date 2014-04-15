package org.apx.interaction;

import org.apx.interaction.annotation.Registered;
import org.apx.interaction.enums.InteractionPropKeys;
import org.apx.interaction.enums.RemoteMessage;
import org.apx.interaction.enums.RemoteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.Serializable;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * Maintains a connection to RMI registry. Scans for @Registered annotations on your beans<br/>
 * and registers them there. If a connection to registry was lost will poll until the registry is up again<br/>
 * and reregister beans there. Settings can be changes in interaction.properties file. The keys are specified in InteractionPropKeys.<br/>
 * Can be used to find proxies for required interfaces, register/deregister classes at runtime.
 * @see InteractionPropKeys
 * @see Registered
 */

@Component
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class RegistryManager implements Serializable {
    private static final long serialVersionUID = 6349487206550409439L;
    static Logger LOG = LoggerFactory.getLogger(RegistryManager.class);
    static int RETRY_TIMEOUT = 10;
    static int BEAN_PORT = 1299;
    private static RegistryManager INSTANCE;

    @Autowired
    @Qualifier("registrySettings")
    Properties registrySettings;

    @Autowired
    @Qualifier("taskManager")
    TaskScheduler taskManager;

    @Autowired
    private ApplicationContext applicationContext;

    boolean taskScheduled = false;

    Map<Class, Remote> registeredClasses = new HashMap<Class, Remote>();

    Registry registry;

    /**
     * Get singleton instance of this bean. Handy for non spring beans etc.
     * @return reference to RegistryManager.
     */
    public static RegistryManager instance() {
        return INSTANCE;
    }

    /**
     * Check if connection to registry is up and running.<br/>
     * Will trigger registry polling if connection is down (See parameter {@link InteractionPropKeys#TIMEOUT_KEY}).
     * @return true if everything is ok, false otherwise.
     */
    public boolean isActive() {
        if (registry == null || taskScheduled) {
            return false;
        }
        try {
            registry.list();
        } catch (RemoteException e) {
            LOG.error("Connection to registry is lost.");
            locateRegistryLater();
            synchronized (this) {
                taskScheduled = true;
            }
            return false;
        }
        return true;
    }



    /**
     * List all registered bean interface names
     * @return RemoteResponse with RemoteMessage.OK and list of bean names. Also returns response with message RemoteMessage.REGISTRY_NOT_FOUND and empty array.
     * @see RemoteMessage
     * @see RemoteResponse
     */
    public RemoteResponse<String[]> list(){
        String[] result = null;
        if (!isActive()) {
            return new RemoteResponse<String[]>().message(RemoteMessage.REGISTRY_NOT_FOUND).result(new String[0]);
        }

        try {
            result = registry.list();
        } catch (RemoteException e) {
            return new RemoteResponse<String[]>().message(RemoteMessage.REGISTRY_NOT_FOUND).result(new String[0]);
        }
        return new RemoteResponse<String[]>().message(RemoteMessage.OK).result(result);
    }


    /**
     * Get proxy for this interface
     *
     * @param interfaceClass proxy interface class
     * @param <E>
     * @return RemoteResponse<E> with RemoteMessage.OK if found with non null result.<br/>
     *         Also returns RemoteMessage.REGISTRY_NOT_FOUND, RemoteMessage.EMPTY_PARAMETER,<br/>
     *         RemoteMessage.INTERNAL_ERROR, RemoteMessage.RESOURCE_NOT_FOUND  with null result
     * @see RemoteMessage
     * @see RemoteResponse
     */
    public <E extends Remote> RemoteResponse<E> proxy(Class<E> interfaceClass) {
        if (!isActive()) {
            return new RemoteResponse<E>().message(RemoteMessage.REGISTRY_NOT_FOUND).result(null);
        }

        if (interfaceClass == null) {
            return new RemoteResponse<E>().message(RemoteMessage.EMPTY_PARAMETER).result(null);
        }

        E remote = null;
        try {
            remote = (E) registry.lookup(interfaceClass.getCanonicalName());
        } catch (RemoteException e) {
            LOG.error("Unable to lookup bean with name '{}' ", interfaceClass, e);
            return new RemoteResponse<E>().message(RemoteMessage.INTERNAL_ERROR).result(null);
        } catch (NotBoundException e) {
            LOG.error("Unable to lookup bean with name '{}'. Bean is not found in registry.", interfaceClass, e);
            return new RemoteResponse<E>().message(RemoteMessage.RESOURCE_NOT_FOUND).result(null);
        }

        return new RemoteResponse<E>().message(RemoteMessage.OK).result(remote);
    }


    /**
     * Register bean in current RMI registry.<br></br>
     *
     * @param interfaceClass RMI name will be a canonical name of this object
     * @param bean will be exported for remote calls. Keep in mind this object should not implement UnicastRemoteObject class.
     * @return RemoteResponse with RemoteMessage.OK and true as result.<br/>
     * Also returns RemoteMessage.REGISTRY_NOT_FOUND, RemoteMessage.EMPTY_PARAMETER (if one of parameters is null),<br/>
     * RemoteMessage.NOT_A_REMOTE_OBJECT (if object doesn't implement Remote interface), RemoteMessage.INTERNAL_ERROR (if any other error occurred, check logs) with false result
     * @see RemoteMessage
     * @see RemoteResponse
     * @see Remote
     * @see UnicastRemoteObject
     */
    public RemoteResponse<Boolean> register(Class<? extends Remote> interfaceClass, Object bean) {
        if (!isActive()) {
            return new RemoteResponse<Boolean>().message(RemoteMessage.REGISTRY_NOT_FOUND).result(false);
        }

        if (interfaceClass == null || bean == null) {
            return new RemoteResponse<Boolean>().message(RemoteMessage.EMPTY_PARAMETER).result(false);
        }

        Remote actual = null;
        try {
            if (AopUtils.isCglibProxy(bean) || AopUtils.isAopProxy(bean) || AopUtils.isJdkDynamicProxy(bean)) {
                actual = (Remote) ((Advised) bean).getTargetSource().getTarget();
            } else {
                if (bean instanceof Remote) {
                    actual = (Remote) bean;
                } else {
                    return new RemoteResponse<Boolean>().message(RemoteMessage.NOT_A_REMOTE_OBJECT).result(false);
                }
            }
        } catch (Exception e) {
            LOG.error("Unable to bind bean '{}' as '{}'", bean, interfaceClass, e);
            return new RemoteResponse<Boolean>().message(RemoteMessage.INTERNAL_ERROR).result(false);
        }

        try {
            UnicastRemoteObject.unexportObject(actual, true);
        } catch (NoSuchObjectException e) {

        }
        try {
            Remote stub = UnicastRemoteObject.exportObject(actual, BEAN_PORT);
            registry.rebind(interfaceClass.getCanonicalName(), stub);
            synchronized (this) {
                registeredClasses.put(interfaceClass, actual);
            }
        } catch (RemoteException e) {
            LOG.error("Unable to bind bean '{}' as '{}'", bean, interfaceClass, e);
            return new RemoteResponse<Boolean>().message(RemoteMessage.INTERNAL_ERROR).result(true);
        }

        return new RemoteResponse<Boolean>().message(RemoteMessage.OK).result(true);
    }

    /**
     * Deregisteres bean with given interface in RMI registry
     * @param interfaceClass bean name will be defined as canonical name of this parameter
     * @return RemoteResponse with RemoteMessage.OK and true as result.<br/>
     * Also returns RemoteMessage.REGISTRY_NOT_FOUND, RemoteMessage.EMPTY_PARAMETER (if parameters is null),<br/>
     * RemoteMessage.RESOURCE_NOT_FOUND (if no bean was registered with given name), RemoteMessage.INTERNAL_ERROR (if any other error occurred, check logs) with false result
     */
    public RemoteResponse<Boolean> deregister(Class<? extends Remote> interfaceClass) {
        if (!isActive()) {
            return new RemoteResponse<Boolean>().message(RemoteMessage.REGISTRY_NOT_FOUND).result(false);
        }

        if (interfaceClass == null) {
            return new RemoteResponse<Boolean>().message(RemoteMessage.EMPTY_PARAMETER).result(false);
        }

        try {
            if (!Arrays.asList(registry.list()).contains(interfaceClass.getName())) {
                return new RemoteResponse<Boolean>().message(RemoteMessage.OK).result(true);
            }
        } catch (RemoteException e) {
            LOG.error("Connection to registry lost.", e);
            return new RemoteResponse<Boolean>().message(RemoteMessage.REGISTRY_NOT_FOUND).result(false);
        }

        try {
            Remote stub = registeredClasses.get(interfaceClass);
            if (stub != null) {
                UnicastRemoteObject.unexportObject(stub, true);
            }
            synchronized (this) {
                registeredClasses.remove(interfaceClass);
            }
            registry.unbind(interfaceClass.getCanonicalName());
            LOG.debug("Deregistered bean '{};",interfaceClass);
        } catch (RemoteException e) {
            LOG.error("Unable to unbind bean with name '{}' ", interfaceClass, e);
            return new RemoteResponse<Boolean>().message(RemoteMessage.INTERNAL_ERROR).result(true);
        } catch (NotBoundException e) {
            LOG.error("Unable to unbind bean with name '{}'. Bean is not found in registry.", interfaceClass, e);
            return new RemoteResponse<Boolean>().message(RemoteMessage.RESOURCE_NOT_FOUND).result(true);
        }

        return new RemoteResponse<Boolean>().message(RemoteMessage.OK).result(true);
    }


    protected boolean isActiveSimple() {
        if (registry == null || taskScheduled) {
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
    public void init() {
        LOG.debug("Initializing Registry Manager");
        String timeout = registrySettings.getProperty(InteractionPropKeys.TIMEOUT_KEY);
        String bPort = registrySettings.getProperty(InteractionPropKeys.BEAN_PORT_KEY);
        int beanPort = -1;
        final RegistryManager self = this;
        try {
            beanPort = Integer.parseInt(bPort);
        } catch (NumberFormatException nfe) {
            LOG.error("Invalid export bean port '{}'. Switching to default.", bPort);
        }

        synchronized (this) {
            INSTANCE = this;
            if (timeout != null && !"".equals(timeout)) {
                RETRY_TIMEOUT = Integer.parseInt(timeout);
            }
            if (beanPort > 0) {
                BEAN_PORT = beanPort;
            }
        }

        locateRegistry();
    }

    @PreDestroy
    public void destroy() {
        deregisterBeans();
        synchronized (this) {
            INSTANCE = null;
        }
        LOG.debug("Destroying Registry Manager");
    }

    protected boolean locateRegistry() {
        if (isActiveSimple()) {
            return true;
        }
        synchronized (this) {
            String host = registrySettings.getProperty(InteractionPropKeys.HOST_KEY);
            int port = Integer.parseInt(registrySettings.getProperty(InteractionPropKeys.PORT_KEY));
            try {
                taskScheduled = false;
                if (!registeredClasses.isEmpty()) {
                    deregisterBeans();
                }
                registry = LocateRegistry.getRegistry(host, port);
                registry.list();
                LOG.debug("RMI Registry found");
                registerBeans();
                return true;
            } catch (RemoteException e) {
                LOG.error(RemoteMessage.REGISTRY_NOT_FOUND.getMessage());
                locateRegistryLater();
                taskScheduled = true;
            }
            return false;
        }
    }


    protected void locateRegistryLater() {

        if (taskScheduled) {
            LOG.warn("Reconnect task already scheduled. Skipping.");
            return;
        }

        LOG.debug("Will try to reconnect to RMI registry in {} second(s)", RETRY_TIMEOUT);
        Date d = new Date();
        Date schedueled = new Date(d.getTime() + RETRY_TIMEOUT * 1000);
        taskManager.schedule(new PeriodicReconnect(this), schedueled);
    }


    protected class PeriodicReconnect implements Runnable {
        Logger log = LoggerFactory.getLogger(getClass());
        RegistryManager manager;

        public PeriodicReconnect(RegistryManager m) {
            manager = m;
        }

        @Override
        public void run() {
            log.debug("Retrying connection to RMI Registry");
            manager.locateRegistry();
        }
    }


    protected void registerBeans() {
        Map<String, Object> candidates = applicationContext.getBeansWithAnnotation(Registered.class);
        registeredClasses.clear();
        if (candidates.isEmpty()) {
            return;
        }

        LOG.debug("Registering {} bean(s) in remote registry", candidates.size());
        for (Map.Entry<String, Object> candidate : candidates.entrySet()) {
            Registered ann = applicationContext.findAnnotationOnBean(candidate.getKey(), Registered.class);
            Object o = candidate.getValue();
            RemoteResponse<Boolean> resp = this.register(ann.interfaceClass(), o);
            if (!resp.getMessage().equals(RemoteMessage.OK)) {
                continue;
            }
        }
    }

    protected void deregisterBeans() {
        LOG.debug("Deregistering beans");
        for (Map.Entry<Class, Remote> entry : registeredClasses.entrySet()) {
            try {
                UnicastRemoteObject.unexportObject(entry.getValue(), true);
                registry.unbind(entry.getKey().getCanonicalName());
                LOG.info("Deregistered bean {}",entry.getKey());
            } catch (RemoteException e) {
                LOG.error("Error during bean {} deregistration. Possibly registry was shut down",entry.getKey());
            } catch (NotBoundException e) {
                LOG.error("Error during bean deregistration. Bean {} was not registered.",entry.getKey());
            }
        }
        registeredClasses.clear();
    }
}
