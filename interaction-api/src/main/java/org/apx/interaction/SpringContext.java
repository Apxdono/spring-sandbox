package org.apx.interaction;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 08.04.14
 * Time: 18:48
 * To change this template use File | Settings | File Templates.
 */
@Component
@Scope("singleton")
class SpringContext implements ApplicationContextAware, InitializingBean {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.print("fuck yeah");
    }
}
