'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('MainCtrl', ['$scope', '$window', 'userService', function($scope, $window, userService) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

    $scope.user = null;

    $scope.login = function(provider) {
      userService.login(provider).then(function() {
      	me();
      });
    };

    function me() {
      userService.me().then(function(user) {
        $scope.user = user;
      });
    }

  }]);
