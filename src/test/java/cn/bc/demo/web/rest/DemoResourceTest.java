package cn.bc.demo.web.rest;

import cn.bc.demo.DemoService;
import cn.bc.rest.test.JerseySpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class DemoResourceTest extends JerseySpringTest {
	@Autowired
	private DemoService service;

	@Test
	public void sameService() {
		assertEquals(String.valueOf(service.hashCode()), target("demo/service/hash-code").request().get(String.class));
	}

	@Test
	public void resourceIsSingleton() {
		assertEquals(target("demo/hash-code").request().get(String.class),
				target("demo/hash-code").request().get(String.class));
	}
}