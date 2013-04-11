package com.vaggs.Route;

import static com.vaggs.Utils.OfyService.ofy;

import java.util.Date;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.vaggs.Utils.JsonRouteWriter;

/**
 * An object that represents a transponder code
 */
@Entity
public class Transponder {
	private Route route = null;
	@Id private long code;
	private Date timeStamp;
	
	public static Transponder Parse(long code) {
		if(!CheckTransponderCode(code)) 
			return null;
		Transponder transponder = ofy().load().type(Transponder.class).id(code).get();
		if(transponder == null || transponder.timeStamp.before(getInvalidTime()))
			transponder = new Transponder(code);
		return transponder;
	}

	private static final long halfHourInMillis = 30 * 60 * 1000;  
	private static Date getInvalidTime() {
		Date time = new Date();
		time.setTime(time.getTime() - halfHourInMillis);
		return time;
	}
	
	private Transponder() {
		code = -1;
	}
	
	private Transponder(long code) {
		this.code = code;
		ForceSave();
	}
	
	/**
	 * Private method for checking validity of Transponder Codes
	 * @return true if the argument is between the octal numbers 0000 and 7777
	 */
	static boolean CheckTransponderCode(long code) {
		for(int i = 0; i < 4; i++) {
			if(code % 10 > 7) return false;
			code /= 10;
		}
		return code == 0;
	}
	
	public void ForceSave() {
		timeStamp = new Date();
		ofy().save().entity(this).now();
	}
	
	/**
	 * 
	 * @return true if the RouteRequest has been given a route by the ATC,
	 * false otherwise
	 */
	public boolean hasRoute() {
		return null != route;
	}

	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(Route route) {
		this.route = route;
		
		//update route on the client
		ChannelService channelService = ChannelServiceFactory.getChannelService();
	    channelService.sendMessage(new ChannelMessage(
	    		String.valueOf(getTransponderCode()), JsonRouteWriter.writeRoute(route)));

	    ForceSave();
	}

	/**
	 * @return the transponderCode
	 */
	public long getTransponderCode() {
		return code;
	}
}
