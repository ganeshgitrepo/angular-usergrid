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
		.when(rootPath + "/register", {
			controller:RegistrationController, templateUrl:"register.html"})
		.otherwise({"redirectTo":rootPath});
});

function LoginController($scope, $location) {
	$scope.username = "";
	$scope.password = "";
	$scope.password_verify = "";
	$scope.alerts = [];
	$scope.login = function(uname, pword) {
		client.login(uname, pword, function(err, data) {
			if (err) {
				$scope.alerts.push({type: "error", msg: "Login failed, please try again..."});
			} else {
				$scope.alerts.push({type: "success", msg: "Login success. Directing you to page #1"});
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
	$scope.email = "";
	$scope.password = "";
	$scope.password_verify = "";
	$scope.alerts = [];
	$scope.register = function(username, email, password) {
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
	$scope.validate = function() {
		return false;
	};
}

// Based on code from http://plnkr.co/edit/DIq3i6YHnPc9EJzd21b3?p=preview 
appModule.directive('match', function($parse) {
	return {
		require: 'ngModel',
		link: function(scope, elem, attrs, ctrl) {
			scope.$watch(function() {
				return $parse(attrs.match)(scope) === ctrl.$modelValue;
			}, function(currentValue) {
				ctrl.$setValidity('mismatch', currentValue);
			});
		}
	};
});

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