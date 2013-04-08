package com.vaggs.Utils;

import static com.vaggs.Utils.OfyService.ofy;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class AtcUser{
	@Id String username;	
	
	private AtcUser(){
		
	}
	
	public AtcUser(String name){
		this.username = name;
	}
	
	
	public boolean isThere(){
		AtcUser controller = ofy().load().type(AtcUser.class).id(this.username).get();
		if(controller == null) {
			
			return false;
		}
		return true;
	}
	
	/*
	public void addUser(){ 
		if (!this.isThere()){
			ofy().save().entity(this).now();
		}
	}
	*/
	
}