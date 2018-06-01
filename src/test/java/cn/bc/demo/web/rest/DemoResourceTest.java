package cn.bc.demo.web.rest;

import cn.bc.demo.Demo;
import cn.bc.demo.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration("classpath:spring-test.xml")
public class DemoResourceTest {
  @Autowired
  public WebTarget target;
  @Autowired
  public DemoService service;

  @Test
  public void resourceIsSingleton() {
    // 资源类通过 @Singleton 配置为单例类型
    assertEquals(target.path("demo/hash-code").request().get(String.class), target.path("demo/hash-code").request().get(String.class));
  }

  @Test
  public void template4view() {
    // 先清空
    deleteAll();

    String path = "demo";
    String tpl = "<div>{0}</div>";

    // 指定 accept: text/html
    Response res = target.path(path).request(MediaType.TEXT_HTML).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType());
    assertEquals(tpl, res.readEntity(String.class));

    // 指定 chrome 浏览器默认的 accept
    res = target.path(path).request("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType());
    assertEquals(tpl, res.readEntity(String.class));

    // 不指定 accept：在不同环境可能会得到不同的结果，慎用
    // 实测 IDE 内右键运行单元测试，适配 accept=html/text
    // 实测 mvn test，则适配 accept=application/json
    res = target.path(path).request().get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    // assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType()); // 在不同环境可能会得到不同的结果
    // assertEquals(tpl, res.readEntity(String.class));
  }

  @Test
  public void data4view() {
    // 先清空
    deleteAll();

    // 指定 accept: application/json
    Response res = target.path("demo").request(MediaType.APPLICATION_JSON).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    assertEquals("[]", res.readEntity(String.class));

    // 通过质量因子控制 application/json 优先
    res = target.path("demo").request("text/html;q=0.4,application/json;q=0.5").get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    assertEquals("[]", res.readEntity(String.class));
  }

  @Test
  public void template4form() {
    String path = "demo/form";
    String tpl = "<form>{0}</form>";

    // 不指定 accept
    Response res = target.path(path).request().get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType());
    assertEquals(tpl, res.readEntity(String.class));

    // 指定 accept: text/html
    res = target.path(path).request(MediaType.TEXT_HTML).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType());
    assertEquals(tpl, res.readEntity(String.class));

    // 指定 chrome 浏览器默认的 accept
    res = target.path(path).request("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType());
    assertEquals(tpl, res.readEntity(String.class));
  }

  @Test
  public void data4form_exists() {
    // 先创建一个对象
    Demo origin = createOne();

    // 获取对象信息
    String path = "demo/" + origin.getId();
    // 指定 accept: application/json
    Response res = target.path(path).request(MediaType.APPLICATION_JSON).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    assertEquals(origin.getId(), res.readEntity(Demo.class).getId());
  }

  @Test
  public void data4form_not_exists() {
    String path = "demo/10000";
    // 指定 accept: application/json
    Response res = target.path(path).request(MediaType.APPLICATION_JSON).get();
    assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("所要获取的对象已不存在！id=10000", res.readEntity(String.class));
  }

  @Test
  public void dataWithForm_exists() {
    // 先创建一个对象
    Demo origin = createOne();

    // 获取对象信息
    String path = "demo/" + origin.getId();
    // 指定 accept: text/html
    Response res = target.path(path).request(MediaType.TEXT_HTML).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_HTML_TYPE, res.getMediaType());
    assertEquals("<form>" + origin.getId() + ":" + origin.getName() + "</form>", res.readEntity(String.class));
  }

  @Test
  public void dataWithForm_not_exists() {
    // 获取对象信息
    String path = "demo/10000";
    Response res = target.path(path).request(MediaType.TEXT_HTML).get();
    assertEquals(Status.NOT_FOUND.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("所要获取的对象已不存在！id=10000", res.readEntity(String.class));
  }

  @Test
  public void create() {
    Form form = new Form().param("ok", "true").param("name", "创建");
    Response res = target.path("demo").request().post(Entity.form(form));
    assertEquals(Status.CREATED.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_JSON_TYPE, res.getMediaType());
    Demo demo = res.readEntity(Demo.class);
    assertNotNull(demo);
    assertNotNull(demo.getId());
    assertEquals(true, demo.isOk());
    assertEquals("创建", demo.getName());
  }

  @Test
  public void update() {
    // 先创建一个对象
    Demo origin = createOne();

    // 仅更新 name
    String newName = origin.getName() + "-update";
    Form form = new Form().param("name", newName);
    Response res = target.path("demo/" + origin.getId()).request().put(Entity.form(form));
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("成功更新 1 条信息！", res.readEntity(String.class));
    Demo u = service.load(origin.getId());
    assertNotNull(u);
    assertEquals(newName, u.getName());
    assertEquals(origin.isOk(), u.isOk());
  }

  @Test
  public void delete() {
    // 先创建一个对象
    Demo origin = createOne();

    // 删除创建的对象
    Response res = target.path("demo/" + origin.getId()).request().delete();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("成功删除 1 条信息！", res.readEntity(String.class));

    // 重复删除
    res = target.path("demo/" + origin.getId()).request().delete();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("成功删除 0 条信息！", res.readEntity(String.class));
  }

  @Test
  public void batchDelete() {
    // 创建 2 个对象
    Demo o1 = createOne();
    Demo o2 = createOne();

    String path = "demo/" + o1.getId() + "," + +o2.getId();

    // 删除创建的对象
    Response res = target.path(path).request().delete();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("成功删除 2 条信息！", res.readEntity(String.class));

    // 重复删除
    res = target.path(path).request().delete();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertEquals("成功删除 0 条信息！", res.readEntity(String.class));
  }

  @Test
  public void clean() {
    Response res = target.path("demo").request().delete();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.TEXT_PLAIN_TYPE, res.getMediaType());
    assertTrue(res.readEntity(String.class).startsWith("成功删除"));
  }

  @Test
  public void download2txt() throws IOException {
    Response res = target.path("demo/export").request(MediaType.APPLICATION_OCTET_STREAM).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(MediaType.APPLICATION_OCTET_STREAM_TYPE, res.getMediaType());

    InputStream in = res.readEntity(InputStream.class);
    assertNotNull(in);
    assertEquals(14, in.available());
    assertEquals("测试 abc 123", StreamUtils.copyToString(in, Charset.forName("UTF-8")));
    // FileCopyUtils.copy(in, new FileOutputStream("d:\\test_download.txt")); // 保存为文件

		/* also ok
		String in = res.readEntity(String.class);
		assertNotNull(in);
		assertEquals("测试 abc 123", in);
		*/
  }

  //@Test
  public void download2excel() throws IOException {
    String mediaType = "application/vnd.ms-excel";
    Response res = target.path("demo/export").request(mediaType).get();
    assertEquals(Status.OK.getStatusCode(), res.getStatus());
    assertEquals(mediaType, res.getMediaType().toString());
    InputStream in = res.readEntity(InputStream.class);
    assertNotNull(in);
    //assertEquals(8199, in.available());
    FileCopyUtils.copy(in, new FileOutputStream("d:\\test_download.xlsx"));// 保存为文件
  }

  private Demo createOne() {
    return createOne(true, "新人");
  }

  private Demo createOne(boolean ok, String name) {
    Demo demo = new Demo();
    demo.setOk(ok);
    demo.setName(name);
    return service.create(demo);
  }

  private void deleteAll() {
    service.deleteAll();
  }

  @Test
  public void _formatString() {
    assertEquals("1, a, 2", String.format("%1$d, %2$s, %3$s", 1, "a", 2));
  }
}