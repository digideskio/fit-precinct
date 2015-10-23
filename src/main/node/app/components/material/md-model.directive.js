(function() {
  'use strict';
  var material = angular.module('app.material');

  material.directive('mdModel', directive);

  function directive() {
    return {
      require: 'ngModel',
      scope: {
        'ngModel': '&'
      },
      link: function($scope, $element, $attributes, ngModelController) {
        $scope.$watch($scope.ngModel, function(newVal) {
          if (ngModelController.$isEmpty(newVal)) {
            $element.addClass('empty');
          } else {
            $element.removeClass('empty');
          }
        });
      }
    };
  }
})();
