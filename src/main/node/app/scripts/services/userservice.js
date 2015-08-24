'use strict';

/**
 * @ngdoc service
 * @name nodeApp.userService
 * @description
 * # userService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .factory('userService', ['$http', '$q', '$window', '$interval', function userService($http, $q, $window, $interval) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var sessionId = null;
    var baseUri = 'http://localhost:8080/web/api/user';

    var loginDeferred = null;
    var loginIntervalPromise = null;

    var receiveMessage = function(event) {
      // check event origin
      var originUrl = new URL(event.origin);
      console.log(originUrl, window.location);
      if (originUrl.protocol !== window.location.protocol || originUrl.hostname !== window.location.hostname) {
        return;
      }

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
        $http.get(baseUri + '/me', {'headers': {'Session': sessionId}}).then(function(result) {
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
      'setUploadPassword': function(password) {
        var deferred = $q.defer();
        var request = {
          'password': password
        };
        $http.put(baseUri + '/uploadpw', request).then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      }
    };
  }]);
