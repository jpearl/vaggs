package com.vaggs.Utils;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import static com.vaggs.Utils.OfyService.ofy;

@Entity
public class AtcUser{
	@Id String username;
	
	@SuppressWarnings("unused")
    private AtcUser(){	
	}
	
	public static AtcUser CreateUser(User user) {
		return new AtcUser(user.getNickname());
	}
	
	public static AtcUser GetUser(String username) {
		return ofy().load().type(AtcUser.class).id(username).get();
	}
	
	public AtcUser(String name){
		this.username = name;
	}
	
	
}