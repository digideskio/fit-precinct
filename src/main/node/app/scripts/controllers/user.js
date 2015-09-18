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
    $scope.localStorageAvailable = userService.localStorageAvailable();
    if ($scope.localStorageAvailable) {
      $scope.localStorage = true;

      userService.useLocalStorage($scope.localStorage);
      $scope.profile = userService.getProfile();
    }

    $scope.user = {};
    $scope.uploadSettings = {};
    $scope.status = null;
    $scope.user.imgUrl = 'http://placehold.it/100x100';

    $scope.updateHr = function() {
      if ($scope.profile.linkHr && $scope.profile.age) {
        $scope.profile.hr100 = (220 - $scope.profile.age).toFixed(1);
        $scope.profile.hr90 = ($scope.profile.hr100 * 0.9).toFixed(1);
        $scope.profile.hr80 = ($scope.profile.hr100 * 0.8).toFixed(1);
        $scope.profile.hr70 = ($scope.profile.hr100 * 0.7).toFixed(1);
        $scope.profile.hr60 = ($scope.profile.hr100 * 0.6).toFixed(1);
        $scope.profile.hr50 = ($scope.profile.hr100 * 0.5).toFixed(1);
      }
    };

    function me() {
      userService.me().then(function(result) {
        $scope.user = result.data;
      });
    }

    me();

    $scope.setUploadSettings = function($event) {
      $event.preventDefault();
      userService.setUploadUser($scope.uploadSettings.username, $scope.uploadSettings.password).then(function(result) {
        console.log(result);
        $scope.status = 'OK';
      }, function(result) {
        console.log('error', result);
      });
    };

    $scope.setUserProfile = function($event) {
      if ($event) {
        $event.preventDefault();
      }
      userService.useLocalStorage($scope.localStorage);
      userService.saveProfile($scope.profile);
    };

    $scope.saveUser = function($event) {
      if ($event) {
        $event.preventDefault();
      }

      console.log($scope.user.imgData);
      var data;
      if ($scope.user.imgData) {
        data = new FormData();
        if ($scope.user.imgData) {
          data.append('img', $scope.user.imgData);
        }

        var meta = {};
        angular.forEach(angular.element('input', $event.target), function(input) {
          input = angular.element(input);
          if (input.attr('name') && input.attr('type')) {
            if (input.attr('type') === 'text') {
              meta[input.attr('name')] = input.val();
            }
          }
        });
        data.append('user', angular.toJson(meta));
      } else {
        data = $scope.user;
      }

      userService.update(data).then(function(result) {
        console.log($event);
        console.log(result);
      });
    };

  }]);
