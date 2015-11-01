(function() {
  'use strict';

  var module = angular.module('app.workouts');

  config.$inject = ['router'];
  module.run(config);

  function config(router) {
    router.state('workouts', {
      url: '/workouts',
      templateUrl: 'components/workouts/workouts.html',
      controller: 'Workouts'
    });

    router.state('workout', {
      url: '/workout/:id',
      templateUrl: 'components/workouts/workout.html',
      controller: 'Workout'
    });
  }
})();
