package cn.bc.demo.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * json 数据类型的请求测试
 *
 * @author dragon 2016-07-29
 */
@Named
@Path("json")
public class JsonResource {
  private static Logger logger = LoggerFactory.getLogger(JsonResource.class);

  @GET
  @Path("{id: \\d+}")
  @Produces(MediaType.APPLICATION_JSON)
  public JsonObject getJsonObject(@PathParam("id") final int id) {
    return Json.createObjectBuilder()
      .add("id", id)
      .build();
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public JsonArray getJsonArray() {
    return Json.createArrayBuilder()
      .add(Json.createObjectBuilder().add("id", 1))
      .build();
  }

  // 使用 jersey-media-json-processing
  // https://jersey.java.net/documentation/latest/media.html#json.json-p
  // class com.owlike.genson.ext.jsr353.GensonJsonObject
  @POST
  @Path("post-object")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postJsonObject(JsonObject data) {
    logger.debug("class={}", data.getClass());
    return Response.ok().entity(data).build();
  }

  @POST
  @Path("post-array")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postJsonArray(JsonArray data) {
    logger.debug("class={}", data.getClass());
    return Response.created(null).entity(data).build();
  }
}