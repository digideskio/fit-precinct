'use strict';

describe('Service: openlayersService', function () {

  // load the service's module
  beforeEach(module('nodeApp'));

  // instantiate service
  var openlayersService;
  beforeEach(inject(function (_openlayersService_) {
    openlayersService = _openlayersService_;
  }));

  it('should do something', function () {
    expect(!!openlayersService).toBe(true);
  });

});
