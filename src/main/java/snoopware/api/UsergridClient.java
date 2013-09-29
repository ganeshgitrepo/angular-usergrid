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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.java.client.Client;

/**
 * Usergrid client configured via app.properties. 
 * @author David M. Johnson (snoopdave@gmail.com)
 */
public class UsergridClient extends Client {
  private final Logger log = LoggerFactory.getLogger(UsergridClient.class);
  public static final String organizationId;
  public static final String applicationId;
  public static final String applicationName;
  public static final String clientId;
  public static final String clientSecret;
  public static final URI targetUri;
  static {
    try {
      Properties properties = new Properties();
      properties.load(UsergridClient.class.getResourceAsStream("/app.properties"));
      organizationId = (String)properties.get("org.id");
      applicationId =  (String)properties.get("app.id");
      applicationName = (String) properties.get("app.name");
      clientId =       (String)properties.get("org.clientId");
      clientSecret =   (String)properties.get("org.clientSecret");
      targetUri = new URI((String) properties.get("app.usergridUri"));
    } catch (IOException ex) {
      throw new RuntimeException("Error reading app.properties", ex);
    } catch (URISyntaxException ex) {
      throw new RuntimeException("Error reading app.properties", ex);
    }
  }
 
  public UsergridClient() {
    super(organizationId, applicationId);
    authorizeAppClient(clientId, clientSecret);
    log.info("Usergrid client instance initialized");
  }
}
