package com.vaggs.Utils;

import static com.vaggs.Utils.OfyService.ofy;

import java.util.Arrays;

import com.vaggs.Route.Route;
import com.vaggs.Route.Transponder;
import com.vaggs.Route.Waypoint;

public class DebuggingDBObjects {

	public static void createDBObjects () {
		/* Transponder Code = 1 */
		Route debugRoute = Route.ParseRoute((new Waypoint(new LatLng(41.7258, -71.4368), false)), "");
		debugRoute.addWaypoints(Arrays.asList(
				new Waypoint(new LatLng(41.7258, -71.4368), false),
				new Waypoint(new LatLng(41.7087976, -71.44134), false),
				new Waypoint(new LatLng(41.73783, -71.41615), false),
				new Waypoint(new LatLng(41.725, -71.433333), true)
				));
		Transponder debug = new Transponder(1);
		Transponder debug2 = new Transponder(2);
		debug.setRoute(debugRoute);
		debug2.setRoute(debugRoute);
		ofy().save().entity(debug).now();
		ofy().save().entity(debug2).now();
	}
}
