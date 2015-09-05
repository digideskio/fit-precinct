'use strict';

/**
 * @ngdoc service
 * @name nodeApp.workoutService
 * @description
 * # workoutService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('workoutService', ['$http', '$q', function workoutService($http, $q) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var baseUri = 'http://localhost:8080/web/api/workout';

    return {
      'list': function() {
        var deferred = $q.defer();
        $http.get(baseUri + '/list', {
          'withCredentials': true
        }).then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;
      },
      'get': function(id){
        var deferred = $q.defer();
        $http.get(baseUri + '/get/' + id, {
          'withCredentials': true
        }).then(function(result) {
          deferred.resolve(result);
        }, function(error) {
          deferred.reject(error);
        });

        return deferred.promise;

      }
    };
  }]);
