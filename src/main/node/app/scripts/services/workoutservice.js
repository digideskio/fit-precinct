'use strict';

/**
 * @ngdoc service
 * @name nodeApp.workoutService
 * @description
 * # workoutService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('workoutService', ['$http', '$q', 'apiLocation', function workoutService($http, $q, apiLocation) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var baseUri = apiLocation + '/web/api/workout';

    var list = function(since, until) {
      var deferred = $q.defer();
      var path = baseUri + '/list';
      if (since) {
        path += '/since/' + since;
        if (until) {
          path += '/until/' + until;
        }
      }

      $http.get(path, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var listLast = function(n) {
      var deferred = $q.defer();
      var path = baseUri + '/list/' + n;

      $http.get(path, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var get = function(id) {
      var deferred = $q.defer();
      $http.get(baseUri + '/get/' + id, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var update = function(workout) {
      console.log('update', workout);
      var deferred = $q.defer();
      $http.post(baseUri + '/update/' + workout.id, workout, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var load = function(id) {
      var deferred = $q.defer();

      get(id).then(function(result) {
        var head, workoutData = {};
        angular.forEach(result.data, function(data) {
          if (data.dataSet) {
            if (typeof(workoutData[data.type]) === 'undefined') {
              workoutData[data.type] = data.dataSet;
            } else {
              workoutData[data.type].push.apply(workoutData[data.type], data.dataSet);
            }
          } else {
            head = data;
            // angular.extend($scope.workout, data);
          }
        });

        deferred.resolve({
          head: head,
          data: workoutData
        });
      });

      return deferred.promise;
    };

    return {
      'list': list,
      'listLast': listLast,
      'get': get,
      'update': update,
      'load': load
    };
  }]);
