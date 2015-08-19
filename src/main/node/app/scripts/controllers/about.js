'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:AboutCtrl
 * @description
 * # AboutCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('AboutCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
