(function() {
  'use strict';

  var module = angular.module('app.login');

  module.run(config);

  config.$inject = ['router'];

  function config(router) {
    router.state('login', {
      url: '/',
      templateUrl: 'components/login/login.html',
      controller: 'Login'
    });
  }

})();
