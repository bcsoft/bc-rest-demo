package cn.bc.demo.web.rest;

import cn.bc.demo.DemoService;
import cn.bc.demo.DemoServiceImpl;
import com.owlike.genson.GensonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 显示请求信息
 *
 * @author dragon 2016-07-25
 */
@Named
@Path("info")
public class InfoResource {
	private final static Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);
	@Inject
	public DemoService service;

	@PostConstruct
	private void init() {
		// 在这里添加 spring 注入后的初始化代码;
	}

	@GET
	@Path("service-hash-code")
	@Produces(MediaType.TEXT_PLAIN)
	public String getServiceHashCode() {
		return service == null ? "null" : String.valueOf(service.hashCode());
	}

	@GET
	@Path("hash-code")
	@Produces(MediaType.TEXT_PLAIN)
	public String getHashCode() {
		return String.valueOf(this.hashCode());
	}

	@GET
	@Path("request")
	@Produces(MediaType.APPLICATION_JSON)
	public String requestInfo(@Context HttpHeaders headers,
	                          @Context UriInfo uriInfo,
	                          @Context HttpServletRequest request,
	                          @Context Request request_) {
		//logger.debug("headers={}", new JSONObject(headers).toString());
		//logger.debug("uriInfo={}", new Genson().serialize(uriInfo));
		//logger.debug("request={}", new Genson().serialize(request));
		Map<String, Object> info = new LinkedHashMap<>();

		// request
		Map<String, Object> req = new LinkedHashMap<>();
		info.put("request", req);
		if (request != null) {
			req.put("protocol", request.getProtocol());
			req.put("method", request.getMethod());
			req.put("requestUri", request.getRequestURI());
			req.put("requestUrl", request.getRequestURL().toString());
			req.put("queryString", request.getQueryString());
			req.put("contextPath", request.getContextPath());
			req.put("scheme", request.getScheme());
			req.put("remoteAddr", request.getRemoteAddr());
			req.put("remoteHost", request.getRemoteHost());
			req.put("remotePort", request.getRemotePort());
			req.put("remoteUser", request.getRemoteUser());
			req.put("serverName", request.getServerName());
			req.put("serverPort", request.getServerPort());
			req.put("localAddr", request.getLocalAddr());
			req.put("localPort", request.getLocalPort());
			req.put("localName", request.getLocalName());
			req.put("userPrincipal", request.getUserPrincipal() != null ? request.getUserPrincipal().toString() : null);
			req.put("contentType", request.getContentType());
			req.put("characterEncoding", request.getCharacterEncoding());
			req.put("locale", request.getLocale() != null ? request.getLocale().toString() : null);
			req.put("authType", request.getAuthType());
			req.put("class", request.getClass().getName());

			// session
			HttpSession session = null;
			try {
				session = request.getSession();
			} catch (Exception e) {
				logger.warn(e.getMessage());
			}
			Map<String, Object> s = new LinkedHashMap<>();
			info.put("session", s);
			if (session != null) {
				s.put("id", session.getId());
				s.put("creationTime", LocalDateTime.ofInstant(
						Instant.ofEpochMilli(session.getCreationTime()), ZoneId.systemDefault()).toString());
				s.put("lastAccessedTime", LocalDateTime.ofInstant(
						Instant.ofEpochMilli(session.getLastAccessedTime()), ZoneId.systemDefault()).toString());
				s.put("maxInactiveInterval", session.getMaxInactiveInterval());
				s.put("maxInactiveInterval2", Duration.ofSeconds(session.getMaxInactiveInterval()).toString());
				s.put("class", session.getClass().getName());
			}
		}

		// headers
		Map<String, Object> t = new LinkedHashMap<>();
		headers.getRequestHeaders().forEach((k, v) -> t.put(k, headers.getHeaderString(k)));
		req.put("headers", t);

		// uri
		Map<String, Object> uri = new LinkedHashMap<>();
		info.put("uriInfo", uri);
		uri.put("baseUri", uriInfo.getBaseUri());           // contextPath
		uri.put("requestUri", uriInfo.getRequestUri());
		uri.put("absolutePath", uriInfo.getAbsolutePath());
		uri.put("path", uriInfo.getPath());
		uri.put("path(decode)", uriInfo.getPath(true));
		uri.put("pathSegments", uriInfo.getPathSegments().toString());
		uri.put("pathParameters", uriInfo.getPathParameters().toString());
		uri.put("queryParameters", uriInfo.getQueryParameters().toString());
		uri.put("queryParameters(decode)", uriInfo.getQueryParameters(true).toString());
		info.put("cookies", headers.getCookies());

		String json = new GensonBuilder().useIndentation(true).create().serialize(info);
		logger.debug("json={}", json);
		return json;
	}

	@POST
	@Path("file")
	public File postFile(File file) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String s;
			do {
				s = br.readLine();
				logger.debug(s);
			} while (s != null);
			return file;
		}
	}

	// 字节流
	@POST
	@Path("stream")
	public String postInputStream(InputStream is) throws IOException {
		// jdk 7 的 try-with-resource 语法（最后自动释放资源）
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			StringBuilder r = new StringBuilder();
			String s = br.readLine();
			while (s != null) {
				r.append(s).append("\n");
				logger.debug(s);
				s = br.readLine();
			}
			return r.toString();
		}
	}

	@POST
	@Path("reader")
	public String postReader(Reader reader) throws IOException {
		try (BufferedReader br = new BufferedReader(reader)) {
			StringBuilder r = new StringBuilder();
			String s = br.readLine();
			while (s != null) {
				r.append(s).append("\n");
				logger.debug(s);
				s = br.readLine();
			}
			return r.toString();
		}
	}

	@GET
	@Path("json")
	public JsonObject jsr353_1() {
		return Json.createObjectBuilder().add("id", 1).add("name", "test").build();
	}

	@GET
	@Path("jsons")
	public JsonArray jsr353_2() {
		return Json.createArrayBuilder()
				.add(Json.createObjectBuilder()
						.add("id", 1)
						.add("str", "a"))
				.add(Json.createObjectBuilder()
						.add("id", 2)
						.add("str", "b"))
				.addNull()
				.build();
	}
}