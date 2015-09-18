'use strict';

describe('Directive: ngModel', function () {

  // load the directive's module
  beforeEach(module('nodeApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<ng-model></ng-model>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the ngModel directive');
  }));
});
