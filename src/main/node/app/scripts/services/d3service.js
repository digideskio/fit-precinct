'use strict';
/* global d3 */

/**
 * @ngdoc service
 * @name nodeApp.d3service
 * @description
 * # d3service
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('d3service', function () {
    // AngularJS will instantiate a singleton by calling "new" on this function
    return d3;
  });
