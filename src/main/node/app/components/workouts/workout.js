(function() {
  'use strict';

  var module = angular.module('app.workouts');

  module.controller('Workout', Workout);

  Workout.$inject = ['$scope', '$state', '$stateParams', '$q', '$timeout', 'workoutService', 'mathToolbox', 'openlayersService'];

  function Workout($scope, $state, $stateParams, $q, $timeout, workoutService, mathToolbox, olService) {
    $scope.loading = true;

    $scope.dataset = [{
      data: [],
      yaxis: 1,
      label: 'sin'
    }];
    $scope.options = {
      legend: {
        container: '#legend',
        show: true
      }
    };

    $scope.rightOptions = {
      // multiplier: 3.6,
      // description: 'Speed (km/h)'
      description: 'Heartrate (bpm)',
      interpolate: 60
    };
    $scope.leftOptions = {
      description: 'Elevation (m)'
    };

    for (var i = 0; i < 14; i += 0.5) {
      $scope.dataset[0].data.push([i, Math.sin(i)]);
    }

    $scope.icon = function(name) {
      name = name || '';
      return {
        'walking': 'icon-run',
        'cycling': 'icon-bicycle'
      }[name.toLowerCase()];
    };

    $scope.updateHead = function(e, modal) {
      $scope.loading = true;
      workoutService.update($scope.workoutHead).then(function(result) {
        console.log(result);
        $scope.loading = false;
        angular.element(modal).modal('hide');
      });
    };

    $scope.delete = function(e, modal) {
      $scope.loading = true;
      console.log('deleting workout ' + $scope.workoutHead.id, modal);
      workoutService.delete($scope.workoutHead.id).then(function(result) {
        console.log(result);
        $scope.loading = false;
        angular.element(modal).modal('hide');
        $timeout(function() {
          $state.go('workouts');
        }, 500);
      });
    };

    $scope.mapSource = olService.source.osm;

    $scope.map = new olService.ol.Map({
      target: 'map',
      layers: $scope.mapSource,
      view: new olService.ol.View({
        center: [0, 0],
        zoom: 4
      })
    });

    function loadMapData(map) {
      var points = [];
      angular.forEach($scope.workout.location, function(location) {
        var point = [parseFloat(location.data[1]), parseFloat(location.data[0])];
        point = olService.ol.proj.fromLonLat(point, 'EPSG:900913');
        points.push(point);
      });

      var styles = new olService.ol.style.Style({
        stroke: new olService.ol.style.Stroke({
          color: '#0500bd',
          width: 5
        })
      });

      var layerLines = new olService.ol.layer.Vector({
        style: styles,
        source: new olService.ol.source.Vector({
          features: [new olService.ol.Feature({
            geometry: new olService.ol.geom.LineString(points),
            name: 'Line'
          })]
        })
      });

      map.addLayer(layerLines);


      var start = [parseFloat($scope.workout.location[0].data[1]), parseFloat($scope.workout.location[0].data[0])];
      start = olService.ol.proj.fromLonLat(start, 'EPSG:900913');
      map.getView().setCenter(start);
      map.getView().setZoom(13);
    }

    $scope.workoutHead = {};
    $scope.workout = {};

    workoutService.load($stateParams.id).then(function(result) {
      $scope.workoutHead = result.head;
      $scope.workout = result.data;
      fill();

      loadMapData($scope.map);
      createCharts();

      $scope.loading = false;

    });

    function fill() {
      if (!$scope.workout.speed && $scope.workout.distance) {
        var output = [];

        var prevX = 0;
        var prevY = 0;
        angular.forEach($scope.workout.distance, function(data) {
          var diffX = data.offset - prevX;
          var diffY = data.data[0] - prevY;

          var value = diffY / diffX;
          if (!isNaN(value)) {
            output.push({
              offset: data.offset,
              data: [value]
            });
          } else {
            output.push({
              offset: data.offset,
              data: [0]
            });
          }

          prevX = data.offset;
          prevY = data.data[0];
        });

        $scope.workout.speed = output;
      }
    }

    function createCharts() {
      $scope.charts = [];
      $scope.charts.push({
        title: 'Speed / Elevation',
        boxtype: 'box-info',
        leftData: $scope.workout.speed,
        leftOptions: {
          multiplier: 3.6,
          description: 'Speed (km/h)',
          interpolate: 20
        },
        rightData: $scope.workout.elevation,
        rightOptions: {
          description: 'Elevation (m)',
          interpolate: 60
        }
      });

      $scope.charts.push({
        title: 'Speed / Heartrate',
        boxtype: 'box-info',
        leftData: $scope.workout.speed,
        leftOptions: {
          multiplier: 3.6,
          description: 'Speed (km/h)',
          interpolate: 20
        },
        rightData: $scope.workout.heartrate,
        rightOptions: {
          description: 'Heartrate (bpm)',
          interpolate: 20
        }
      });

      $scope.charts.push({
        title: 'Elevation / Heartrate',
        boxtype: 'box-info',
        leftData: $scope.workout.elevation,
        leftOptions: {
          description: 'Elevation (m)',
          interpolate: 60
        },
        rightData: $scope.workout.heartrate,
        rightOptions: {
          description: 'Heartrate (bpm)',
          interpolate: 20
        }
      });

      $scope.charts.push({
        title: 'Cadence / Heartrate',
        boxtype: 'box-info',
        leftData: $scope.workout.cadence,
        leftOptions: {
          description: 'Cadence (1)',
          interpolate: 60
        },
        rightData: $scope.workout.heartrate,
        rightOptions: {
          description: 'Heartrate (bpm)',
          interpolate: 20
        }
      });
    }
  }
})();
