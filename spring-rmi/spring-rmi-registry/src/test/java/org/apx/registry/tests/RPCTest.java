package org.apx.registry.tests;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import org.junit.Test;

import java.net.URL;

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
