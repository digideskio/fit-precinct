'use strict';

/**
 * @ngdoc service
 * @name nodeApp.userService
 * @description
 * # userService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .factory('userService', ['$http', function userService($http) {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var baseUri = 'http://localhost:8080/web/api/user';

    return {
      'me': function() {
        $http.get(baseUri + '/me').then(function(result) {
          console.log(result);
        }, function(errorResult){
        	console.log("error", errorResult);
        });
      }
    };
  }]);
