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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.usergrid.java.client.Client;
import org.usergrid.java.client.entities.Entity;
import org.usergrid.java.client.response.ApiResponse;


/**
 * Test basic operation of Usergrid Java SDK.
 * @author David M. Johnson (snoopdave@gmail.com)
 */
@RunWith(JUnit4.class)
public class UsergridSdkTest {
  private final Logger log = LoggerFactory.getLogger(UsergridSdkTest.class);

  @Test
  public void testUsergridAuth() throws IOException, URISyntaxException {

    Properties properties = new Properties();
    properties.load(getClass().getResourceAsStream("/app.properties"));

    URI targetUri = new URI((String)properties.get("app.usergridUri"));

    String organizationId = (String)properties.get("org.id");
    String applicationId = (String)properties.get("app.id");
    Client client = new Client(organizationId, applicationId);

    String clientId = (String)properties.get("org.clientId");
    String clientSecret = (String) properties.get("org.clientSecret");
    client.authorizeAppClient(clientId, clientSecret);

    Client.Query queryUsers = client.queryUsers();
    Assert.assertTrue(queryUsers.getResponse().getEntities().size() > 0);
    for (Entity entity : queryUsers.getResponse().getEntities()) {
      log.info("found username: " + entity.getProperties().get("username").asText());
    }

//    {
//      ApiResponse res = client.apiRequest(HttpMethod.GET, null, null, 
//        (String)properties.get("org.name"), (String)properties.get("app.name"), "user", "b");
//      Assert.assertEquals("service_resource_not_found", res.getError());
//      log.info(res.getStatus());
//    }
//
//    {
//      ApiResponse res = client.apiRequest(HttpMethod.GET, null, null, 
//        (String)properties.get("org.name"), (String)properties.get("app.name"), "user", "bob");
//      Assert.assertEquals(null, res.getError());
//    }

  }
}
