(function() {
  'use strict';

  var module = angular.module('app.user');

  config.$inject = ['router'];
  module.run(config);

  function config(router) {
    router.state('user', {
      url: '/user',
      templateUrl: 'components/user/user.html',
      controller: 'User'
    });
  }
})();
