var app = angular.module('heating', ["ngRoute", "toggle-switch"]);
app.config(function($routeProvider) {
	  $routeProvider
	  .when("/", {
		templateUrl : "home.html"
	  })
	  .when("/temperatursensor", {
	    templateUrl : "temperatursensor.html",
	    controller : "temperaturController"
	  })
	  .when("/relais", {
	    templateUrl : "relais.html",
	    controller: "relaisController"
	  })
	});