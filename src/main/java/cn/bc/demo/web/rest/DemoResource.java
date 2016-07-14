package cn.bc.demo.web.rest;

import cn.bc.demo.Demo;
import cn.bc.demo.DemoService;
import com.owlike.genson.GensonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * demo 资源
 *
 * @author dragon 2016-07-14
 */
@Singleton
@Path("demo")
public class DemoResource {
	private final static Logger logger = LoggerFactory.getLogger(DemoResource.class);

	public DemoResource() {
		logger.debug("this={}", this);
	}

	@PostConstruct
	private void init() {
		logger.debug("init");
	}

	@Inject
	private DemoService service;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String root() {
		if (service == null) throw new WebApplicationException("service is null");
		return "Hello Demo.";
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("request-info")
	public String requestInfo(@Context HttpHeaders headers,
	                          @Context UriInfo uriInfo,
	                          @Context HttpServletRequest request,
	                          @Context Request request_) {
		Map<String, Object> info = new LinkedHashMap<>();

		// request
		Map<String, Object> req = new LinkedHashMap<>();
		info.put("request", req);
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
		// headers
		Map<String, Object> t = new LinkedHashMap<>();
		headers.getRequestHeaders().forEach((k, v) -> t.put(k, headers.getHeaderString(k)));
		req.put("headers", t);
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

		// uri
		Map<String, Object> uri = new LinkedHashMap<>();
		info.put("uriInfo", uri);
		uri.put("baseUri", uriInfo.getBaseUri());   // contextPath
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
		logger.info("json={}", json);
		return json;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id: \\d+}")
	public Demo get(@PathParam("id") Long id) {
		if (id == 100) throw new WebApplicationException();
		return service.get(id);
	}
}