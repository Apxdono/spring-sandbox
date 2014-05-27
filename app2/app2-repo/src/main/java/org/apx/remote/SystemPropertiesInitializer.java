package org.apx.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.rmi.RMISecurityManager;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 15.03.14
 * Time: 22:14
 * To change this template use File | Settings | File Templates.
 */

public class SystemPropertiesInitializer {

    static Logger LOG = LoggerFactory.getLogger(SystemPropertiesInitializer.class);

    public SystemPropertiesInitializer(){
        LOG.info("Initializing system properties");
//        System.setSecurityManager(new RMISecurityManager());
    }
}
