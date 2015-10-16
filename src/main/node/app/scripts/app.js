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
    'ngTouch'
  ])
  .config(['$stateProvider', '$locationProvider', '$httpProvider', function($stateProvider, $locationProvider, $httpProvider) {
    $stateProvider
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: 'components/dashboard/dashboard.html',
        controller: 'DashboardCtrl'
      })
      .state('login', {
        url: '/',
        templateUrl: 'components/login/login.html',
        controller: 'LoginCtrl'
      })
      .state('about', {
        url: '/about',
        templateUrl: 'components/about/about.html',
        controller: 'AboutCtrl'
      })
      .state('help', {
        url: '/help',
        templateUrl: 'components/help/help.html',
        controller: 'HelpCtrl'
      })
      .state('user', {
        url: '/user',
        templateUrl: 'components/user/user.html',
        controller: 'UserCtrl'
      })
      .state('workouts', {
        url: '/workouts',
        templateUrl: 'components/workouts/workouts.html',
        controller: 'WorkoutsCtrl'
      })
      .state('workout', {
        url: '/workout/:id',
        templateUrl: 'components/workout/workout.html',
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
