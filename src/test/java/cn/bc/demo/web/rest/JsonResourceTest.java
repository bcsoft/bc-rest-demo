package cn.bc.demo.web.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.OK;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class JsonResourceTest {
  @Autowired
  public WebTarget target;

  @Test
  public void getJsonObject() {
    Response res = target.path("json/1").request().get();
    assertEquals(OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    // or assertEquals({"id":1}, res.readEntity(String.class));
    assertEquals("{\"id\":1}", res.readEntity(JsonObject.class).toString());
  }

  @Test
  public void getJsonArray() {
    Response res = target.path("json").request().get();
    assertEquals(OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    // or assertEquals([{"id":1}], res.readEntity(String.class));
    assertEquals("[{\"id\":1}]", res.readEntity(JsonArray.class).toString());
  }

  @Test
  public void postJsonObject() {
    JsonObject json = Json.createObjectBuilder()
      .add("str", "test")
      .add("bln", true)
      .add("num", 1.1)
      .build();
    Response res = target.path("json/post-object").request().post(Entity.json(json));
    assertEquals(OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    // or assertEquals(json.toString(), res.readEntity(String.class));
    assertEquals(json.toString(), res.readEntity(JsonObject.class).toString());
  }

  @Test
  public void postJsonArray() {
    JsonArray jsons = Json.createArrayBuilder()
      .add(Json.createObjectBuilder()
        .add("str", "test")
        .add("bln", true)
        .add("num", 1.1))
      .build();
    Response res = target.path("json/post-array").request().post(Entity.json(jsons));
    assertEquals(Response.Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    // or assertEquals(jsons.toString(), res.readEntity(String.class));
    assertEquals(jsons.toString(), res.readEntity(JsonArray.class).toString());
  }
}