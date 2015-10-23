(function() {
  'use strict';
  var material = angular.module('app.material');

  material.directive('mdCheckbox', directive);

  function directive() {
    return {
      restrict: 'A',
      template: '<div class="checkbox" ng-transclude></div>',
      replace: true,
      transclude: true,
      link: function postLink(scope, element, attrs) {
        element.find('input[type=checkbox]').after('<span class="checkbox-material"><span class="check"></span></span>');

        element.on('change', '.checkbox input[type=checkbox]', function() {
          console.log(this);
          angular.element(this).blur();
        });

      }
    }
  }
})();
