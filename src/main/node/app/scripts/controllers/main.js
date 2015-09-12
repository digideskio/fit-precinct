'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('MainCtrl', ['$scope', '$window', '$q', 'userService', 'workoutService', 'momentService', 'mathToolbox', function($scope, $window, $q, userService, workoutService, moment, mathToolbox) {
    $scope.user = null;

    $scope.login = function(provider) {
      userService.login(provider).then(function() {
        me();
      });
    };

    function me() {
      userService.me().then(function(user) {
        $scope.user = user;

        loadDashboardData().then(function(result) {
          console.log(result, moment().to(moment(result.lastTrained)));
          $scope.latestWorkouts = result.latest;
          $scope.timeTrained = result.totalTime;
          $scope.lastTraining = result.lastTrained;
          $scope.lastTrained = moment().to(moment(result.lastTrained));
          $scope.trainingTrend = 'transform: rotate(' + result.trainingTrend + 'deg)';
        });
      });
    }

    me();

    $scope.battery = function(lastTraining) {
      var diff = moment().diff(moment(lastTraining), 'hours');
      if (diff > 36) {
        return 'fa-battery-4';
      } else if (diff > 27) {
        return 'fa-battery-3';
      } else if (diff > 18) {
        return 'fa-battery-2';
      } else if (diff > 9) {
        return 'fa-battery-1';
      } else {
        return 'fa-battery-0';
      }
    };

    function loadDashboardData() {
      var deferred = $q.defer();

      $q.all([workoutService.listLast(3), workoutService.list(moment().add(-30, 'days').unix() * 1000)])
        .then(function(result) {
          var latest = result[0];
          var month = result[1];

          var endOfLastTraining = latest.data[0].startTime + latest.data[0].data.clockDuration;
          var totalTime = 0;
          var n = [0, 0, 0, 0, 0];
          var trendZoneStart = moment().startOf('day').add(-28, 'days');
          angular.forEach(month.data, function(workout) {
            totalTime += workout.data.duration;
            var diff = Math.abs(trendZoneStart.diff(moment(workout.startTime), 'days'));
            var zone = Math.floor(diff / 7);
            n[zone]++;
          });

          console.log(n);
          var trendLine = mathToolbox.findLineByLeastSquares([0, 1, 2, 3, 4], n);
          var delta = trendLine[1][trendLine.length - 2] - trendLine[1][trendLine.length - 1];
          var degree = Math.tan(1 / delta) * Math.PI;

          deferred.resolve({
            latest: latest.data,
            totalTime: totalTime,
            lastTrained: endOfLastTraining,
            trainingTrend: degree
          });
        });

      return deferred.promise;
    }
  }]);
