package org.apx.interaction;

import org.springframework.beans.factory.InitializingBean;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 08.04.14
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public abstract class Registrator implements InitializingBean {

    private Map<Class<? extends Remote>, Remote> queue = new HashMap<Class<? extends Remote>, Remote>();

    public void addCandidate(Class<? extends Remote> interfaceClass, Remote object){
        queue.put(interfaceClass,object);
    }

    public abstract void init();

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        registerCandidates();
    }

    protected void registerCandidates() throws RemoteException {
        for (Map.Entry<Class<? extends Remote>, Remote> candidate : queue.entrySet()) {
            ProxyUtils.bind(candidate.getKey(),candidate.getValue());
        }
    }
}
