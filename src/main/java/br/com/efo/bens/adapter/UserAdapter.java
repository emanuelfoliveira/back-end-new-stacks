package br.com.efo.bens.adapter;

import org.springframework.stereotype.Component;

import br.com.efo.bens.ds.User;
import br.com.efo.bens.ds.UserPostResponse;
import br.com.efo.bens.ds.UserResponse;

@Component
public class UserAdapter
{
    public UserResponse adaptResponse(User user)
    {
        return UserResponse.builder()
                           .id(user.getId())
                           .name(user.getName())
                           .email(user.getEmail())
                           .gender(user.getGender())
                           .userName(user.getUserName())
                           .passwordText(user.getPasswordText())
                           .build();
    }
    
    public UserPostResponse adaptPostResponse(User user) 
    {
	    return UserPostResponse.builder()
	    		.id(user.getId())
	    	    .name(user.getName())
	    	    .email(user.getEmail())
	    	    .gender(user.getGender())
	    	    .userName(user.getUserName())
	    	    .passwordText(user.getPasswordText())
	    	    .build();	
    }

}
