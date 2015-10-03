'use strict';

window.debounce = window.debounce ||
  // Returns a function, that, as long as it continues to be invoked, will not
  // be triggered. The function will be called after it stops being called for
  // N milliseconds. If `immediate` is passed, trigger the function on the
  // leading edge, instead of the trailing.
  function(func, wait, immediate) {
    var timeout;
    return function() {
      var context = this,
        args = arguments;
      var later = function() {
        timeout = null;
        if (!immediate) {
          func.apply(context, args);
        }
      };
      var callNow = immediate && !timeout;
      clearTimeout(timeout);
      timeout = setTimeout(later, wait);
      if (callNow) {
        func.apply(context, args);
      }
    };
  };

/**
 * @ngdoc directive
 * @name nodeApp.directive:d3chart
 * @description
 * # d3chart
 */
angular.module('nodeApp')
  .directive('d3chart', ['d3service', function(d3) {
    return {
      restrict: 'A',
      scope: {
        leftData: '&',
        rightData: '&',
        leftOptions: '=',
        rightOptions: '='
      },
      link: function postLink(scope, element, attrs) {
        scope.$watch(scope.leftData, function(newVal) {
          if (Array.isArray(newVal)) {
            scope.leftOptions.descriptionAlign = scope.leftOptions.descriptionAlign || 'left';
            draw(newVal, scope.leftOptions, 'left');
          }
        });
        scope.$watch(scope.rightData, function(newVal) {
          if (Array.isArray(newVal)) {
            scope.rightOptions.descriptionAlign = scope.rightOptions.descriptionAlign || 'right';
            draw(newVal, scope.rightOptions, 'right');
          }
        });

        var debounceResize = window.debounce(function(e) {
          console.log('This is where i should redraw the chart.', e);
        }, 250);
        angular.element(window).on('resize', debounceResize);

        var initialHeight = attrs.height || 290;
        var margin = {
            top: 25,
            right: 35,
            bottom: 35,
            left: 35
          },
          width = element.width() - margin.left - margin.right,
          height = initialHeight - margin.top - margin.bottom;

        var vis = d3.select(element[0]).append('svg:svg')
          .attr('width', width + margin.left + margin.right)
          .attr('height', height + margin.top + margin.bottom)
          .append('g')
          .attr('transform', 'translate(' + margin.left + ',' + margin.top + ')');

        var sides = {};
        var xAxis = {};

        function createXAxis(data) {
          if (xAxis.gx) {
            xAxis.gx.remove();
          }
          data = data.sort(function(a, b) {
            return a.offset - b.offset;
          });

          xAxis.extent = d3.extent(data, function(d) {
            return d.offset;
          });
          xAxis.scale = d3.scale.linear().domain(xAxis.extent).range([0, width]);

          xAxis.x = d3.svg.axis()
            .scale(xAxis.scale)
            .ticks(6)
            .tickFormat(function(d) {
              return Math.floor(d / 60);
            })
            .orient('bottom');

          xAxis.gx = vis.append('g')
            .attr('class', 'x axis')
            .attr('transform', 'translate(0,' + height + ')')
            .call(xAxis.x);

          xAxis.gx.append('text')
            .style('text-anchor', 'middle')
            .attr('dx', '50%')
            .attr('y', 17)
            .attr('dy', '1em')
            .text('Time (minutes)');
        }

        function createYAxis(options, side) {
          if (sides[side].axis) {
            sides[side].axis.gy.remove();
          }

          sides[side].axis = {};
          sides[side].axis.y = d3.svg.axis()
            .scale(sides[side].scale)
            .ticks(5)
            .orient(side);

          sides[side].axis.gy = vis.append('g')
            .attr('class', 'y axis')
            .call(sides[side].axis.y);

          var group = sides[side].axis.gy.append('g')
            .attr('transform', 'translate(0,-10)');

          if (options.description) {
            var text = group.append('text')
              .text(options.description);

            // Add color blob to determine correct color
            var circle = group.append('circle')
              .attr('cy', -5)
              .attr('r', 5)
              .attr('class', 'circle ' + side);


            if (options.descriptionAlign === 'right') {
              sides[side].axis.gy
                .attr('transform', 'translate(' + width * (side === 'right') + ',0)'); // translate right scales to the right
              text.style('text-anchor', 'end')
                .attr('transform', 'translate(' + margin.right + ',0)');

              // Add color blob to determine correct color
              circle
                .attr('cx', -Math.ceil(text.node().getBBox().width) + margin.right - 8);

            } else {
              text.style('text-anchor', 'start')
                .attr('transform', 'translate(' + -margin.left + ',0)');

              // Add color blob to determine correct color
              circle
                .attr('cx', Math.ceil(text.node().getBBox().width) - margin.left + 8);
            }
          }
        }

        function draw(data, options, side) {
          options = options || {};
          options.multiplier = options.multiplier || 1;
          if (!sides[side]) {
            sides[side] = {};
          }

          sides[side].extent = d3.extent(data, function(d) {
            return parseFloat(d.data[0]);
          });
          sides[side].extent[1] *= options.multiplier;
          sides[side].scale = d3.scale.linear().domain(sides[side].extent).range([height, 0]);

          createXAxis(data);
          createYAxis(options, side);

          var line = d3.svg.line()
            .x(function(d/*, i*/) {
              return xAxis.scale(d.offset);
            })
            .y(function(d/*, i*/) {
              return sides[side].scale(d.data[0] * options.multiplier);
            });

          var area = d3.svg.area()
            .x(function(d/*, i*/) {
              return xAxis.scale(d.offset);
            })
            .y0(height)
            .y1(function(d/*, i*/) {
              return sides[side].scale(d.data[0] * options.multiplier);
            });

          if (options.interpolate) {
            if (typeof options.interpolate === 'number') {
              line.interpolate(movingAvg(options.interpolate));
              area.interpolate(movingAvg(options.interpolate));
            } else {
              line.interpolate(options.interpolate);
              area.interpolate(options.interpolate);
            }
          }

          vis.append('path')
            .datum(data)
            .attr('class', 'area ' + side)
            .attr('d', area);

          vis.selectAll('path.line')
            .data([data])
            .enter().append('svg:path')
            .attr('d', line).attr('class', 'path ' + side);
        }

        function movingAvg(n) {
          return function(points) {
            points = points.map(function(each, index, array) {
              var to = index + n - 1;
              var subSeq, sum;
              if (to < points.length) {
                subSeq = array.slice(index, to + 1);
                sum = subSeq.reduce(function(a, b) {
                  return [a[0] + b[0], a[1] + b[1]];
                });
                return sum.map(function(each) {
                  return each / n;
                });
              }
              return undefined;
            });
            points = points.filter(function(each) {
              return typeof each !== 'undefined';
            });
            return points.join('L');
          };
        }
      }
    };
  }]);
