package org.apx.remote.services;

import org.apx.repo.CommonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.03.14
 * Time: 16:43
 * To change this template use File | Settings | File Templates.
 */
@Component(value = "HelloService")
public class HelloServiceImpl implements HelloService {

    private static final long serialVersionUID = 8330289415699988583L;
    @Autowired
    CommonRepo repo;

    @Override
    public String sayHello() throws RemoteException {
        return "HELLO BEACHE!";
    }

    @Override
    @Transactional(readOnly = true)
    public int countUsers() throws RemoteException {
        Number n = (Number) repo.createQuery(Number.class,"SELECT COUNT(i) FROM User i",null,null,null).getSingleResult();
        return n.intValue();
    }
}
