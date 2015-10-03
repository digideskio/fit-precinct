'use strict';

describe('Directive: d3chart', function () {

  // load the directive's module
  beforeEach(module('nodeApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<d3chart></d3chart>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the d3chart directive');
  }));
});
