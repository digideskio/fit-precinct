'use strict';

/**
 * @ngdoc directive
 * @name nodeApp.directive:ngModel
 * @description
 * # ngModel
 */
angular.module('nodeApp')
  .directive('ngModelExt', [function() {
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
  }]);
