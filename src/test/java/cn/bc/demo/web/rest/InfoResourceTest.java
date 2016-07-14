package cn.bc.demo.web.rest;

import cn.bc.demo.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class InfoResourceTest {
	private final static Logger logger = LoggerFactory.getLogger(InfoResourceTest.class);
	@Autowired
	public WebTarget target;
	@Autowired
	private DemoService service;

	@Test
	public void sameService() {
		// 资源类中注入的 service 与这里注入的 service 是同一个实例，则证明整个单元测试是在同一个 spring 上下文内
		assertEquals(String.valueOf(service.hashCode()),
				target.path("info/service-hash-code").request().get(String.class));
	}

	@Test
	public void resourceIsPrototype() {
		// 资源类通过 @Named 配置为原型类型
		assertNotEquals(target.path("info/hash-code").request().get(String.class),
				target.path("info/hash-code").request().get(String.class));
	}

	@Test
	public void getRequestInfo() {
		Response res = target.path("info/request").request().get();
		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
		logger.debug(res.readEntity(String.class));
	}
}