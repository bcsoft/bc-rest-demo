package cn.bc.demo.web.rest;

import cn.bc.core.exception.NotExistsException;
import cn.bc.core.util.StringUtils;
import cn.bc.demo.Demo;
import cn.bc.demo.DemoService;
import com.owlike.genson.Genson;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * demo 资源
 *
 * @author dragon 2016-07-14
 */
@Singleton
@Named
@Path("demo")
public class DemoResource {
	@Inject
	public DemoService service;

	@GET
	@Path("hash-code")
	@Produces(MediaType.TEXT_PLAIN)
	public String getHashCode() {
		return String.valueOf(this.hashCode());
	}

	// 获取视图模板
	@GET
	@Produces(MediaType.TEXT_HTML)          // Accept=text/html
	public String template4view() {
		return "<div>{0}</div>";
	}

	// 获取视图数据
	@GET
	@Produces(MediaType.APPLICATION_JSON)   // Accept=application/json
	public String data4view() {
		return new Genson().serialize(service.find());
	}

	// 获取表单模板：如静态模板
	@GET
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	@Path("form")
	public String template4form() {
		return "<form>{0}</form>";
	}

	// 获取混合表单数据的 html 页面：如动态模板
	@GET
	@Consumes(MediaType.TEXT_HTML)
	@Produces(MediaType.TEXT_HTML)
	@Path("{id: \\d+}")
	public String dataWithForm(@PathParam("id") Long id) {
		// 表单模版
		String tpl = "<form>%1$s:%2$s</form>";

		// 对象信息
		Demo demo = service.load(id);
		if (demo == null) throw new NotExistsException("所要获取的对象已不存在！id=" + id);

		// 用对象信息格式化模版
		return String.format(tpl, id, demo.getName());
	}

	// 获取表单数据
	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Demo data4form(@PathParam("id") Long id) {
		Demo demo = service.load(id);
		if (demo == null) throw new NotExistsException("所要获取的对象已不存在！id=" + id);
		return demo;
	}

	// 新建
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)    // Content-Type=application/x-www-form-urlencoded
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(@FormParam("ok") boolean ok, @FormParam("name") String name) throws URISyntaxException {
		Demo demo = new Demo();
		demo.setOk(ok);
		demo.setName(name);
		service.create(demo);
		return Response.created(new URI("/demo/" + demo.getId())).entity(demo).build();
	}

	// 更新
	@PUT
	@Path("{id: \\d+}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_PLAIN)
	public String update(@PathParam("id") Long id, @FormParam("ok") Boolean ok, @FormParam("name") String name) {
		Demo demo = service.load(id);
		if (demo == null) throw new NotExistsException("所要更新的对象已不存在！id=" + id);
		if (ok != null) demo.setOk(ok);
		if (name != null) demo.setName(name);
		int count = service.update(demo);
		return "成功更新 " + count + " 条信息！";
	}

	// 删除 1 条
	@DELETE
	@Path("{id: \\d+}")
	@Produces(MediaType.TEXT_PLAIN)
	public String delete(@PathParam("id") Long id) {
		int count = service.delete(new Long[]{id});
		return "成功删除 " + count + " 条信息！";
	}

	// 删除多条
	@DELETE
	@Path("{ids: .+}")
	@Produces(MediaType.TEXT_PLAIN)
	public String batchDelete(@PathParam("ids") String ids) {
		int count = service.delete(StringUtils.stringArray2LongArray(ids.split(",")));
		return "成功删除 " + count + " 条信息！";
	}

	// 删除全部
	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public String clean() {
		int count = service.deleteAll();
		return "成功删除 " + count + " 条信息！";
	}

	// 下载 txt 文件
	@GET
	@Path("export")
	@Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.TEXT_PLAIN})
	public Response download2txt() throws FileNotFoundException {
		// 模拟一个 InputStream
		InputStream in = new ByteArrayInputStream("测试 abc 123".getBytes(Charset.forName("UTF-8")));

		// 下载：实测 Content-Length 会自动生成
		return Response.ok(in).header("Content-Disposition", "inline; filename=test.txt").build();
	}

	// 导出 Excel
	@GET
	@Path("export")
	@Produces("application/vnd.ms-excel")    // 导出 Excel
	public Response download2excel() throws IOException {
		// 使用 File 或者 InputStream 两种方式都是可以的
		// File file = new File("d:\\test.xlsx");
		InputStream in = new FileInputStream("d:\\test.xlsx");
		return Response.ok(in)
				.header("Content-Length", String.valueOf(in.available()))      // 这个实测需要自己生成
				.header("Content-Disposition", "inline; filename=test.xlsx")   // 中文需要另行转码
				.build();
	}
}