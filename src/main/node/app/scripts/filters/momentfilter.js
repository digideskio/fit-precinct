'use strict';
/* globals moment */

/**
 * @ngdoc filter
 * @name nodeApp.filter:momentFilter
 * @function
 * @description
 * # momentFilter
 * Filter in the nodeApp.
 */
angular.module('nodeApp')
  .filter('momentFilter', function() {
    return function(input, inputFormat, output, outputFormat) {
      if (typeof(output) === 'undefined') {
        outputFormat = inputFormat;
        output = 'date';
      }

      switch (output) {
        case 'duration':
          switch (outputFormat) {
            case 'hours':
              return moment.duration(input, inputFormat).hours();
            case 'minutes':
              return moment.duration(input, inputFormat).minutes();
            case 'asHours':
              return moment.duration(input, inputFormat).asHours();
            case 'asMinutes':
              return moment.duration(input, inputFormat).asMinutes();
            default:
              return moment.duration(input, inputFormat).asMinutes();
          }
          break;
        case 'date':
          return moment(input).format(outputFormat);
        default:
          return input;
      }
    };
  });
