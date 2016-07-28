package cn.bc.demo.web.rest;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * post 各种数据类型的请求测试
 *
 * @author dragon 2016-07-28
 */
@Named
@Path("post")
public class PostResource {
	@POST
	@Path("form")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public Response form(@FormParam("code") String code, @FormParam("name") String name) {
		return Response.created(null).entity(code + name).build();
	}
}