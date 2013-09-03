/*
 * Copyright 2013 David M. Johnson (snoopdave@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package snoopware.api;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpRequest;
import org.usergrid.java.client.Client;

/**
 * Proxy all requests to UserGrid and to add credentials to /users GET and POST requests.
 * @author David M. Johnson (snoopdave@gmail.com)
 */
@WebServlet(
	name = "usergridProxy", urlPatterns = {"/api/*"}, 
	initParams={
		@WebInitParam(name="log", value="true"),
		@WebInitParam(name="targetUri", value="https://api.usergrid.com")
	}) 
@SuppressWarnings("serial")
public class UserGridProxyServlet extends ProxyServlet {
	protected static final Logger log = Logger.getLogger(ProxyServlet.class.getName()); 

	private Client userGridClient;
	private String applicationName;

	/**
	 * Override to authorized UserGrid client with creds from app.propeties.
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		Properties properties = new Properties();
		try {
			properties.load(getClass().getResourceAsStream("/app.properties"));

			applicationName = (String)properties.get("app.applicationName");
			String organizationId = (String)properties.get("app.organizationId");
			String applicationId = (String)properties.get("app.applicationId");
			userGridClient = new Client(organizationId, applicationId);

			String clientId = (String)properties.get("app.clientId");
			String clientSecret = (String)properties.get("app.clientSecret");
			userGridClient.authorizeAppClient(clientId, clientSecret);

		} catch (IOException ex) {
			log.log(Level.SEVERE, "Unable to load app.properties and authorize the client", ex);
		}
	}

	/**
	 * Override to add Authorization to requests that need it. 
	 */
	@Override
	protected void copyRequestHeaders(HttpServletRequest req, HttpRequest proxyReq) {
		super.copyRequestHeaders(req, proxyReq);

		boolean hasAccessToken = req.getQueryString() == null 
			? false : req.getQueryString().contains("access_token=");

		if (!hasAccessToken
			&& ("POST".equals(req.getMethod()) || "GET".equals(req.getMethod()))
			&& req.getPathInfo().contains("/" + applicationName + "/users")) {

				String header = "Bearer " +  userGridClient.getAccessToken(); 
				proxyReq.setHeader("Authorization", header);
				log.log(Level.INFO, "Added header = {0}", header);
		}
	}
}
