'use strict';

/* global Storage, localStorage */

/**
 * @ngdoc service
 * @name nodeApp.storageService
 * @description
 * # storageService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('storageService', function storageService() {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var storageAvailable = false;
    if (typeof(Storage) !== 'undefined') {
      if (typeof(localStorage) !== 'undefined') {
        storageAvailable = true;
      }
    }

    return {
      'available': function() {
        return storageAvailable;
      },
      'put': function(key, value) {
        if (!storageAvailable) {
          return false;
        }
        localStorage.setItem(key, JSON.stringify(value));
        return true;
      },
      'get': function(key) {
        if (!storageAvailable) {
          return false;
        }
        return JSON.parse(localStorage.getItem(key));
      }
    };
  });
