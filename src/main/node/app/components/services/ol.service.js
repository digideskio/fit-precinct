'use strict';
/* global ol */
/**
 * @ngdoc service
 * @name nodeApp.openlayersService
 * @description
 * # openlayersService
 * Service in the nodeApp.
 */
angular.module('app')
  .service('openlayersService', function openlayersService() {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var openlayers = {};

    openlayers.ol = ol;
    openlayers.source = {};
    // openlayers.source.BingMaps = [new ol.layer.Tile({
    //   source: new ol.source.BingMaps({
    // key: 'Your Bing Maps Key from http://www.bingmapsportal.com/ here',
    // imagerySet: styles[i]
    // use maxZoom 19 to see stretched tiles instead of the BingMaps
    // "no photos at this zoom level" tiles
    // maxZoom: 19
    //   })
    // })];

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
