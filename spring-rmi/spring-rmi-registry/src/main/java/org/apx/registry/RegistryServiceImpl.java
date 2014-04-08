package org.apx.registry;

import com.googlecode.jsonrpc4j.JsonRpcParamName;
import com.googlecode.jsonrpc4j.JsonRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.remoting.rmi.RmiRegistryFactoryBean;
import org.springframework.stereotype.Component;

import java.rmi.Remote;

/**
 * Created by oleg on 3/30/14.
 */
@Component(value = "registryService")
public class RegistryServiceImpl implements RegistryService {

	@Override
	public <K extends Remote> boolean bind(String url, Class<K> beanInterface) {

		System.out.println("BINDING BEAN");
		return true;
	}

	@Override
	public <K extends Remote> boolean unbind(String url, Class<K> beanInterface) {
		System.out.println("UNBINDING BEAN");
		return true;
	}
}
