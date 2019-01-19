package br.com.efo.bens.controller;

import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.internal.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.google.gson.JsonObject;

import br.com.efo.bens.adapter.UserAdapter;
import br.com.efo.bens.common.RestSupport;
import br.com.efo.bens.common.utils.MessagesUtils;
import br.com.efo.bens.common.utils.PasswordUtils;
import br.com.efo.bens.ds.ChangePasswordRequest;
import br.com.efo.bens.ds.Role;
import br.com.efo.bens.ds.User;
import br.com.efo.bens.ds.UserPostResponse;
import br.com.efo.bens.ds.UserResponse;
import br.com.efo.bens.service.inter.MailService;
import br.com.efo.bens.service.inter.UserService;

@Component
@Path("/user")
@CrossOrigin(origins = "*")
public class UserRestController extends RestSupport{

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserAdapter userAdapter;
    
    @Autowired
    private MailService mailService;

    @GET
    @Produces("application/json")
    public Response get()
    {
       List<UserResponse> usersResponse = userService.queryAll().stream()
        					.map(user -> userAdapter.adaptResponse(user))
        					.collect(Collectors.toList());

        return responseGetAllSuccess(usersResponse);
    }

    @DELETE
    @Produces("application/json")
    public Response delete()
    {
        userService.delete();
        return responseDeleteAllSuccess();
    }

    @DELETE
    @Path("{id}")
    @Produces("application/json")
    public Response deleteById(@PathParam("id") Integer id)
    {
        Optional<User> optionalUser = userService.getUserById(id);
        if(!optionalUser.isPresent()) {
            return responseIdNotFound("User");
        }
        if(optionalUser.get().getRole().equals(Role.ADMINISTRATOR)) {
            JsonObject json = new JsonObject();
            json.addProperty("error", "ADMIN User");
            return Response.status(401).entity(json.toString()).build();
        }
        userService.deleteById(id);
        return Response.status(200).entity(optionalUser.get()).build();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Response post(User user)
    {
        if(!validUser(user))
        {
            return responseBodyInvalid();
        }
        user = userService.save(user);
        UserPostResponse userPostResponse = userAdapter.adaptPostResponse(user);
        return Response.status(Status.CREATED).entity(userPostResponse).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Response getById(@PathParam("id") Integer id)
    {
        Optional<User> optionalUser = userService.getUserById(id);
        if(!optionalUser.isPresent())
        {
            return responseIdNotFound("User");
        }
        User user = optionalUser.get();
        return responseGetByIdSuccess( userAdapter.adaptResponse(user));
    }

    @PATCH
    @Path("{id}")
    @Consumes("application/json")
    public Response patch(@PathParam("id") Integer id, User user)
    {
        Optional<User> optionalUser = userService.getUserById(id);
        if(!optionalUser.isPresent())
        {
            return responseIdNotFound("User");
        }
        if(!validUser(user))
        {
            return responseBodyInvalid();
        }
        user.setId(optionalUser.get().getId());
        userService.save(user);
        return responsePatchSuccess();
    }

    @PermitAll
    @POST
    @Path("login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response createNewUser(User user) {
        if(userService.queryByUserName(user.getUserName()) != null) {
            return responseBodyInvalid();
        }
        user = userService.save(user);
        return Response.status(Status.CREATED).entity(user).build();
    }

    @GET
    @Path("login")
    @CrossOrigin(allowedHeaders = "*", origins = "*")
    @Produces("application/json")
    public Response login(@HeaderParam("Authorization") String auth) {
        User user = userService.getUserById(getCurrentUser(auth)).get();
        return Response.status(Status.OK).entity(userAdapter.adaptResponse(user)).build();
    }

    @PermitAll
    @POST
    @Path("changePassword")
    @Consumes("application/json")
    public Response changePassword(ChangePasswordRequest request) {
        User user = userService.queryByUserName(request.getUsername());
        if(user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(MessagesUtils.ERROR_USER_OR_PSW).build();
        }

        user.setPasswordText(request.getNewPassword());
        userService.save(user);
        return Response.status(Status.OK).build();
    }
    
    @POST
    @Path("updatePassword")
    @Consumes("application/json")
    public Response updatePassword(ChangePasswordRequest request) {
        User user = userService.queryByUserName(request.getUsername());
        if(user == null || validateOldAndNewPassword(request, user)) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(MessagesUtils.ERROR_USER_OR_PSW).build();
        }

        user.setPasswordText(request.getNewPassword());
        userService.save(user);
        return Response.status(Status.OK).build();
    }

	private boolean validateOldAndNewPassword(ChangePasswordRequest request, User user) {
		User requestPsw = new User();
		requestPsw.setPasswordText(request.getOldPassword());
		
		return !user.getPasswordText().equals(requestPsw.getPasswordText());
	}
    
    @PermitAll
    @POST
    @Path("resetPassword/{email}")
    public Response resetPassword(@PathParam("email") String email) {
        User user = userService.queryByEmail(email);
        if(user == null) {
        	return Response.status(Response.Status.UNAUTHORIZED).entity(MessagesUtils.ERROR_EMAIL).build();
        }
        user.setPasswordText(PasswordUtils.DEFAULT_PASSWORD);
        userService.save(user);
        
        return Response.status(Status.OK).build();
    }
    
    @PermitAll
    @POST
    @Path("sendChangePasswordEmail/{email}")
    public Response sendChangePasswordEmail(@PathParam("email") String email) {
        User user = userService.queryByEmail(email);
        if(user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(MessagesUtils.ERROR_EMAIL).build();
        }
        mailService.sendForgottenPasswordEmail(user.getName(), email);
        
        return Response.status(Status.OK).build();
    }

    private boolean validUser(User user)
    {
        return !(user.getUserName() == null || user.getName() == null || user.getEmail() == null);
    }


    private Integer getCurrentUser(String auth)
    {
        final String encodedUserPassword = auth.replaceFirst("Basic ", "");
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));
        final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        return userService.queryByUserName(username).getId();
    }
}
