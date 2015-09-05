'use strict';

/**
 * @ngdoc overview
 * @name nodeApp
 * @description
 * # nodeApp
 *
 * Main module of the application.
 */
angular
  .module('nodeApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ui.router',
    'ngSanitize',
    'ngTouch',
    'chart.js'
  ])
  .config(['$stateProvider', '$locationProvider', function ($stateProvider, $locationProvider) {
    $stateProvider
      .state('root', {
        url: '/',
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .state('about', {
        url: '/about',
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .state('user', {
        url: '/user',
        templateUrl: 'views/user.html',
        controller: 'UserCtrl'
      })
      .state('workouts', {
        url: '/workouts',
        templateUrl: 'views/workouts.html',
        controller: 'WorkoutsCtrl'
      })
      .state('workout', {
        url: '/workout/:id',
        templateUrl: 'views/workout.html',
        controller: 'WorkoutCtrl'
      });
      //.otherwise({
      //  redirectTo: '/'
      //});

      $locationProvider.html5Mode(true);
  }]);
