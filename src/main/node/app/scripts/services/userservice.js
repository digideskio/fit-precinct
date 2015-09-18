'use strict';

/**
 * @ngdoc service
 * @name nodeApp.userService
 * @description
 * # userService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .factory('userService', ['$http', '$q', '$window', '$interval', 'storageService', 'apiLocation', function userService($http, $q, $window, $interval, storageService, apiLocation) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var sessionId = null;

    var baseUri = apiLocation + '/web/api/user';

    var loginDeferred = null;
    var loginIntervalPromise = null;
    var useLocalStorage = false;

    var receiveMessage = function(event) {
      // check event origin
      var originUrl = new URL(event.origin);
      if (originUrl.protocol !== window.location.protocol || originUrl.hostname !== window.location.hostname) {
        console.log('origin mismatch', event);
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
        $http.get(baseUri + '/me').then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      },
      'update': function(data) {
        var deferred = $q.defer();
        console.log(data, data.append, typeof data.append);

        if (data && data.append && typeof data.append === 'function') {
          // Update via FormData
          $http.get(baseUri + '/getUpdateUrl').then(function(result) {
            $http.post(result.data.updateUrl, data, {
              headers: {
                'Content-Type': undefined
              },
              // transformRequest: function(data) { return data; }
            }).then(function(result) {
              console.log(result);
              deferred.resolve(result);
            }, function(error) {
              console.log(error);
              deferred.reject(error);
            });
          }, function(error) {
            deferred.reject(error);
          });
        } else {
          // Update via json data
          $http.post(baseUri + '/me', data).then(function(result) {
            deferred.resolve(result);
          }, function(error) {
            deferred.reject(error);
          });


        }

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
