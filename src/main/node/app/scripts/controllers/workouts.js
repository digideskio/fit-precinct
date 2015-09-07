'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:WorkoutsCtrl
 * @description
 * # WorkoutsCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('WorkoutsCtrl', ['$scope', '$filter', 'workoutService', 'momentService', function($scope, $filter, workoutService, moment) {
    $scope.loading = true;
    $scope.icon = {
      'walking': 'fa icon-run bg-green',
      'cycling': 'fa icon-bicycle bg-yellow'
    };

    $scope.workouts = null;
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
          symbol: workout.type.toLowerCase()
        });
      });
    });
  }]);
