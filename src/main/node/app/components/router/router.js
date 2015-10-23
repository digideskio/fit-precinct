(function() {
  'use strict';

  var module = angular.module('app.router');

  module
    .provider('routerConfig', routerConfig)
    .factory('router', router);


  router.$inject = ['routerConfig']; // $stateProvider

  // Must configure via the routehelperConfigProvider
  function routerConfig() {
    /* jshint validthis:true */
    this.config = {
      // These are the properties we need to set
      // $stateProvider: undefined
      // docTitle: ''
      // resolveAlways: {ready: function(){ } }
    };

    this.$get = function() {
      return {
        config: this.config
      };
    };
  }

  function router(routerConfig) { // $stateProvider
    return routerConfig.config.$stateProvider;
  }
})();
