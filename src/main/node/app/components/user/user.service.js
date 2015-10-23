(function() {
  'use strict';
  var module = angular.module('app.user');

  module.service('userService', service);

  service.$inject = ['$http', '$q', '$window', '$interval', 'storageService', 'apiLocation'];
  function service($http, $q, $window, $interval, storageService, apiLocation) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var opt = {
      useLocalStorage: false
    };
    var sessionId = null;

    var baseUri = apiLocation + '/web/api/user';

    var loginDeferred = null;
    var loginIntervalPromise = null;

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

    function getProfile() {
      if (localStorageAvailable()) {
        return storageService.get('profile');
      }
    }

    function saveProfile(profile) {
      if (localStorageAvailable()) {
        storageService.put('profile', profile);
      }
    }

    function localStorageAvailable() {
      return storageService.available();
    }

    function separateProfileFromUser(user) {
      if (useLocalStorage()) {
        // save profile to localStorage and remove user profile before server request
        saveProfile(user.profileData);
        delete user.profileData;
      }

      // If local storage is not used profile data will be sent to the server
      return user;
    }

    function useLocalStorage(value) {
      if (typeof value === 'undefined') {
        return opt.useLocalStorage;
      }
      console.log('localStorage switched ' + (value ? 'on' : 'off'));
      opt.useLocalStorage = value;
    }

    return {
      'me': function() {
        var deferred = $q.defer();
        $http.get(baseUri + '/me').then(function(result) {
          if (result.data.profileData) {
            // profile data available on server side
            useLocalStorage(false);
          } else if (localStorageAvailable()) {
            result.data.profileData = getProfile() || {};
            useLocalStorage(true);
          }
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      },
      'update': function(user, data) {
        var deferred = $q.defer();
        user = separateProfileFromUser(user);

        if (data && data.append && typeof data.append === 'function') {
          // Update via FormData
          data.append('user', angular.toJson(user));
          $http.get(baseUri + '/getUpdateUrl').then(function(result) {
            $http.post(result.data.updateUrl, data, {
              headers: {
                'Content-Type': undefined
              },
              // transformRequest: function(data) { return data; }
            }).then(function(result) {
              result.profileData = result.profileData || getProfile();
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
          $http.post(baseUri + '/me', user).then(function(result) {
            result.data.profileData = result.data.profileData || getProfile();
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
      'localStorageAvailable': localStorageAvailable,
      'useLocalStorage': useLocalStorage
    };
  }
})();
