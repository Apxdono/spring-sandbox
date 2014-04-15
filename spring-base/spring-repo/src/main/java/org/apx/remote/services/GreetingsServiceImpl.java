package org.apx.remote.services;

import org.apx.interaction.annotation.Registered;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 15.04.14
 * Time: 11:11
 * To change this template use File | Settings | File Templates.
 */
@Component(value = "greetingsService")
@Registered(interfaceClass = GreetingsService.class)
public class GreetingsServiceImpl implements GreetingsService {

    private static final long serialVersionUID = -1630960015047199777L;

    @Override
    public String sayHi() throws RemoteException{
        return "Welcome un bienvenu";
    }
}
