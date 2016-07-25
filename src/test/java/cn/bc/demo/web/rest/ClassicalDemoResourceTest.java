package cn.bc.demo.web.rest;

import cn.bc.rest.test.ClassicalJerseySpringTest;
import org.junit.Test;

import javax.ws.rs.client.Invocation;

import static org.junit.Assert.assertEquals;

public class ClassicalDemoResourceTest extends ClassicalJerseySpringTest {
	@Test
	public void serviceIsSingleton() {
		Invocation.Builder req = target("demo/service/hash-code").request();
		String first = req.get(String.class);
		assertEquals(first, req.get(String.class));
	}

	@Test
	public void resourceIsSingleton() {
		assertEquals(target("demo/hash-code").request().get(String.class),
				target("demo/hash-code").request().get(String.class));
	}
}