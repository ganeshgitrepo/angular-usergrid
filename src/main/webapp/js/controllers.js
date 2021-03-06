/*
 * Copyright 2013 David M. Johnson (snoopdave@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var rootPath = config.rootPath;
client = new Usergrid.Client({
  orgName: config.orgName,
  appName: config.appName,
  URI: config.rootUri + config.rootPath + config.apiPath, 
  logging: true,
  buildCurl: true
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
    .when(rootPath + "/registered", {
      controller:RegisteredController, templateUrl:"registered.html"})
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
        $scope.alerts.push({type: "error", 
          msg: "Login failed, please try again..."});
      } else {
        $scope.alerts.push({type: "success", 
          msg: "Login success. Directing you to page #1"});
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
  $scope.register = function(username, password, email) {
    client.signup(username, password, email, null, function(err, data) {
      if (err) {
        $scope.alerts.push({type:"error", 
          msg:"Registration failed, please try again..."});
      } else {
        $scope.alerts.push({type:"success", 
          msg:"Registrsation success. Directing you to login page."});
        $location.path(rootPath + "/page1");
      }
      $scope.$apply();
    });
  };
  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };
}

function RegisteredController($scope, $location) {
}

// Based on code from http://plnkr.co/edit/DIq3i6YHnPc9EJzd21b3?p=preview 
appModule.directive('auMatch', function($parse) {
  return {
    require: 'ngModel',
    link: function(scope, elem, attrs, ctrl) {
      ctrl.$setValidity('match', false);
      scope.$watch(function() {
        return $parse(attrs.auMatch)(scope) === ctrl.$modelValue;
      }, function(currentValue) {
        ctrl.$setValidity('match', currentValue);
      });
    }
  };
});

appModule.directive('auUniqueUsername', function() {
  return {
    require: 'ngModel',
    link: function(scope, elem, attrs, ctrl) {
      ctrl.$setValidity("unique", false);
      elem.keyup(function(evt) {
        var options = { type:"user", name: ctrl.$modelValue };
        if (ctrl.$modelValue && ctrl.$modelValue.trim().length > 0) {
          client.getEntity(options, function(err, data) {
            ctrl.$setValidity("unique", err); // error means unique
            scope.$apply();
          });
        } else {
          ctrl.$setValidity("unique", false); // consider empty string non unique
          scope.$apply();
        }
      });
    }
  };
});

appModule.directive('auUniqueEmail', function() {
  return {
    require: 'ngModel',
    link: function(scope, elem, attrs, ctrl) {
      ctrl.$setValidity("unique", false);
      elem.keyup(function(evt) {
        var options = { type:"user", email: ctrl.$modelValue, name: ctrl.$modelValue };
        if (ctrl.$modelValue && ctrl.$modelValue.trim().length > 0) {
          client.getEntity(options, function(err, data) {
            ctrl.$setValidity("unique", err); // error means unique
            scope.$apply();
          });
        } else {
          ctrl.$setValidity("unique", false); // consider empty string non unique
          scope.$apply();
        }
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
