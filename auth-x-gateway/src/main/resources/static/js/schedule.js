/**
 * 
 */
'use strict';

var app = angular.module('vcom.schedule', [ 'ngRoute', 'vcom.util' ]);

app.constant("vcConfig", {
	"rootUrl" : window.location.protocol
			+ "//portal.versacomllc.com/quickbooks-gateway-server",
	"activeProjectURL" : "/qbase/projects?active=true",
	"dailyScheduleReport" : "/qbase/schedules/html"
});

app.controller('MainCtrl', function($rootScope,$scope, $http,Base64, vcConfig,$location, $route) {
	

	console.debug("Loading nav contrller.");
	
	$scope.tab = function(route) {
		//console.log("...."+ route);
		return $route.current && route === $route.current.controller;
	};

	$http.get('./api/user').then(function(response) {
		//console.log(response);
		if (response.data.name) {
			$rootScope.authenticated = true;
		} else {
			$rootScope.authenticated = false;
		}
	}, function() {
		$rootScope.authenticated = false;
		console.log("Authentication failed...");
	});
	
	$scope.credentials = {};

	$scope.logout = function() {
		
		console.log("post logout to uaa...xxxx");
		 
//		$http.post('logout', {}).finally(function() {
//			$rootScope.authenticated = false;
//			$location.path("/");
//		});
//		
		$http.get('logout', {}).finally(function() {
			  $rootScope.authenticated = false;
			  console.log("Logged out from API gateway...");
			  //$location.path("/");
//			  $http.post('./auth/logout', {}, {withCredentials:true}).finally(function() {
//				console.log('Logged out Auth Server');
//				$location.path("/");
//			  });
		});
	};


	
});


app.config(function($routeProvider, $httpProvider) {
	
	$httpProvider.defaults.headers.common['X-Requested-With'] = 'XMLHttpRequest';
	$httpProvider.defaults.headers.common['Accept'] = 'application/json';
});

app.controller('DatepickerDemoCtrl', function($scope, $sce, $http,vcConfig,DateUtil) {

	$scope.selectedDate = new Date();
	$scope.selectedDateAsNumber = Date.UTC(1986, 1, 22);
	console.log("Root URL: "+ vcConfig.rootUrl);


	$scope.copyReport = function(){
		console.log($scope.dd);
		return $scope.dd;
	};
	$scope.logoutFromServer = function(){
		console.log("Server logout");
		
	};
	$scope.doSomething = function(){
		console.log("Demo...ss");
		alert("Report copied to clipboard.");
	};
	$scope.projectSelected = function(){
		
		if($scope.selectedProject == null || $scope.selectedProject == undefined) {
			$scope.schedules = [];
		}
		else{
			$scope.loadDailySchedule();
		}
	};
 		
 		
});

app.directive('ngFocusAsync', ['$parse', function($parse) {
	  return {
		    compile: function($element, attr) {
		      var fn = $parse(attr.ngFocusAsync);
		      return function(scope, element) {
		        element.on('blur', function(event) {
		          scope.$evalAsync(function() {
		            fn(scope, {$event:event});
		          });
		        });
		      };
		    }
		  };
		}]);