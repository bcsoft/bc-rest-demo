package cn.bc.demo.web.rest;

import cn.bc.core.exception.NotExistsException;
import cn.bc.demo.Demo;
import cn.bc.demo.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * demo 资源
 *
 * @author dragon 2016-07-14
 */
@Singleton
@Path("demo")
public class DemoResource {
	private final static Logger logger = LoggerFactory.getLogger(DemoResource.class);

	@PostConstruct
	private void init() {
		// 在这里添加 spring 注入后的初始化代码;
	}

	@Inject
	public DemoService service;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("service/hash-code")
	public String getServiceHashCode() {
		return service == null ? "null" : String.valueOf(service.hashCode());
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("hash-code")
	public String getResourceHashCode() {
		return String.valueOf(this.hashCode());
	}

	@GET
	//@Consumes(MediaType.APPLICATION_JSON)   // Accept=application/json 或 Content-Type=application/json(优先判断Content-Type)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Demo getJson(@PathParam("id") Long id) {
		if (id == 100) throw new WebApplicationException();
		return service.get(id);
	}

	@GET
	//@Consumes(MediaType.TEXT_PLAIN)         // Accept=text/plain 或 Content-Type=text/plain(优先判断Content-Type)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{id: \\d+}")
	public String getText(@PathParam("id") Long id) {
		return service.get(id).getStr();
	}

	// 新建
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public String create(@FormParam("code") String code, @FormParam("name") String name) {
		return "{\"method\": \"POST\"}";
	}

	// 更新
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public String update(@PathParam("id") Long id, @FormParam("code") String code, @FormParam("name") String name) {
		if (id == 0) {
			throw new NotExistsException("测试 NotExistsMapper");
		}
		return "{\"method\": \"PUT\"}";
	}

	// 删除
	@DELETE
	@Path("{ids: .+}")
	//@Path("{ids: \\d+|[0-9,]+}")    // id1[,id2]
	public Response delete(@PathParam("ids") String ids) {
		return Response.noContent().build();
	}
}