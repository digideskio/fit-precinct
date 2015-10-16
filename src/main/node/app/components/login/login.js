'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:LoginCtrl
 * @description
 * # LoginCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('LoginCtrl', ['$scope', '$state', 'userService', function($scope, $state, userService) {

    userService.me().then(function() {
      $state.go('dashboard');
    });

    $scope.login = function(provider) {
      userService.login(provider).then(function() {
        $state.go('dashboard');
      });
    };
  }]);
