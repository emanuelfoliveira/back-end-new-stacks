package br.com.efo.bens.common;

import static br.com.efo.bens.common.utils.MessagesUtils.ERROR_AUTHENTICATION;
import static br.com.efo.bens.common.utils.MessagesUtils.ERROR_USER_OR_PSW;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.security.PermitAll;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.efo.bens.common.utils.PasswordUtils;
import br.com.efo.bens.ds.OnlyAdmin;
import br.com.efo.bens.ds.Role;
import br.com.efo.bens.ds.User;
import br.com.efo.bens.service.inter.UserService;

/**
 * 
 * @author emanuelfoliveira
 *
 */
@Provider
@Component
public class JerseyAuthenticationFilter implements ContainerRequestFilter {
	@Autowired
	private UserService userService;

	@Context
	private ResourceInfo resourceInfo;

	public static final String CURRENT_USERNAME = "Current Username";
	private static final String AUTHORIZATION_PROPERTY = "Authorization";
	private static final String AUTHENTICATION_SCHEME = "Basic";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Method method = resourceInfo.getResourceMethod();

		if (method.isAnnotationPresent(PermitAll.class)) {
			return;
		}

		List<String> authorization = requestContext.getHeaders().get(AUTHORIZATION_PROPERTY);
		if (authorization == null || authorization.isEmpty()) {
			abortWithUnauthorized(requestContext, ERROR_AUTHENTICATION);
			return;
		}

		Map<String, String> mapUserAndPass = baseDecode64(authorization);
		String username = mapUserAndPass.get(USERNAME);
		String password = mapUserAndPass.get(PASSWORD);

		User user = userService.queryByUserName(username);

		if (method.isAnnotationPresent(OnlyAdmin.class) && !user.getRole().equals(Role.ADMINISTRATOR)) {
			abortWithUnauthorized(requestContext, ERROR_AUTHENTICATION);
		}

		if (user == null || !PasswordUtils.verifyUserPassword(password, user.getPasswordText())) {
			abortWithUnauthorized(requestContext, ERROR_USER_OR_PSW);
			return;
		}

		requestContext.setProperty(CURRENT_USERNAME, username);
	}

	/**
	 *
	 * @param requestContext
	 */
	private void abortWithUnauthorized(ContainerRequestContext requestContext, String msg) {

		requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity(msg).build());
	}

	/**
	 *
	 * @param userAndPass
	 * @param authorization
	 * @return
	 */
	private Map<String, String> baseDecode64(List<String> authorization) {
		Map<String, String> map = new HashMap<>();
		String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
		String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));
		StringTokenizer stringTokenizer = new StringTokenizer(usernameAndPassword, ":");
		String user = stringTokenizer.nextToken();
		String psw = stringTokenizer.nextToken();

		map.put(USERNAME, user);
		map.put(PASSWORD, psw);
		return map;
	}

}
