'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:WorkoutCtrl
 * @description
 * # WorkoutCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('WorkoutCtrl', ['$scope', '$stateParams', 'workoutService', function($scope, $stateParams, workoutService) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

    $scope.workout = {};

    workoutService.get($stateParams.id).then(function(result) {
      angular.forEach(result.data, function(data) {
        if (data.dataSet) {
          if (typeof($scope.workout[data.type]) === 'undefined') {
            $scope.workout[data.type] = data.dataSet;
          } else {
            $scope.workout[data.type].push.apply($scope.workout[data.type], data.dataSet);
          }
        } else {
          angular.extend($scope.workout, data);
        }
      });


      console.log($scope.workout);
      // var speed = derivePlot('distance', 50);
      var datasets = normalizeData('heartrate');
      var tmpData = largestTriangleThreeBuckets(datasets, 50);
      var heartrate = prepareData(tmpData);

      datasets = normalizeData('cadence');
      tmpData = largestTriangleThreeBuckets(datasets, 50);
      var cadence = prepareData(tmpData);

      datasets = normalizeData('distance');
      tmpData = largestTriangleThreeBuckets(datasets, 50);
      var distance = prepareData(tmpData);
      var derivedDatasets = derive(tmpData);
      var speed = prepareData(derivedDatasets);

      // var foo = plot('heartrate', 50);
      // $scope.labels = foo.labels;
      $scope.charts = [];
      $scope.charts.push({
        series: ['Distance'],
        labels: distance.labels,
        data: [distance.data]
      });
      $scope.charts.push({
        series: ['Cadence'],
        labels: cadence.labels,
        data: [cadence.data]
      });
      $scope.charts.push({
        series: ['Speed'],
        labels: speed.labels,
        data: [speed.data]
      });
      $scope.charts.push({
        series: ['Heartrate'],
        labels: heartrate.labels,
        data: [heartrate.data]
      });
    });

    function derive(datasets) {
      var output = [];
      var prevX = 0;
      var prevY = 0;
      angular.forEach(datasets, function(data) {
        var diffX = data[0] - prevX;
        var diffY = data[1] - prevY;

        var value = diffY / diffX;
        // console.log(diffX, diffY, value);
        if (!isNaN(value)) {
          output.push([data[0], value]);
        }

        prevX = data[0];
        prevY = data[1];
      });

      return output;
    }

    function normalizeData(dataType) {
      var datasets = [];
      angular.forEach($scope.workout[dataType], function(data) {
        datasets.push([data.offset, data.data[0]]);
      });
      return datasets;
    }

    function prepareData(datasets) {
      var labels = [];
      var data = [];
      angular.forEach(datasets, function(dataset) {
        labels.push(dataset[0]);
        data.push(dataset[1]);
      });

      return {
        labels: labels,
        data: data
      };
    }

    /**
     * https://github.com/sveinn-steinarsson/flot-downsample/
     * @param  {[type]} data      [description]
     * @param  {[type]} threshold [description]
     * @return {[type]}           [description]
     */
    function largestTriangleThreeBuckets(data, threshold) {
      var floor = Math.floor,
        abs = Math.abs;

      var dataLength = data.length;
      if (threshold >= dataLength || threshold === 0) {
        return data; // Nothing to do
      }

      var sampled = [],
        sampledIndex = 0;

      // Bucket size. Leave room for start and end data points
      var every = (dataLength - 2) / (threshold - 2);

      var a = 0, // Initially a is the first point in the triangle
        maxAreaPoint,
        maxArea,
        area,
        nextA;

      sampled[sampledIndex++] = data[a]; // Always add the first point

      for (var i = 0; i < threshold - 2; i++) {

        // Calculate point average for next bucket (containing c)
        var avgX = 0,
          avgY = 0,
          avgRangeStart = floor((i + 1) * every) + 1,
          avgRangeEnd = floor((i + 2) * every) + 1;
        avgRangeEnd = avgRangeEnd < dataLength ? avgRangeEnd : dataLength;

        var avgRangeLength = avgRangeEnd - avgRangeStart;

        for (; avgRangeStart < avgRangeEnd; avgRangeStart++) {
          avgX += data[avgRangeStart][0] * 1; // * 1 enforces Number (value may be Date)
          avgY += data[avgRangeStart][1] * 1;
        }
        avgX /= avgRangeLength;
        avgY /= avgRangeLength;

        // Get the range for this bucket
        var rangeOffs = floor((i + 0) * every) + 1,
          rangeTo = floor((i + 1) * every) + 1;

        // Point a
        var pointAX = data[a][0] * 1, // Enforce Number (value may be Date)
          pointAY = data[a][1] * 1;

        maxArea = area = -1;

        for (; rangeOffs < rangeTo; rangeOffs++) {
          // Calculate triangle area over three buckets
          area = abs((pointAX - avgX) * (data[rangeOffs][1] - pointAY) -
            (pointAX - data[rangeOffs][0]) * (avgY - pointAY)
          ) * 0.5;
          if (area > maxArea) {
            maxArea = area;
            maxAreaPoint = data[rangeOffs];
            nextA = rangeOffs; // Next a is this b
          }
        }

        sampled[sampledIndex++] = maxAreaPoint; // Pick this point from the bucket
        a = nextA; // This a is the next a (chosen b)
      }

      sampled[sampledIndex++] = data[dataLength - 1]; // Always add last

      return sampled;
    }
  }]);
