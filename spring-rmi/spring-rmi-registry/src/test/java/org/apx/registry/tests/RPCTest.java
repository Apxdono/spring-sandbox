package org.apx.registry.tests;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import org.apx.registry.RegistryService;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.rmi.Remote;

/**
 * Created by oleg on 3/30/14.
 */

public class RPCTest {

	@Test
	public void testRPC() throws Throwable {
		JsonRpcHttpClient client = new JsonRpcHttpClient(
				new URL("http://127.0.0.1/registry.json"));

		Boolean result = client.invoke("bind", new Object[]{"alala", null}, Boolean.class);




		System.out.println(result);
	}
}
