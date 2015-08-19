'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
