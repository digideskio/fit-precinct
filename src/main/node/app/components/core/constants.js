/* global d3:false, moment:false */
(function() {
  'use strict';

  var apiLocation;
  if (window.location.hostname === 'localhost') {
    apiLocation = 'http://localhost:8080';
  } else {
    apiLocation = 'https://' + window.location.hostname;
  }

  angular
    .module('app.core')
    .constant('d3', d3)
    .constant('apiLocation', apiLocation)
    .constant('moment', moment);
})();
