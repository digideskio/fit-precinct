'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:HelpCtrl
 * @description
 * # HelpCtrl
 * Controller of the nodeApp
 */
angular.module('app')
  .controller('HelpCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
