(function() {
  'use strict';
  var module = angular.module('app.workouts');
  module.service('workoutService', WorkoutService);
  WorkoutService.$inject = ['$http', '$q', 'apiLocation'];

  function WorkoutService($http, $q, apiLocation) {
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
    var head = function(id) {
      var deferred = $q.defer();
      $http.get(baseUri + '/get/' + id + '/head', {
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
    var deleteWorkout = function(id) {
      var deferred = $q.defer();
      $http.delete(baseUri + '/delete/' + id, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var createHead = function(workoutHead) {
      var deferred = $q.defer();
      $http.post(baseUri + '/createHead', workoutHead, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var appendData = function(id, workoutData) {
      var deferred = $q.defer();
      $http.post(baseUri + '/append/' + id, workoutData, {
        'withCredentials': true
      }).then(function(result) {
        deferred.resolve(result);
      }, function(error) {
        deferred.reject(error);
      });

      return deferred.promise;
    };
    var concatWorkouts = function(workoutIds, newWorkout) {
      newWorkout = newWorkout || {};
      newWorkout.data = {};

      var deferred = $q.defer();
      // Retrieve workout heads
      var headsDeferred = [];
      angular.forEach(workoutIds, function(workoutId) {
        headsDeferred.push(head(workoutId));
      });

      $q.all(headsDeferred).then(function(result) {
        // order heads by start time
        result.sort(function(a, b) {
          return a.data.startTime - b.data.startTime;
        });

        // abort if time overlaps
        var t = 0;
        var tMin = 0;
        angular.forEach(result, function(workoutHeader, i) {
          var header = workoutHeader.data;
          console.log(header, i);
          var duration = header.data.clockDuration || header.data.duration;
          var endTime = header.startTime + duration;
          console.log(duration, endTime);
          if (endTime > t) {
            t = endTime;
            if (i < 1) {
              tMin = header.startTime;
              for (var firstKeys in header.data) {
                newWorkout.data[firstKeys] = header.data[firstKeys];
              }
            } else {
              for (var key in header.data) {
                if (key.indexOf('Max') > 0) {
                  newWorkout.data[key] = header.data[key] > newWorkout.data[key] ? header.data[key] : newWorkout.data[key];
                } else {
                  newWorkout.data[key] += header.data[key];
                }
              }
            }
          } else {
            deferred.reject('cannot concatenate overlapping workouts.');
            return deferred.promise; // ??
          }
        });

        // Correct average values
        for (var key in newWorkout.data) {
          if (newWorkout.data[key] && key.indexOf('Avg') > 0) {
            console.log('Correcting ' + key);
            newWorkout.data[key] = newWorkout.data[key] / result.length;
          }
        }

        // Create new workout head
        createHead(newWorkout).then(function(createResult) {
          newWorkout = createResult.data;

          // retrieve data data
          var sMax = 0;
          angular.forEach(result, function(workoutHeader) {
            load(workoutHeader.data.id).then(function(workout) {
              // reenumerate data. Make sure to check the offset of start time
              var offset = Math.floor((workout.head.startTime - tMin) / 1000);
              for (var key in workout.data) {
                if (key === 'distance') {
                  for (var i in workout.data[key]) {
                    workout.data[key][i].offset = workout.data[key][i].offset + offset;
                    // console.log(workout.data[key][i].data[0]);
                    workout.data[key][i].data[0] = sMax + parseInt(workout.data[key][i].data[0]);
                  }
                } else {
                  for (var j in workout.data[key]) {
                    workout.data[key][j].offset = workout.data[key][j].offset + offset;
                  }
                }
              }

              sMax = workout.data.distance[workout.data.distance.length - 1].data[0];

              // add data
              var appendTasks = [];
              for (key in workout.data) {
                appendTasks.push(appendData(newWorkout.id, {
                  type: key,
                  dataSet: workout.data[key]
                }));
              }

              $q.all(appendTasks).then(function(result) {
                console.log(result);
                deferred.resolve(true);
              });
            });
          });
        });
      });

      return deferred.promise;
    };

    return {
      'list': list,
      'listLast': listLast,
      'get': get,
      'update': update,
      'load': load,
      'delete': deleteWorkout,
      'concat': concatWorkouts
    };
  }

})();
