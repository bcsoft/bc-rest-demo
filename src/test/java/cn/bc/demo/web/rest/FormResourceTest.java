package cn.bc.demo.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.assertEquals;

/**
 * 提交表单数据的测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class FormResourceTest {
  @Autowired
  public WebTarget target;

  // request.Content-Type=application/x-www-form-urlencoded
  @Test
  public void postForm11__x_www_form_urlencoded() {
    Form form = new Form().param("code", "test").param("name", "测试");
    Response res = target.path("form/post11").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("test测试", res.readEntity(String.class));
  }

  // request.Content-Type=application/x-www-form-urlencoded
  @Test
  public void postForm12__x_www_form_urlencoded() {
    Form form = new Form().param("code", "t1").param("code", "t2").param("name", "测试");
    Response res = target.path("form/post12").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    assertEquals("{\"code\":[\"t1\",\"t2\"],\"name\":[\"测试\"]}", res.readEntity(String.class));
  }

  @Test
  public void postForm13__x_www_form_urlencoded() {
    Form form = new Form().param("code", "t1").param("code", "t2").param("name", "测试");
    Response res = target.path("form/post13").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    assertEquals("{\"code\":[\"t1\",\"t2\"],\"name\":[\"测试\"]}", res.readEntity(String.class));
  }

  @Test
  public void postForm14__x_www_form_urlencoded() throws UnsupportedEncodingException {
    Form form = new Form().param("code", "t1").param("code", "t2").param("name", "测试");
    Response res = target.path("form/post14").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    //System.out.println(URLEncoder.encode("测试", "UTF-8"));
    assertEquals("\"code=t1&code=t2&name=" + URLEncoder.encode("测试", "UTF-8") + "\"", res.readEntity(String.class));
  }

  @Test
  public void postForm15__x_www_form_urlencoded() throws UnsupportedEncodingException {
    Form form = new Form().param("code", "t1").param("code", "t2").param("name", "测试");
    Response res = target.path("form/post15").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("code=t1&code=t2&name=" + URLEncoder.encode("测试", "UTF-8"), res.readEntity(String.class));
  }

  @Test
  public void postForm16__x_www_form_urlencoded() throws UnsupportedEncodingException {
    Form form = new Form().param("code", "t1").param("code", "t2").param("name", "测试");
    Response res = target.path("form/post16").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("code=t1&code=t2&name=" + URLEncoder.encode("测试", "UTF-8"), res.readEntity(String.class));
  }

  @Test
  public void postForm17__x_www_form_urlencoded() throws UnsupportedEncodingException {
    Form form = new Form().param("code", "t1").param("code", "t2").param("name", "测试");
    Response res = target.path("form/post17").request().post(Entity.form(form));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("code=t1&code=t2&name=" + URLEncoder.encode("测试", "UTF-8"), res.readEntity(String.class));
  }

  // request.Content-Type=multipart/form-data
  @Test
  public void postForm21__multipart_form_data() throws UnsupportedEncodingException {
    Form form = new Form().param("code", "test").param("name", "测试");
    Response res = target.path("form/post21").request()
      .post(Entity.entity(form, MediaType.MULTIPART_FORM_DATA));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("code=test&name=" + URLEncoder.encode("测试", "UTF-8"), res.readEntity(String.class));
  }
}