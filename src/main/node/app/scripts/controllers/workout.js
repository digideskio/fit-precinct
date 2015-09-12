'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:WorkoutCtrl
 * @description
 * # WorkoutCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('WorkoutCtrl', ['$scope', '$stateParams', '$q', 'workoutService', 'mathToolbox', 'openlayersService', function($scope, $stateParams, $q, workoutService, mathToolbox, olService) {
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
      console.log(e);
      workoutService.update($scope.workoutHead).then(function(result) {
        console.log(result);
        angular.element(modal).modal('hide');
      });
    };

    $scope.mapSource = olService.source.mapQuest;

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

      loadMapData($scope.map);
      createCharts();

      $scope.loading = false;

    });

    function createCharts() {
      var datasets = normalizeData('heartrate');
      var tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
      var heartrate = prepareData(tmpData);

      datasets = normalizeData('cadence');
      tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
      var cadence = prepareData(tmpData);

      datasets = normalizeData('elevation');
      tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
      var elevation = prepareData(tmpData);

      datasets = normalizeData('distance');
      tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
      var distance = prepareData(tmpData, 1 / 1000);
      var derivedDatasets = mathToolbox.derive(tmpData);
      var speed = prepareData(derivedDatasets, 3.6);

      // var foo = plot('heartrate', 50);
      // $scope.labels = foo.labels;
      $scope.charts = [];
      $scope.charts.push({
        series: ['Distance'],
        labels: distance.labels,
        data: [distance.data],
        boxtype: 'box-info',
        options: {
          bezierCurve: true,
          scaleShowVerticalLines: false,
          pointHitDetectionRadius: 1
        }
      });
      if (cadence && cadence.data && cadence.data.length > 0) {
        $scope.charts.push({
          series: ['Cadence'],
          labels: cadence.labels,
          data: [cadence.data],
          boxtype: 'box-success'
        });
      }
      $scope.charts.push({
        series: ['Elevation'],
        labels: elevation.labels,
        data: [elevation.data]
      });
      $scope.charts.push({
        series: ['Speed'],
        labels: speed.labels,
        data: [speed.data],
        boxtype: 'box-warning'
      });
      $scope.charts.push({
        series: ['Heartrate'],
        labels: heartrate.labels,
        data: [heartrate.data],
        boxtype: 'box-danger'
      });
    }

    function normalizeData(dataType) {
      var datasets = [];
      angular.forEach($scope.workout[dataType], function(data) {
        datasets.push([data.offset, data.data[0]]);
      });
      return datasets;
    }

    function prepareData(datasets, factor) {
      factor = factor || 1;
      var labels = [];
      var data = [];
      angular.forEach(datasets, function(dataset) {
        labels.push(dataset[0]);
        data.push(dataset[1] * factor);
      });

      return {
        labels: labels,
        data: data
      };
    }
  }]);
