'use strict';

describe('Filter: momentFilter', function () {

  // load the filter's module
  beforeEach(module('nodeApp'));

  // initialize a new instance of the filter before each test
  var momentFilter;
  beforeEach(inject(function ($filter) {
    momentFilter = $filter('momentFilter');
  }));

  it('should return the input prefixed with "momentFilter filter:"', function () {
    var text = 'angularjs';
    expect(momentFilter(text)).toBe('momentFilter filter: ' + text);
  });

});
