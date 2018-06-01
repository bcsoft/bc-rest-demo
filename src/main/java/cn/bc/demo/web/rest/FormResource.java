package cn.bc.demo.web.rest;

import com.owlike.genson.Genson;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;

import javax.inject.Named;
import javax.ws.rs.*;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * 提交表单数据的测试
 *
 * @author dragon 2016-07-28
 */
@Named
@Path("form")
public class FormResource {
  // 经典的使用方式：@FormParam
  @POST
  @Path("post11")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postForm1(@FormParam("code") String code, @FormParam("name") String name) {
    return Response.created(null).entity(code + name).build();
  }

  // 自动将提交的表单参数全部放到 MultivaluedMap 中
  @POST
  @Path("post12")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postForm1(MultivaluedMap<String, String> data) {
    //System.out.println(form.getFirst("code")); // String
    //System.out.println(form.get("code"));      // List<String>
    return Response.created(null).entity(new Genson().serialize(data)).build();
  }

  // 自动将提交的表单参数全部放到 Form 中
  @POST
  @Path("post13")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.APPLICATION_JSON)
  public Response postForm1(Form form) {
    return Response.created(null).entity(new Genson().serialize(form.asMap())).build();
  }

  // 自动将提交的表单参数全部放到 InputStream 中
  @POST
  @Path("post14")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postForm1(InputStream is) throws IOException {
    return Response.created(null).
      entity(new Genson().serialize(StreamUtils.copyToString(is, Charset.forName("UTF-8"))))
      .build();
  }

  // 自动将提交的表单参数全部放到 body 中
  @POST
  @Path("post15")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postForm1(String body) throws IOException {
    return Response.created(null).entity(body).build();
  }

  // 自动将提交的表单参数全部放到 Reader 中
  @POST
  @Path("post16")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postForm1(Reader body) throws IOException {
    return Response.created(null).entity(FileCopyUtils.copyToString(body)).build();
  }

  // 自动将提交的表单参数全部放到 byte[] 中
  @POST
  @Path("post17")
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postForm1(byte[] body) {
    return Response.created(null).entity(new String(body, Charset.forName("UTF-8"))).build();
  }

  // 自动将提交的表单参数全部放到 body 中
  @POST
  @Path("post21")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  @Produces(MediaType.TEXT_PLAIN)
  public Response postForm21(String body) {
    return Response.created(null).entity(body).build();
  }
}