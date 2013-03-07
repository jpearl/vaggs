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
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAK0HG-UZtFAiPtHkyc6xjxI2wQXCOX4Pk&sensor=false"></script>
    <script type="text/javascript">
      var route;
      var transponder = 0;
      var overlay;

      PVDOverlay.prototype = new google.maps.OverlayView();

      function TransponderCall() {
        var tCode = transponder;
        $.getJSON("route?transponder=" + tCode, function(json) {
          console.log("Got route info for transponder code: " + tCode);
          route = json;
        }).complete(function() {
          setTimeout(TransponderCall, 2500);
        }).error(function() {
          console.log("Failed to get route info for transponder code: " + tCode);
        });
      }
      
      function initialize() {
        $.ajax({cache:false});
        TransponderCall();
        
        var myLatLng = new google.maps.LatLng(41.7258, -71.4368);
        var mapOptions = {
          zoom: 14,
          center: myLatLng,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };

        var map = new google.maps.Map(document.getElementById('map_canvas'), mapOptions);
        
        //sanity check for coordinates
        new google.maps.Marker({ position: new google.maps.LatLng(41.725, -71.433333), map: map });

        var swBound = new google.maps.LatLng(41.7087976, -71.44134);
        var neBound = new google.maps.LatLng(41.73783, -71.41615);
        
        var bounds = new google.maps.LatLngBounds(swBound, neBound);

        //kpvd airport diagram
        var srcImage = 'airports/pvd2.png';
        overlay = new PVDOverlay(bounds, srcImage, map);
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