package org.apx.remote.services;

import org.apx.interaction.ProxyUtils;
import org.apx.interaction.RegistryManager;
import org.apx.interaction.annotation.Registered;
import org.apx.interaction.enums.RemoteResponse;
import org.apx.model.User;
import org.apx.repo.CommonRepo;
import org.apx.repo.ICommonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
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
        return (int) repo.getResultCount(User.class);
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public boolean createCityAndUser(boolean fail) throws RemoteException {
        User u = new User();
        u.setDisplayName("Ахалай Махалай" + (!fail ? "1" : "2"));
        u.setInternalName("a.mahalay"+(!fail ? "1" : "2"));
        u = repo.save(u);

        CityService cs = RegistryManager.instance().proxy(CityService.class).getResult();

        if(cs != null){
            if(fail){
                cs.createCityRollback();
            } else {
                cs.createCity();
            }
        }


        return u != null;
    }
}
