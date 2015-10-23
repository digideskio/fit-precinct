(function() {
  'use strict';
  var module = angular.module('app.user');

  module.controller('User', User);

  User.$inject = ['$scope', '$state', 'userService'];
  function User($scope, $state, userService) {
    $scope.useLocalStorage = false;
    $scope.localStorageAvailable = userService.localStorageAvailable();

    $scope.user = {};
    $scope.uploadSettings = {};
    $scope.status = null;
    $scope.imgData = null;
    $scope.user.imgUrl = 'http://placehold.it/100x100';

    $scope.updateHr = function() {
      console.log($scope.user.profileData.linkHr && $scope.user.profileData.age);
      if ($scope.user.profileData.linkHr && $scope.user.profileData.age) {
        $scope.user.profileData.hr100 = (220 - $scope.user.profileData.age).toFixed(1);
        $scope.user.profileData.hr90 = ($scope.user.profileData.hr100 * 0.9).toFixed(1);
        $scope.user.profileData.hr80 = ($scope.user.profileData.hr100 * 0.8).toFixed(1);
        $scope.user.profileData.hr70 = ($scope.user.profileData.hr100 * 0.7).toFixed(1);
        $scope.user.profileData.hr60 = ($scope.user.profileData.hr100 * 0.6).toFixed(1);
        $scope.user.profileData.hr50 = ($scope.user.profileData.hr100 * 0.5).toFixed(1);
      }
    };

    function me() {
      userService.me().then(function(result) {
        $scope.user = result.data;
        $scope.useLocalStorage = userService.useLocalStorage();
      }, function(error) {
        console.log('error', error);
        if (error.status == 401) {
          $state.go('login');
        }
      });
    }

    me();

    $scope.setUploadSettings = function($event) {
      $event.preventDefault();
      userService.setUploadUser($scope.uploadSettings.username, $scope.uploadSettings.password).then(function(result) {
        console.log(result);
        $scope.status = 'OK';
      }, function(error) {
        console.log('error', error);
        if (error.status == 401) {
          $state.go('login');
        }
      });
    };

    $scope.saveUser = function($event) {
      if ($event) {
        $event.preventDefault();
      }
      userService.useLocalStorage($scope.useLocalStorage);

      var data;
      if ($scope.imgData) {
        data = new FormData();
        data.append('img', $scope.imgData);
      }

      userService.update($scope.user, data).then(function(result) {
        $scope.user = result.data;
        console.log($event);
        console.log(result);
      });
    };

  }
})();
