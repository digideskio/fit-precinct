'use strict';

/**
 * @ngdoc service
 * @name nodeApp.userService
 * @description
 * # userService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .factory('userService', ['$http', '$q', '$window', '$interval', 'storageService', function userService($http, $q, $window, $interval, storageService) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var sessionId = null;
    var baseUri = 'http://localhost:8080/web/api/user';

    var loginDeferred = null;
    var loginIntervalPromise = null;
    var useLocalStorage = false;

    var receiveMessage = function(event) {
      // check event origin
      var originUrl = new URL(event.origin);
      if (originUrl.protocol !== window.location.protocol || originUrl.hostname !== window.location.hostname) {
        return;
      }

      console.log(originUrl, window.location);

      // read response data
      var data = angular.fromJson(event.data);
      switch (data.response) {
        case 'close':
          $interval.cancel(loginIntervalPromise);
          $window.removeEventListener('message', receiveMessage);
          break;
        case 'session':
          loginDeferred.resolve(data.msg);
          sessionId = data.msg;
          event.source.postMessage('{"type":"close"}', event.origin);
          break;
        default:
          break;
      }
    };

    return {
      'me': function() {
        var deferred = $q.defer();
        $http.get(baseUri + '/me', {
          'withCredentials': true
        }).then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      },
      'update': function(user) {
        var deferred = $q.defer();
        $http.post(baseUri + '/me', user, {
          'withCredentials': true
        }).then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      },
      'login': function(provider) {
        if (loginDeferred !== null) {
          loginDeferred.reject('New login request');
        }

        loginDeferred = $q.defer();

        $window.addEventListener('message', receiveMessage, false);

        var loginWindow = $window.open(baseUri + '/login/' + provider);

        if (loginIntervalPromise !== null) {
          $interval.cancel(loginIntervalPromise);
        }

        loginIntervalPromise = $interval(function() {
          loginWindow.postMessage('{"type":"session"}', '*');
        }, 1000);

        return loginDeferred.promise;
      },
      'setUploadUser': function(username, password) {
        var deferred = $q.defer();
        $http.put(baseUri + '/uploadUser', {
          'username': username,
          'password': password
        }, {
          'withCredentials': true
        }).then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      },
      'localStorageAvailable': function() {
        return storageService.available();
      },
      'useLocalStorage': function(value) {
        useLocalStorage = value;
      },
      'saveProfile': function(profile) {
        if (useLocalStorage) {
          storageService.put('profile', profile);
        }
      },
      'getProfile': function() {
        if (useLocalStorage) {
          return storageService.get('profile');
        }
      }
    };
  }]);
