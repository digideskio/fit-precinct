'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:WorkoutsCtrl
 * @description
 * # WorkoutsCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('WorkoutsCtrl', ['$scope', '$filter', '$state', '$q', '$timeout', 'workoutService', 'momentService', function($scope, $filter, $state, $q, $timeout, workoutService, moment) {
    $scope.loading = true;
    $scope.icon = {
      'unknown': 'fa icon-question',
      'walking': 'fa icon-run bg-green',
      'cycling': 'fa icon-bicycle bg-yellow'
    };

    $scope.checkedWorkouts = [];

    $scope.concat = {};
    $scope.concat.workoutHead = {};
    $scope.concat.toggleWorkout = function(workout) {
      if (workout.checked) {
        $scope.checkedWorkouts.push(workout);
      } else {
        $scope.checkedWorkouts.splice($scope.checkedWorkouts.indexOf(workout), 1);
      }
    };
    $scope.concat.prepare = function() {
      $scope.checkedWorkouts.sort(function(a, b) {
        return a.startTime - b.startTime
      });
      // deep copy header
      $scope.concat.workoutHead = angular.fromJson(angular.toJson($scope.checkedWorkouts[0]));
    };
    $scope.concat.action = function(e, modal) {
      $scope.loading = true;
      var workouts = [];
      angular.forEach($scope.checkedWorkouts, function(workout) {
        workouts.push(workout.id);
      });
      delete $scope.concat.workoutHead.checked;
      workoutService.concat(workouts, $scope.concat.workoutHead).then(function(result) {
        $timeout(function() {
          $scope.loading = false;
          angular.element(modal).modal('hide');
          $scope.$apply(function(scope) {
            scope.load();
          });
        }, 2000);
      });
    };

    $scope.delete = {};
    $scope.delete.count = 0;
    $scope.delete.prepare = function() {
      $scope.delete.count = $scope.checkedWorkouts ? $scope.checkedWorkouts.length : 0;
    };
    $scope.delete.action = function(e, modal) {
      $scope.loading = true;
      var deletes = [];
      angular.forEach($scope.checkedWorkouts, function(workout) {
        deletes.push(workoutService.delete(workout.id));
      });
      $q.all(deletes).then(function(result) {
        // wait for eventual consistency
        $timeout(function() {
          $scope.loading = false;
          angular.element(modal).modal('hide');
          $scope.load();
        }, 1000);
      });
    };

    $scope.load = function() {
      console.log('loading workouts');
      workoutService.list().then(function(result) {
        $scope.timeline = [];
        var currentDate = null;
        angular.forEach($filter('orderBy')(result.data, 'startTime', true), function(workout) {
          var date = moment(workout.startTime).format('ll');
          if (date !== currentDate) {
            $scope.timeline.push({
              type: 'date',
              value: date
            });
            currentDate = date;
          }
          $scope.timeline.push({
            type: 'workout',
            value: workout,
            symbol: (workout.type || 'unknown').toLowerCase()
          });
        });

        $scope.loading = false;
      }, function(error) {
        if (error.status == 401) {
          $state.go('login');
        }
      });
    }

    $scope.load();
  }]);
