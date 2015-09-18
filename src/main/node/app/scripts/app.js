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
  .config(['$stateProvider', '$locationProvider', '$httpProvider', function($stateProvider, $locationProvider, $httpProvider) {
    $stateProvider
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .state('login', {
        url: '/',
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl'
      })
      .state('about', {
        url: '/about',
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'
      })
      .state('help', {
        url: '/help',
        templateUrl: 'views/help.html',
        controller: 'HelpCtrl'
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

    $httpProvider.defaults.withCredentials = true;
  }]);


angular.module('nodeApp')
  .factory('apiLocation', ['$window', function apiLocation($window) {
    if ($window.location.hostname === 'localhost') {
      return 'http://localhost:8080';
    } else {
      return 'https://' + $window.location.hostname;
    }
  }]);
