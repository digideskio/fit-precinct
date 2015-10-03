'use strict';

describe('Service: d3service', function () {

  // load the service's module
  beforeEach(module('nodeApp'));

  // instantiate service
  var d3service;
  beforeEach(inject(function (_d3service_) {
    d3service = _d3service_;
  }));

  it('should do something', function () {
    expect(!!d3service).toBe(true);
  });

});
