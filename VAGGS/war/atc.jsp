<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map_canvas { height: 100% }
    </style>
    
    <script type="text/javascript" src="jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="/_ah/channel/jsapi"></script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAK0HG-UZtFAiPtHkyc6xjxI2wQXCOX4Pk&sensor=false"></script>
    <script type="text/javascript">
      var overlay;
      var map;
      var allowedBounds = new google.maps.	LatLngBounds(new google.maps.LatLng
        (41.7125479, -71.44203186),new google.maps.LatLng(41.7336318, -71.4168834));
      var minZoomLevel = 14;
      var maxZoomLevel = 16;

      var airportID = "kpvd";
      var taxiways = null;
      var currTaxiway = 0;
      var markers = [];
      var polyLine = null;
      var route = [];
      var routeRequests = [];
      
      PVDOverlay.prototype = new google.maps.OverlayView();

      function LatLng(lat, lng) {
        return new google.maps.LatLng(lat, lng);
      }
      
      function initialize() {
        $.ajax({cache:false});
        
		//register and set up the channel
        $.getJSON("channelregistration?mode=tower", function(tokenResp) {
            token = tokenResp.token;
            channel = new goog.appengine.Channel(token);
            socket = channel.open();
            socket.onmessage = onChannelMessage;
            socket.onopen = onOpen;
         });
		
        $.getJSON("airportinfo?airport=" + airportID, function(taxis) {
          console.log("Queried API and got taxiway info");
          taxiways = taxis;
          displayTaxiways([0]);
        }).error(function() {
          console.log("Failed to get taxiway info from server for " + airportID);
        });
        
        map = new google.maps.Map(document.getElementById('map_canvas'), {
          zoom: 14,
          center: LatLng(41.7258, -71.4368),
          mapTypeId: google.maps.MapTypeId.ROADMAP,
          disableDefaultUI: true,
          styles: [{
            elementType: 'labels',
            featureType: 'poi',
            stylers: [ { visibility: 'off' } ]
          }]
        });
        
        //airport diagram
        var srcImage = 'airports/pvd2.png';
        overlay = new PVDOverlay(new google.maps.LatLngBounds(LatLng(41.7087976, -71.44134), LatLng(41.73783, -71.41615)), srcImage, map);
        
		/** Google Channel Functions **/
        onChannelMessage = function(message) {
          console.log("Got route request message");
          processMessage(message);
        }
        
        onOpen = function() {
            console.log("Channel opened to server");
        }
        
        
        /** ============ **/
        
        function processMessage(message) {
            transponder = $.parseJSON(message.data).transponder;
            routeRequests.push(transponder);
            console.log('Transponder: ' + transponder + ' connected');
        }
		
        taxiRouteOptions = {
          strokeColor: "#00FF00",
          strokeOpacity: 1.0,
          strokeWeight: 3,
          map: map,
          clickable: false,
        };
        polyLine = new google.maps.Polyline(taxiRouteOptions);

        var buttonDiv = document.createElement('div');
        var button = new ButtonInit(buttonDiv, map);
        
        buttonDiv.index = 1;
        map.controls[google.maps.ControlPosition.TOP_RIGHT].push(buttonDiv);
        
        google.maps.event.addListener(map,'center_changed',function() { checkBounds(); });
        google.maps.event.addListener(map, 'zoom_changed', function() {
          if (map.getZoom() < minZoomLevel) map.setZoom(minZoomLevel);
          if (map.getZoom() > maxZoomLevel) map.setZoom(maxZoomLevel);
        });
      }
      
      function ButtonInit(buttonDiv, map) {

        buttonDiv.style.padding = '5px';

        // Set CSS for the control border
        var controlUI = document.createElement('div');
        controlUI.style.backgroundColor = 'white';
        controlUI.style.borderStyle = 'solid';
        controlUI.style.borderWidth = '1px';
        controlUI.style.cursor = 'pointer';
        controlUI.style.textAlign = 'center';
        buttonDiv.appendChild(controlUI);

        // Set CSS for the control interior
        var controlText = document.createElement('div');
        controlText.style.fontFamily = 'Arial,sans-serif';
        controlText.style.fontSize = '12px';
        controlText.style.paddingLeft = '4px';
        controlText.style.paddingRight = '4px';
        controlText.innerHTML = '<b>Send Route</b>';
        controlUI.appendChild(controlText);

        // Setup the click event listener
        // Start making async calls for route data
        google.maps.event.addDomListener(controlUI, 'click', sendRoute);
      }
      
      function sendRoute() {
          /*
          $.post("/postroute", JSON.stringify(route),
          function(){
            console.log("Sent route!");
            route = [];
            polyLine.getPath().clear(); 
            //TODO: clear the markers
          });
          */
          var tCode = prompt("Tranponder Code of Airplane")
          $.ajax({
            type: "POST",
            url: '/postroute',
            data: JSON.stringify({ "transponder" : tCode, "route" : route }),
            success: function() {
                console.log("Sent route!");
                route = [];
                polyLine.getPath().clear(); 
                displayTaxiways([0]);
              },
            statusCode: {
                403: function() {
                    alert('Error: Not authenticated');
                }
            }
          });
      }
      
      
      function containsPt(taxiway, pt) {
        var ans = false;
        taxiway.forEach(function (waypt) {
          if(wayPtEq(pt, waypt))
            ans = true;
        });
        return ans;
      }
      
      function findTaxiway(pt1, pt2) {
        var ans = null;
        if(pt1 != null && pt2 != null) {
          taxiways.forEach(function(taxi) {
            if(containsPt(taxi, pt1) && containsPt(taxi, pt2))
              ans = taxi;
          });
        }
        return ans;
      }
      
      function wayPtEq(ptA, ptB) {
        return ptA.Lat == ptB.Lat && ptA.Lng == ptB.Lng
          && ptA.Holdshort == ptB.Holdshort && ptA.Intersection == ptB.Intersection;
      }
      
      function addToPolyLine(taxi, lastWaypt, wayPt) {      
        if(taxi != null && lastWaypt != null && wayPt != null) {
          var found = false;
          var linePts = polyLine.getPath();
          for(i = 0; i < taxi.length; i++) {
            if(wayPtEq(lastWaypt, taxi[i])) {
              found = true;
              linePts.push(LatLng(taxi[i].Lat, taxi[i].Lng));
            } else if (wayPtEq(wayPt, taxi[i])) {
              if(found) {
                found = false;
                linePts.push(LatLng(taxi[i].Lat, taxi[i].Lng));
              } else {
                addToPolyLine(taxi.reverse(), lastWaypt, wayPt);
                return;
              }
            } else if (found) {
              linePts.push(LatLng(taxi[i].Lat, taxi[i].Lng));
            }
          }
        }
      }
      
      function displayTaxiways (indicies) {
        if(taxiways != null) {
          markers.forEach(function (marker) {
            marker.setMap(null);
          });
          markers = [];
          
          indicies.forEach(function (i) { 
            if(i >= 0 && i < taxiways.length) {
              taxiways[i].forEach(function (wayPt) {
                if(wayPt.Intersection || wayPt.Holdshort) {
                  var latlng = LatLng(wayPt.Lat, wayPt.Lng);
                  var marker = null;
                  if(wayPt.Holdshort) {
                    /*
                    marker = new google.maps.Marker({
                      icon: {
                          path: 'M -5,0 0,-5 5,0 0,5 z',
                          strokeColor: '#F00',
                          fillColor: '#F00',
                          fillOpacity: 1,
                      },
                      position: LatLng(wayPt.Lat, wayPt.Lng),
                      map: map,
                      clickable: false,
                    });
                    */
                  } else {
                    marker = new google.maps.Marker({ position: latlng, map: map });
                    google.maps.event.addListener(marker, 'click', function() {           
                      var lastWaypt = route.pop();
                      if(lastWaypt == null || !wayPtEq(lastWaypt, wayPt)) {
                        if(lastWaypt != null) 
                          route.push(lastWaypt);
                        route.push(wayPt);
                        var taxi = findTaxiway(lastWaypt, wayPt);
                        addToPolyLine(taxi, lastWaypt, wayPt);
                      } else {
                        var linePts = polyLine.getPath();
                        lastWaypt = route.pop();
                        if(lastWaypt != null) {
                          console.log("  (" + lastWaypt.Lat + ", " + lastWaypt.Lng + ")");
                          latlng = linePts.pop();
                          while(latlng != null && (latlng.lng() != lastWaypt.Lng || latlng.lat() != lastWaypt.Lat)) {
                            latlng = linePts.pop();
                            console.log("  " + latlng);
                          }
                          route.push(lastWaypt);
                        } else {
                          route = [];
                          linePts.clear();
                        }
                      }
                  
                      var ans = [];
                      for( j = 0; j < taxiways.length; j++) {
                        if(containsPt(taxiways[j], wayPt) && i != j)
                          ans.push(j);
                      }
                      console.log(ans);
                      displayTaxiways(ans);
                    });
                  }
                  if(marker != null)
                    markers.push(marker);
                }
              });
            }
          });
        }
      }
            
      function checkBounds(){
        if(! allowedBounds.contains(map.getCenter())) {
          var C = map.getCenter();
          var X = C.lng();
          var Y = C.lat();

          var AmaxX = allowedBounds.getNorthEast().lng();
          var AmaxY = allowedBounds.getNorthEast().lat();
          var AminX = allowedBounds.getSouthWest().lng();
          var AminY = allowedBounds.getSouthWest().lat();

          if (X < AminX) {X = AminX;}
          if (X > AmaxX) {X = AmaxX;}
          if (Y < AminY) {Y = AminY;}
          if (Y > AmaxY) {Y = AmaxY;}

          map.setCenter(new google.maps.LatLng(Y,X));
        }
      }

      function PVDOverlay(bounds, image, map) {

        // Now initialize all properties.
        this.bounds_ = bounds;
        this.image_ = image;
        this.map_ = map;

        // We define a property to hold the image's div. We'll
        // actually create this div upon receipt of the onAdd()
        // method so we'll leave it null for now.
        this.div_ = null;

        // Explicitly call setMap on this overlay
        this.setMap(map);
      }

      PVDOverlay.prototype.onAdd = function() {

        // Note: an overlay's receipt of onAdd() indicates that
        // the map's panes are now available for attaching
        // the overlay to the map via the DOM.

        // Create the DIV and set some basic attributes.
        var div = document.createElement('div');
        div.style.borderStyle = 'none';
        div.style.borderWidth = '0px';
        div.style.position = 'absolute';

        // Create an IMG element and attach it to the DIV.
        var img = document.createElement('img');
        img.src = this.image_;
        img.style.width = '100%';
        img.style.height = '100%';
        img.style.position = 'absolute';
        div.appendChild(img);

        // Set the overlay's div_ property to this DIV
        this.div_ = div;

        // We add an overlay to a map via one of the map's panes.
        // We'll add this overlay to the overlayLayer pane.
        var panes = this.getPanes();
        panes.overlayLayer.appendChild(div);
      }

      PVDOverlay.prototype.draw = function() {

        // Size and position the overlay. We use a southwest and northeast
        // position of the overlay to peg it to the correct position and size.
        // We need to retrieve the projection from this overlay to do this.
        var overlayProjection = this.getProjection();

        // Retrieve the southwest and northeast coordinates of this overlay
        // in latlngs and convert them to pixels coordinates.
        // We'll use these coordinates to resize the DIV.
        
        //swBounds = this.bounds_.getSouthWest();
        //neBounds = this.bounds_.getNorthEast();
        
        var sw = overlayProjection.fromLatLngToDivPixel(this.bounds_.getSouthWest());
        var ne = overlayProjection.fromLatLngToDivPixel(this.bounds_.getNorthEast());

        // Resize the image's DIV to fit the indicated dimensions.
        var div = this.div_;
        div.style.left = sw.x + 'px';
        div.style.top = ne.y + 'px';
        div.style.width = (ne.x - sw.x) + 'px';
        div.style.height = (sw.y - ne.y) + 'px';
      }

      PVDOverlay.prototype.onRemove = function() {
        this.div_.parentNode.removeChild(this.div_);
        this.div_ = null;
      }
      
      
    </script>
  </head>
  <body onload="initialize()">
  <div id="page" style="height: 100%; width: 100%">
      
        <div id="map_canvas" style="width:100%; height:100%;"></div>
  </div>
  
  </body>
</html>