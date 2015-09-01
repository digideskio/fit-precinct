'use strict';

/**
 * @ngdoc function
 * @name nodeApp.controller:UserCtrl
 * @description
 * # UserCtrl
 * Controller of the nodeApp
 */
angular.module('nodeApp')
  .controller('UserCtrl', ['$scope', 'userService', function($scope, userService) {
    $scope.user = null;
    $scope.uploadSettings = {};
    $scope.status = null;

    function me() {
      userService.me().then(function(user) {
        $scope.user = user;
      });
    }

    me();

    $scope.setUploadSettings = function($event) {
      $event.preventDefault();
      userService.setUploadUser($scope.uploadSettings.username, $scope.uploadSettings.password).then(function(result) {
      	console.log(result);
				$scope.status = 'OK';
      }, function(result){
      	console.log('error', result);
      });
    };

  }]);
