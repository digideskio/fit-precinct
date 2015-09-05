'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:WorkoutsCtrl
 * @description
 * # WorkoutsCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('WorkoutsCtrl', ['$scope', 'workoutService', function ($scope, workoutService) {
  	$scope.workouts = null;
  	workoutService.list().then(function(result){
  		$scope.workouts = result.data;
  		// console.log($scope.workouts);
  	});
  }]);
