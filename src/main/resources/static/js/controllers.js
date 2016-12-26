app.controller("temperaturController", function ($scope, $http, $interval) {
	var headers = {
			'Authorization' : 'Basic' + window.btoa("user:user"), 
			'Accept' : "application/json"
	}
	
	$http.get("/temperatursensor.json", headers).then(function (response) {
		var data = response.data;
		$scope.sensores = data;
	});   
	
	$interval(function() {
		$http.get("/temperatursensor.json", headers).then(function (response) {
			var data = response.data;
			$scope.sensores = data;
		});        
    }, 60000);
	
    $scope.title = "Temperatur";  
});

app.controller("relaisController", function ($scope, $http, $timeout, $log) {
	var headers = {
			'Authorization' : 'Basic' + window.btoa("user:user"), 
			'Accept' : "application/json"
	}
	
	$http.get("/relais.json", headers).then(function (response) {
		var data = response.data;
		
		angular.forEach(data, function(value, key) {
			if(value.status == 'ON') {
				value.status = true;
			} else {
				value.status = false;
			}
		});
		
		$scope.relaises = data;
	});
	
    $scope.title = "Relais";
    
    $scope.toggle = function (relais, index) {
    	$log.info(relais.relaisId + ": " + relais.status)
    	if(relais.status) {
    		$http.post("/relais/" + relais.relaisId + "/off.json", headers)
    		.then(function (response) {});
    		relais.status = !relais.status;
    	} else {
    		$http.post("/relais/" + relais.relaisId + "/on.json", headers)
    		.then(function (response) {});
    		relais.status = !relais.status;
    	}
    	
    	$scope.relaises[index] = relais;
    }
});