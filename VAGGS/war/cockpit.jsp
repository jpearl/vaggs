<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map_canvas { height: 100% }
    </style>
    <script type="text/javascript"
      src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAK0HG-UZtFAiPtHkyc6xjxI2wQXCOX4Pk&sensor=false">
    </script>
    <script type="text/javascript">
      var kpvdTypeOptions = {
      getTileUrl: function(coord, zoom) {
            return "http://flightaware.com/resources/airport/PVD/APD/AIRPORT+DIAGRAM/png/1"
            },
      tileSize: new google.maps.Size(256, 256),
      maxZoom: 9,
      minZoom: 0,
      radius: 1738000,
      name: "kpvd"
    };
    
    var kpvdMapType = new google.maps.ImageMapType(kpvdTypeOptions);


      function initialize() {
        var mapOptions = {
          //center: new google.maps.LatLng(41.7258, -71.4368),
          center: new google.maps.LatLng(0, 0),
          zoom: 4,
          //mapTypeId: kpvdMapType,
          mapTypeControlOptions: {
                mapTypeIds: ["kpvdMapType"]
            },
          //disableDefaultUI: true,
          //draggable: false,
          //scrollwheel: false
        };
        var map = new google.maps.Map(document.getElementById("map_canvas"),
            mapOptions);
        map.mapTypes.set('kpvd', kpvdMapType);
        map.setMapTypeId('kpvd');
      }
    </script>
  </head>
  <body onload="initialize()">
  <div id="page" style="height: 100%; width: 100%">
      <p>Welcome to the cockpit!</p>
        <div id="map_canvas" style="width:100%; height:92%;"></div>
  </div>
  </body>
</html>