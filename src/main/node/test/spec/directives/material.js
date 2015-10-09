'use strict';

describe('Directive: material', function () {

  // load the directive's module
  beforeEach(module('nodeApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<material></material>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the material directive');
  }));
});
