package cn.bc.demo.web.rest;

import cn.bc.rest.test.JerseySpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class InfoResourceTest extends JerseySpringTest {
	private final static Logger logger = LoggerFactory.getLogger(InfoResourceTest.class);

	@Test
	public void getRequestInfo() {
		Response res = target("info/request").request().get();
		assertEquals(Response.Status.OK.getStatusCode(), res.getStatus());
		assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
		logger.debug(res.readEntity(String.class));
	}
}