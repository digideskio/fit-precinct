'use strict';

describe('Service: mathToolbox', function () {

  // load the service's module
  beforeEach(module('nodeApp'));

  // instantiate service
  var mathToolbox;
  beforeEach(inject(function (_mathToolbox_) {
    mathToolbox = _mathToolbox_;
  }));

  it('should do something', function () {
    expect(!!mathToolbox).toBe(true);
  });

});
