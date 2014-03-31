package org.apx.registry;

import java.rmi.Remote;

/**
 * Created by oleg on 3/30/14.
 */
public interface RegistryService {
	<K extends Remote> boolean bind(String url, Class<K> beanInterface);
	<K extends Remote> boolean unbind(String url, Class<K> beanInterface);

}
