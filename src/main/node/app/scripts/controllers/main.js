'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('MainCtrl', ['$scope', 'userService', function ($scope, userService) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];

    userService.me();
  }]);
