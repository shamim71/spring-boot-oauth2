(function () {

   var module =  angular.module('vcom.directive', ['vcom.util']);
   
   module.constant("vcConfig", {
	   "rootUrl":window.location.protocol+"//portal.versacomllc.com/quickbooks-gateway-server",
       "activeProjectURL": "/qbase/projects?active=true"
   });
   
    var abcController = function ($scope, $http,Base64, vcConfig) {
        
    	$scope.loadData = function(path, fnc) {
    		
    		var auth = 'Basic ' + Base64.encode("API_USER" + ':' + "dPGaj1mzVSZKuQaCT7Q2m3twSFRIMl");
    		console.log(auth);
            $http({method: 'GET', url: path,headers: {'Authorization': auth}}).
            success(function(data, status, headers, config) {
            	fnc(data);
            }).
            error(function(data, status, headers, config) {
            	console.error("Error response"+ data);
            });
    	};
    	
        $scope.loadProjects = function() {
        	console.log("Root URL: "+ vcConfig.rootUrl);
        	
        	var path = vcConfig.rootUrl + vcConfig.activeProjectURL;
        	
        	$scope.projects = [{rid: 123, name:"TowerCo Site Aq."}, {rid: 321, name:"L1900"}];
        	
        	$scope.loadData(path, function(data){
        		//console.log("Fetched Data: "+ data);
        		$scope.projects= data;
        	});
        	
        };
        $scope.onProjectSelected = function() {
        	console.log("Bla Bla"+ $scope.selectedProject.name);
        	 //$scope.sProject = $scope.selectedProject;
        };
        
/*        $scope.$watch('sProject', function(v) {
            console.log("Selected Project: "+v.name);
        }); */
        
        
    };

    module.controller('abcController', ['$scope','$http','Base64','vcConfig', abcController]);

    var vcProjectList = function () {
        return {
            restrict: 'E', //E = element, A = attribute, C = class, M = comment    
            transclude: true,
            scope: {    
            	selectedProject: '=vcProject'
            },
            template: '<select ng-model="selectedProject" ng-options="p.name for p in projects" ng-change="onProjectSelected()"><option value="">-- Choose a project --</option></select>',
            controller: abcController,
            link: function ($scope, element, attrs) {
            	
            	$scope.loadProjects();
            	
            }
        };
    };
    
    console.log("TEST...");

    module.directive('vcProjectList', vcProjectList);
}());