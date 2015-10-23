(function() {
  'use strict';
  /**
   * @ngdoc overview
   * @name nodeApp
   * @description
   * # nodeApp
   *
   * Main module of the application.
   */
  angular
    .module('app', [
      /*
       * Order is not important. Angular makes a
       * pass to register all of the modules listed
       * and then when app.dashboard tries to use app.data,
       * its components are available.
       */

      /*
       * Everybody has access to these.
       * We could place these under every feature area,
       * but this is easier to maintain.
       */
      'app.core',
      'app.router',
      'app.material',
      'ngAnimate',
      'ngCookies',
      'ngResource',
      'ngSanitize',
      'ngTouch',
      /*
       * Feature areas
       */
      'app.dashboard',
      'app.login',
      'app.user',
      'app.workouts'
    ]);
})();
