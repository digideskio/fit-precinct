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

    $scope.rightOptions = {
      // multiplier: 3.6,
      // description: 'Speed (km/h)'
      description: 'Heartrate (bpm)',
      interpolate: 60
    };
    $scope.leftOptions = {
      description: 'Elevation (m)'
    }

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
          // console.log(diffX, diffY, value);
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
          description: 'Speed (km/s)',
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
          description: 'Speed (km/s)',
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

      /*      var datasets = normalizeData('heartrate');
            var nvdHeartrate = normalizeData('heartrate', true);
            nvdHeartrate = mathToolbox.largestTriangleThreeBuckets(nvdHeartrate, 100);
            nvdHeartrate = prepareDataNvd(nvdHeartrate, 1, 1);
            var tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
            var heartrate = prepareData(tmpData);

            datasets = normalizeData('cadence');
            tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
            var cadence = prepareData(tmpData);

            datasets = normalizeData('elevation');
            tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
            var elevation = prepareData(tmpData);
            var nvdElevation = normalizeData('elevation', true);
            nvdElevation = mathToolbox.largestTriangleThreeBuckets(datasets, 100);
            nvdElevation = prepareDataNvd(nvdElevation);

            datasets = normalizeData('distance');
            tmpData = mathToolbox.largestTriangleThreeBuckets(datasets, 50);
            var distance = prepareData(tmpData, 1 / 1000);
            var derivedDatasets = mathToolbox.derive(tmpData);
            var speed = prepareData(derivedDatasets, 3.6);
            var nvdDistance = normalizeData('distance', true);
            nvdDistance = mathToolbox.largestTriangleThreeBuckets(nvdDistance, 100);
            var nvdSpeed = mathToolbox.derive(nvdDistance);
            nvdSpeed = prepareDataNvd(nvdSpeed, 3.6, 1);


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

            // console.log(nvdSpeed);

            $scope.nvdchart = {
              data: [{
                'type': 'line',
                'yAxis': 1,
                'color': 'red',
                'key': 'Speed',
                'values': nvdSpeed
              }, {
                'type': 'area',
                'yAxis': 2,
                'key': 'Elevation',
                'values': nvdElevation
              }],
              options: {
                chart: {
                  type: 'multiChart',
                  useInteractiveGuideline: true,
                  transitionDuration: 250,
                  // interpolate: 'cardinal',
                  // showLegend: false,
                  height: 300,
                  xAxis: {
                    axisLabel: 'Time (min)',
                  },
                  y1Axis: {
                    axisLabel: 'Heartrate (bpm)'
                  },
                  y2Axis: {
                    axisLabel: 'Speed (bpm)'
                  }
                }
              }
            };*/
    }

    function normalizeData(dataType, parse) {
      parse = parse || false;
      var datasets = [];
      angular.forEach($scope.workout[dataType], function(data) {
        if (parse) {
          datasets.push([data.offset, parseFloat(data.data[0])]);
        } else {
          datasets.push([data.offset, data.data[0]]);
        }
      });
      return datasets;
    }

    function prepareDataNvd(datasets, factorY, factorX) {
      factorY = factorY || 1;
      factorX = factorX || 1;
      var data = [];
      angular.forEach(datasets, function(dataset) {
        data.push({
          x: dataset[0] * factorX,
          y: dataset[1] * factorY
        });
      });

      return data;
    }

    function prepareData(datasets, factor) {
      factor = factor || 1;
      var labels = [];
      var data = [];
      angular.forEach(datasets, function(dataset) {
        labels.push(dataset[0]);
        data.push(parseFloat(dataset[1] * factor));
      });

      return {
        labels: labels,
        data: data
      };
    }
  }]);
