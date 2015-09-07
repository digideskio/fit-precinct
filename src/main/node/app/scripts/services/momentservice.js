'use strict';

/* global moment */
/**
 * @ngdoc service
 * @name nodeApp.moment
 * @description
 * # moment
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('momentService', function momentService() {
    // AngularJS will instantiate a singleton by calling "new" on this function
  	return moment;
  });

