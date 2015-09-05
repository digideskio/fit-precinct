'use strict';

describe('Service: workoutService', function () {

  // load the service's module
  beforeEach(module('nodeApp'));

  // instantiate service
  var workoutService;
  beforeEach(inject(function (_workoutService_) {
    workoutService = _workoutService_;
  }));

  it('should do something', function () {
    expect(!!workoutService).toBe(true);
  });

});
