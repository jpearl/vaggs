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
      var waypoints = [];
      var holdshort = [];
      
      
      PVDOverlay.prototype = new google.maps.OverlayView();

      function LatLng(lat, lng) {
        return new google.maps.LatLng(lat, lng);
      }
      
      function initialize() {
        $.ajax({cache:false});
        
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
        
        taxiRouteOptions = {
          strokeColor: "#00FF00",
          strokeOpacity: 1.0,
          strokeWeight: 3,
          map: map,
          clickable: false,
        };
        polyLine = new google.maps.Polyline(taxiRouteOptions);

        var buttonDiv = document.createElement('div');
        var button = new ButtonInit(buttonDiv, map, "Generate Waypoints", generateAirportInfo);
        var buttonDiv2 = document.createElement('div');
        var button2 = new ButtonInit(buttonDiv, map, "New Taxiway", defineNewTaxiway)
        
        buttonDiv.index = 1;
        map.controls[google.maps.ControlPosition.TOP_RIGHT].push(buttonDiv);
        
        google.maps.event.addListener(map,'center_changed',function() { checkBounds(); });
        google.maps.event.addListener(map, 'zoom_changed', function() {
          if (map.getZoom() < minZoomLevel) map.setZoom(minZoomLevel);
          if (map.getZoom() > maxZoomLevel) map.setZoom(maxZoomLevel);
        });
        
        google.maps.event.addListener(map, 'click', addWaypoint);
        google.maps.event.addListener(map, 'rightclick', addWaypointHoldshort);
      }
      
      function defineNewTaxiway() {
        var taxiway = prompt('Please enter taxiway/runway name');
        console.log("Taxiway taxiway = new Taxiway('" + taxiway + "'); \n taxiway.setWaypoints(Arrays.asList(");
      }
      
      function addWaypoint(mouseEvent) {
        loc = mouseEvent.latLng;
        waypoints.push(loc);
        holdshort.push(false);
        marker = new google.maps.Marker({
            map: map,
            position: loc
        });
        google.maps.event.addListener(marker, 'click', addWaypoint);
      }
      
      function addWaypointHoldshort(mouseEvent) {
        addWaypoint(mouseEvent);
        holdshort[holdshort.length-1] = true;
      }
      
      function ButtonInit(buttonDiv, map, text, onclick) {

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
        controlText.innerHTML = '<b>' + text + '</b>';
        controlUI.appendChild(controlText);

        // Setup the click event listener
        // Start making async calls for route data
        google.maps.event.addDomListener(controlUI, 'click', onclick);
      }
      
      function generateAirportInfo() {
          for(i=0; i<waypoints.length; i++) {
            console.log('new Waypoint(new LatLng(' + waypoints[i].lat() + ', ' + waypoints[i].lng() + '), ' + holdshort[i] + '),');
          }
          waypoints = [];
          holdshort = [];
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