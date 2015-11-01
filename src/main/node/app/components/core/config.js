(function() {
  'use strict';

  var config = angular.module('app.core');

  config.config(configure);

  configure.$inject = ['$stateProvider', '$locationProvider', '$httpProvider', 'routerConfigProvider'];

  /* @ngInject */
  function configure($stateProvider, $locationProvider, $httpProvider, routerConfig) {
      $stateProvider.state('about', {
        url: '/about',
        templateUrl: 'components/about/about.html',
        controller: 'AboutCtrl'
      });
      $stateProvider.state('help', {
        url: '/help',
        templateUrl: 'components/help/help.html',
        controller: 'HelpCtrl'
      });

    //.otherwise({
    //  redirectTo: '/'
    //});
    //
    routerConfig.config.$stateProvider = $stateProvider;

    $locationProvider.html5Mode(true);

    $httpProvider.defaults.withCredentials = true;
  }
})();
