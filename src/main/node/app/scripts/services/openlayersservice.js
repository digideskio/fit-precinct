'use strict';
/* global ol */
/**
 * @ngdoc service
 * @name nodeApp.openlayersService
 * @description
 * # openlayersService
 * Service in the nodeApp.
 */
angular.module('nodeApp')
  .service('openlayersService', function openlayersService() {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var openlayers = {};
    
    openlayers.ol = ol;
    openlayers.source = {};
    openlayers.source.mapQuest = [new ol.layer.Tile({
      source: new ol.source.MapQuest({
        layer: 'osm'
      })
    })];

    openlayers.source.osm = [new ol.layer.Tile({
      source: new ol.source.OSM()
    })];

    openlayers.source.stamen = [new ol.layer.Tile({
        source: new ol.source.Stamen({
          layer: 'watercolor'
        })
      }),
      new ol.layer.Tile({
        source: new ol.source.Stamen({
          layer: 'terrain-labels'
        })
      })
    ];


    return openlayers;
  });
