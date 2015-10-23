(function() {
  'use strict';
  var module = angular.module('app.login');

  module.controller('Login', Login);

  Login.$inject = ['$scope', '$state', 'userService'];

  function Login($scope, $state, userService) {

    userService.me().then(function() {
      $state.go('dashboard');
    });

    $scope.login = function(provider) {
      userService.login(provider).then(function() {
        $state.go('dashboard');
      });
    };
  }
})();
