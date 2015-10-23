(function() {
  'use strict';

  var module = angular.module('app.dashboard');

  config.$inject = ['router'];
  module.run(config);

  function config(router) {
    router
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: 'components/dashboard/dashboard.html',
        controller: 'Dashboard'
      });
  }
})();
