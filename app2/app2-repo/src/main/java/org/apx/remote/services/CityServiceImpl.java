package org.apx.remote.services;

import org.apx.interaction.annotation.Registered;
import org.apx.model.City;
import org.apx.repo.ICommonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 22.05.14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
@Component(value = "CityService")
@Registered(interfaceClass = CityService.class)
public class CityServiceImpl implements CityService {
    private static final long serialVersionUID = 1255635718285444561L;

    @Autowired
    ICommonRepo repo;

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public boolean createCity() throws RemoteException {
        City c = new City();
        c.setDisplayName("1");
        c.setInternalName("1.internal");

        c = repo.save(c);

        return c != null;
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED,rollbackFor = Throwable.class)
    public boolean createCityRollback() throws RemoteException {
        City c = new City();
        c.setDisplayName("2");
        c.setInternalName("2.internal");

        c = repo.save(c);
        if(true){
            throw new NullPointerException("faked an error");
        }
        return c != null;
    }
}
