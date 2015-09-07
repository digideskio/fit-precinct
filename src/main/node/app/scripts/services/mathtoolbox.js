'use strict';

/**
 * @ngdoc service
 * @name nodeApp.mathToolbox
 * @description
 * # mathToolbox
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('mathToolbox', function mathToolbox() {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var math = {};

    math.derive = function derive(datasets) {
      var output = [];
      var prevX = 0;
      var prevY = 0;
      angular.forEach(datasets, function(data) {
        var diffX = data[0] - prevX;
        var diffY = data[1] - prevY;

        var value = diffY / diffX;
        // console.log(diffX, diffY, value);
        if (!isNaN(value)) {
          output.push([data[0], value]);
        }

        prevX = data[0];
        prevY = data[1];
      });

      return output;
    };

    /**
     * https://github.com/sveinn-steinarsson/flot-downsample/
     * @param  {[type]} data      [description]
     * @param  {[type]} threshold [description]
     * @return {[type]}           [description]
     */
    math.largestTriangleThreeBuckets = function (data, threshold) {
      var floor = Math.floor,
        abs = Math.abs;

      var dataLength = data.length;
      if (threshold >= dataLength || threshold === 0) {
        return data; // Nothing to do
      }

      var sampled = [],
        sampledIndex = 0;

      // Bucket size. Leave room for start and end data points
      var every = (dataLength - 2) / (threshold - 2);

      var a = 0, // Initially a is the first point in the triangle
        maxAreaPoint,
        maxArea,
        area,
        nextA;

      sampled[sampledIndex++] = data[a]; // Always add the first point

      for (var i = 0; i < threshold - 2; i++) {

        // Calculate point average for next bucket (containing c)
        var avgX = 0,
          avgY = 0,
          avgRangeStart = floor((i + 1) * every) + 1,
          avgRangeEnd = floor((i + 2) * every) + 1;
        avgRangeEnd = avgRangeEnd < dataLength ? avgRangeEnd : dataLength;

        var avgRangeLength = avgRangeEnd - avgRangeStart;

        for (; avgRangeStart < avgRangeEnd; avgRangeStart++) {
          avgX += data[avgRangeStart][0] * 1; // * 1 enforces Number (value may be Date)
          avgY += data[avgRangeStart][1] * 1;
        }
        avgX /= avgRangeLength;
        avgY /= avgRangeLength;

        // Get the range for this bucket
        var rangeOffs = floor((i + 0) * every) + 1,
          rangeTo = floor((i + 1) * every) + 1;

        // Point a
        var pointAX = data[a][0] * 1, // Enforce Number (value may be Date)
          pointAY = data[a][1] * 1;

        maxArea = area = -1;

        for (; rangeOffs < rangeTo; rangeOffs++) {
          // Calculate triangle area over three buckets
          area = abs((pointAX - avgX) * (data[rangeOffs][1] - pointAY) -
            (pointAX - data[rangeOffs][0]) * (avgY - pointAY)
          ) * 0.5;
          if (area > maxArea) {
            maxArea = area;
            maxAreaPoint = data[rangeOffs];
            nextA = rangeOffs; // Next a is this b
          }
        }

        sampled[sampledIndex++] = maxAreaPoint; // Pick this point from the bucket
        a = nextA; // This a is the next a (chosen b)
      }

      sampled[sampledIndex++] = data[dataLength - 1]; // Always add last

      return sampled;
    };

    return math;
  });
