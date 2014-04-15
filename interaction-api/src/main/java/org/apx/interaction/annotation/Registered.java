package org.apx.interaction.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.rmi.Remote;

/**
 * Created with IntelliJ IDEA.
 * User: oleg
 * Date: 14.04.14
 * Time: 19:36
 * To change this template use File | Settings | File Templates.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Scope(BeanDefinition.SCOPE_SINGLETON)
public @interface Registered {
    Class<? extends Remote> interfaceClass();
}
