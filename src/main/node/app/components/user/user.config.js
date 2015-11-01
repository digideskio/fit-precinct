(function() {
  'use strict';

  var module = angular.module('app.user');

  module.run(config);

  config.$inject = ['router'];
  function config(router) {
    router.state('user', {
      url: '/user',
      templateUrl: 'components/user/user.html',
      controller: 'User'
    });
  }
})();
