'use strict';

angular.module('vcom.directive', [])

  .directive('vcProjectList', ['$http', function ($http) {
    return {
      restrict: 'E',
      replace: true,
      
      controller: function($scope) {
        
          $scope.loadProjects = function() {
        	  $scope.projects = [{rid: 123, name:"TowerCo Site Aq."}, {rid: 321, name:"L1900"}];
          };
      },      
      template: '<div><select  ng-model="selectedProject" ng-options="p.name for p in projects"><option value="">-- Choose a project --</option></select></div>',
      scope: {
      },
      link: function($scope, elm, attrs, ctrl) {          
    	  $scope.loadProjects();
      }
    };
  }]);