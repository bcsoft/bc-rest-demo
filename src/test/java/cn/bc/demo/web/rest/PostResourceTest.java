package cn.bc.demo.web.rest;

import cn.bc.rest.test.JerseySpringTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class PostResourceTest extends JerseySpringTest {
	@Test
	public void form() {
		Form form = new Form().param("code", "test").param("name", "测试");
		Response res = target("post/form").request().post(Entity.form(form));
		assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
		assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
		assertEquals("test测试", res.readEntity(String.class));
	}
}