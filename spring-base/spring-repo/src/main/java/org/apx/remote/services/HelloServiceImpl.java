package org.apx.remote.services;

import org.apx.interaction.annotation.Registered;
import org.apx.repo.CommonRepo;
import org.apx.repo.ICommonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.03.14
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
@Component(value = "HelloService")
@Registered(interfaceClass = HelloService.class)
public class HelloServiceImpl implements HelloService {

    private static final long serialVersionUID = 8330289415699988583L;

    @Autowired
    ICommonRepo repo;

    @Override
    public String sayHello() throws RemoteException {
        return "HELLO BEACHE!";
    }

    @Override
    @Transactional(readOnly = true)
    public int countUsers() throws RemoteException {
        Number n = (Number) repo.createQuery(Number.class,"SELECT COUNT(i) FROM User i",null,null,null).getSingleResult();
        return n.intValue();
//        return 0;
    }
}
