'use strict';
var foo;
/**
 * @ngdoc directive
 * @name nodeApp.directive:imgUpload
 * @description
 * # imgUpload
 */
angular.module('nodeApp')
  .directive('imgUpload', function() {
    return {
      template: '<input type="file"/><span ng-transclude></span>',
      restrict: 'A',
      transclude: true,
      scope: {
        imgSrc: '=',
        imgInputName: '@',
        imgData: '='
      },
      link: function postLink(scope, element /*, attrs */ ) {
        // file selection
        function fileSelectHandler(e) {
          // cancel event and hover styling
          fileDragHover(e);

          var originalEvent = e.originalEvent;

          // fetch FileList object
          var files = originalEvent.target.files || originalEvent.dataTransfer.files;

          // process all File objects
          angular.forEach(files, function(file) {
            scope.imgData = file;

            if (file.type.indexOf('image') === 0) {
              scope.imgSrc = e.target.result;
              var reader = new FileReader();
              reader.onload = function(e) {
                var preview = angular.element('img', element);
                preview.attr('src', e.target.result);
              };
              reader.readAsDataURL(file);
            }
          });
        }

        // file drag hover
        function fileDragHover(e) {
          e.stopPropagation();
          e.preventDefault();
          foo = e.target;
          if (e.type === 'dragover') {
            e.target.classList.add('hover');
          } else {
            e.target.classList.remove('hover');
          }
        }


        // element.text('this is the upload directive');
        var fileSelect = angular.element('input', element);
        fileSelect.attr('name', scope.imgInputName);
        var fileDrag = angular.element('span', element);
        fileSelect.on('change', fileSelectHandler);

        var xhr = new XMLHttpRequest();
        if (xhr.upload) {
          fileSelect.css('display', 'none');
          fileDrag.on('dragover', fileDragHover);
          fileDrag.on('dragleave', fileDragHover);
          fileDrag.on('drop', fileSelectHandler);
        }
      }
    };
  });
