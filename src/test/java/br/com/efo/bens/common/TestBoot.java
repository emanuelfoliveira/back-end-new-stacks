package br.com.efo.bens.common;

import static io.restassured.RestAssured.port;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

import br.com.efo.bens.common.utils.PasswordUtils;
import br.com.efo.bens.ds.Role;
import br.com.efo.bens.ds.User;
import br.com.efo.bens.service.inter.IdGenService;
import br.com.efo.bens.service.inter.UserService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class TestBoot {

    private static String USERNAME_01 = "UserName Test 01";
    private static String ADMIN = "Administrator";
    private static String PASSWORD_01 = PasswordUtils.DEFAULT_PASSWORD;
    private static String USER_EMAIL = "teste@teste.com";

    public static String INVALID = "invalid";

    @Autowired
    private UserService userService;
    @Autowired
    private IdGenService idGenService;

    @LocalServerPort
    public int PORT;

    public RestTemplate patchRestTemplate;
    @Autowired
    private TestRestTemplate restTemplate;

    public String PATH;
    public Map<String,String> jsonBodyMap = new HashMap<>();
    public List<String> parametersList = new LinkedList<>();

    @Before
    public void supportBefore() {
        idGenService.delete();
        userService.delete();
        port = PORT;
    }

    public void inicializeRestTest()
    {
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        PATH = "http://localhost:" + PORT + "/";
        jsonBodyMap = new HashMap<>();
        parametersList = new LinkedList<>();
    }

    public void inicializeRestTest(String mapping)
    {
        this.patchRestTemplate = restTemplate.getRestTemplate();
        HttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        PATH = "http://localhost:" + PORT + "/" + mapping + "/";
    }

    public JsonObject generateJson(Map<String,String> jsonContents)
    {
        JsonObject json = new JsonObject();

        if(jsonContents == null) {
            return json;
        }

        for(String key : jsonContents.keySet())
        {
            json.addProperty(key, jsonContents.get(key));
        }

        return json;
    }

    public HttpEntity<String> generateHttpEntity(Map<String,String> jsonContents)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Basic "+Base64.encodeAsString(ADMIN+":"+PASSWORD_01));
        headers.set("branchId", "1");
        return new HttpEntity<String>(generateJson(jsonContents).toString(), headers);
    }

    public HttpEntity<Object> generateHttpEntity(Object object)
    {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Basic "+Base64.encodeAsString(ADMIN+":"+PASSWORD_01) );
        headers.set("branchId", "1");
        return new HttpEntity<Object>(object, headers);
    }

    public User getUser()
    {
        return new User(USERNAME_01,PASSWORD_01, USERNAME_01, USER_EMAIL, null, 1, 1, null);
    }

    public User persistUser()
    {
        return userService.save(getUser());
    }

    public User persistUser(Role role)
    {
        User user = new User("User with custom role",PASSWORD_01, USERNAME_01, USER_EMAIL, null, 1, 1,null);
        user.setRole(role);
        return userService.save(user);
    }

    public User persistUser(int branchId)
    {
        return userService.save(new User("User with custom branch",PASSWORD_01, USERNAME_01, USER_EMAIL, null, branchId, 1, null));
    }

    public void persistUsers(int number)
    {
        for (int i = 0; i < number; i++)
        {
            userService.save(new User("UserName Test_"+i,PASSWORD_01, USERNAME_01, USER_EMAIL, null, 1, 1, null));
        }
    }

    public Object assertRequestPattern(TestRequestBuilder request)
    {
        persistAdmin();
        request = prepareTestRequestBuilder(request);
        if(request.getHttpEntity() == null) {
            request.setHttpEntity(generateHttpEntity(null));
        }
        ResponseEntity<?> response = null;
        if(request.getClassType() != null)
        {
            response = patchRestTemplate.exchange(PATH, request.getHttpMethod(), request.getHttpEntity(), request.getClassType());
        }
        if(request.getObjectType() != null)
        {
            response = patchRestTemplate.exchange(PATH, request.getHttpMethod(), request.getHttpEntity(), request.getObjectType());
        }
        assertEquals(request.getHttpStatusExpected(), response.getStatusCode());
        return response;
    }

    private void persistAdmin()
    {
        userService.save(new User(ADMIN, PASSWORD_01, ADMIN, USER_EMAIL, "M", 1, 1, null));
    }

    private TestRequestBuilder prepareTestRequestBuilder(TestRequestBuilder request)
    {
        if(!parametersList.isEmpty())
        {
            request.setParameters(parametersList);
        }

        if(!jsonBodyMap.isEmpty())
        {
            request.setHttpEntity(generateHttpEntity(jsonBodyMap));
        }

        if(request.getParameters() != null && !request.getParameters().isEmpty()) {
            for(String parameter : request.getParameters())
            {
                PATH = PATH + parameter + "/";
            }
            request.setPath(PATH);
        }

        if(request.getPath() == null)
        {
            request.setPath(PATH);
        }

        if(request.getClassType() == null)
        {
            request.setClassType(String.class);
        }

        return request;
    }
}
