package com.vaggs.Utils;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.vaggs.AirportDiagram.Airport;
import com.vaggs.Route.FavoriteRoutes;
import com.vaggs.Route.Route;
import com.vaggs.Route.RouteRequest;
import com.vaggs.Route.Transponder;

public class OfyService {
    static {
            factory().register(FavoriteRoutes.class);
            factory().register(Airport.class);
            factory().register(RouteRequest.class);
            factory().register(Route.class);
            factory().register(Transponder.class);
            
            DebuggingDBObjects.createDBObjects();
    }

    public static Objectify ofy() {
            return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
            return ObjectifyService.factory();
    }
}