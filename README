Taxiway Route Fulfillment System
================================
About TRFS
==========
TRFS is a system to aid Air Traffic Controllers in a particular tower in fulfilling taxi route requests from pilots at their airport. Currently pilots radio to the tower to ask for a route, the controller speaks a route to the pilot, and the pilot reads back the route to the controller. When many planes are scheduled for departure in a short time window, this eats up significant amount of time over the radio and forces the controller to repeat his commands multiple times per minute. It also forces the pilot to remember the assigned route.

TRFS helps alleviate some of these problems by allowing a controller to send a visual representation of a taxi route to a pilot. The controller no longer needs to speak the same route over and over, freeing up valuable time which could be spent paying more attention to the safety on the taxiways themselves. The pilot also has the route on display at all times it is still needed.

About Us
========
TRFS was the Tufts University Computer Science Senior Capstone Project for Josh Pearl, Hawk Glazier, and Max Bulian, class of 2013, completed September 2012 - May 2013.


Future work and current shortcomings
====================================
Abstract Airports
================
Currently TRFS only supports the Providence, RI Airport (KPVD). This was done so
that we could focus on the details of the problem itself and not having to focus
on abstracting the problem to all airports before we could solve it for just one
airport. Some of the design for abstracting the airports exists, but needs
implementing. For example, each airport will be represented by an XML file that
specifies positional coordinates and other metadata necessary for correctly
rendering the airport. Once that is accomplished, we are left with having to
prompt our users in each of the 2 modules (tower and cockpit) for their airport,
as well as building the notion of an airport into the tower user authentication
system.


Audit Trail
===========
We currently do not support an Audit Trail. Implementing an Audit Trail could
be a large undertaking however. We rely on transponder codes as unique
identifiers, however they are only unique for the duration of the flight.
For example, if Flight A is assigned transponder code 1234, once Flight A lands
and is no longer an active flight, the transponder code 1234 is free and
available for assignment. This means that while we can log all the interactions
with a particular transponder code, we currently do not have a way to
distinguish between different flights successively using the same transponder
code.


Users and Favorite Routes Interface
===================================
We have not yet implemented a UI for managing users or for managing favorite
routes. The only UI capabilities for Favorite Routes are creating them. This is
a fairly straightforward exercise to just impelement a UI to the database for
these data.


GPS
===
Integrating GPS with TRFS would allow for many future enhancements to TRFS.
Knowing where a plane is along its route could do things like allow TRFS to read
instructions out loud to the pilot. It could also tie into existing FAA safety
and collision detection systems. If we can report the route the plane is
scheduled to take, then the safety mechanisms can become much more robust.




