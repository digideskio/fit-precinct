'use strict';

describe('Service: storageService', function () {

  // load the service's module
  beforeEach(module('nodeApp'));

  // instantiate service
  var storageService;
  beforeEach(inject(function (_storageService_) {
    storageService = _storageService_;
  }));

  it('should do something', function () {
    expect(!!storageService).toBe(true);
  });

});
