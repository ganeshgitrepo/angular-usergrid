
var rootPath = "/angular-usergrid";

client = new Usergrid.Client({
	orgName: "snoopdave",
	appName: "volda",
	logging: true,
	buildCurl: true,
	URI: "http://localhost:8080/" + rootPath + "/api"
});

var appModule = angular.module("appModule", ["ui.bootstrap"]);
appModule.config(function($routeProvider, $locationProvider) {
	$locationProvider.html5Mode(true);
	$routeProvider
		.when(rootPath + "/", {
			controller:Page1Controller, templateUrl:"page1.html"})
		.when(rootPath + "/page1", {
			controller:Page1Controller, templateUrl:"page1.html"})
		.when(rootPath + "/page2", { 
			controller:Page2Controller, templateUrl:"page2.html"})
		.when(rootPath + "/login", {
			controller:LoginController, templateUrl:"login.html"})
		.when(rootPath + "/regsiter", {
			controller:LoginController, templateUrl:"register.html"})
		.otherwise({"redirectTo":rootPath});
});

function LoginController($scope, $location) {
	$scope.username = "";
	$scope.password = "";
	$scope.response = "";
	$scope.alerts = [];
	$scope.login = function(uname, pword) {
		client.login(uname, pword, function(err, data) {
			if (err) {
				$scope.alerts.push({type:"error", msg:"Login failed, please try again..."});
			} else {
				$scope.alerts.push({type:"success", msg:"Login success. Directing you to page #1"});
				$location.path(rootPath + "/page1");
			}
			$scope.$apply();
		});
	};
	$scope.closeAlert = function(index) {
    	$scope.alerts.splice(index, 1);
	};
}

function RegistrationController($scope, $location) {
	$scope.username = "";
	$scope.password = "";
	$scope.response = "";
	$scope.alerts = [];
	$scope.register = function(uname, pword) {
		client.login(uname, pword, function(err, data) {
			if (err) {
				$scope.alerts.push({type:"error", msg:"Login failed, please try again..."});
			} else {
				$scope.alerts.push({type:"success", msg:"Login success. Directing you to page #1"});
				$location.path(rootPath + "/page1");
			}
			$scope.$apply();
		});
	};
	$scope.closeAlert = function(index) {
    	$scope.alerts.splice(index, 1);
	};
}

function Page1Controller($scope, $location) {
	if (!client.getToken()) {
		$location.path(rootPath + "/login");
	} else {
		$scope.message = "Welcome to page #1";
	}
	$scope.logout = function() { 
		client.logout(); 
		$location.path(rootPath + "/login");
	};
}

function Page2Controller($scope, $location) {
	if (!client.getToken()) {
		$location.path(rootPath + "/login");
	} else {
		$scope.message = "Welcome to page #2";
	}
	$scope.logout = function() { 
		client.logout(); 
		$location.path(rootPath + "/login");
	};
}