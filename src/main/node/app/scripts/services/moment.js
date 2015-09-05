'use strict';

/**
 * @ngdoc service
 * @name nodeApp.moment
 * @description
 * # moment
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('moment', function moment() {
    // AngularJS will instantiate a singleton by calling "new" on this function
  	return moment;
  });

