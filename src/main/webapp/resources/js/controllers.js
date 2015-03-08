var appControllers = angular.module('appControllers', []);
//appControllers.controller('NavbarController', navbarController);
appControllers.controller('LoginController', loginController);
appControllers.controller('RegistrationController', registrationController);

function registrationController($scope, $http, $window, $location) {
    $scope.register = function() {
        var request = $http({
            url: '/register',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'email': $scope.email,
                'password': $scope.password,
                'username': $scope.username,
                'firstName': $scope.firstName,
                'lastName': $scope.lastName,
                'orgName': $scope.orgName,
                'telephone': $scope.telephone,
                'address': $scope.address
            }
        });

        request.success(function(data) {
            $location.path('/login');
        });

        request.error(function(status, data, response, header) {
            document.getElementById('registrationError').style.display = "";
            document.getElementById('registrationErrorMessage').innerHTML = "something went bad";
        });
    };
};

function loginController($scope, $http, $window, $location) {
    $scope.login = function() {
        var request = $http({
            url: '/login',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {'username': $scope.username, 'password': $scope.password}
        });

        request.success(function(data) {
            var user = {username: data.username, token: data.token};
            $window.sessionStorage.user = JSON.stringify(user);
            $location.path('/home');
        });

        request.error(function(status, data, response, header) {
            delete $window.sessionStorage.user;
            document.getElementById('username').classList.add('has-error');
            document.getElementById('password').classList.add('has-error');
        });
    };
};
/*
function navbarController($scope, $http, $window, $location) {
    $scope.logout = function() {
        delete $window.sessionStorage.user;
        $location.path('/');
    };

    $scope.isActive = function(route) {
        return route === $location.path();
    }
}*/