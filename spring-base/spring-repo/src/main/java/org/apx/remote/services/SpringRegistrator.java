package org.apx.remote.services;

import org.apx.interaction.Registrator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 08.04.14
 * Time: 19:07
 * To change this template use File | Settings | File Templates.
 */

@Component
@Scope("Singleton")
public class SpringRegistrator extends Registrator implements InitializingBean {

    @Autowired
    HelloService helloService;

    @Override
    public void init() {
        addCandidate(HelloService.class,helloService);
    }
}
