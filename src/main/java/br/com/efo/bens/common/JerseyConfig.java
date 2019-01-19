package br.com.efo.bens.common;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.Configuration;

import br.com.efo.bens.controller.UserRestController;

/**
 * 
 * @author emanuelfoliveira
 *
 */
@Configuration
public class JerseyConfig extends ResourceConfig {
	public JerseyConfig() {
		property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
		register(JerseyAuthenticationFilter.class);

		// Business
		register(UserRestController.class);
	}
}