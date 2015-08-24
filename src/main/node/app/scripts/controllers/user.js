'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:UserCtrl
 * @description
 * # UserCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('UserCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
