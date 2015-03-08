var appControllers = angular.module('appControllers', []);
//vinylControllers.controller('LoginController', loginController);
appControllers.controller('NavbarController', navbarController)

/*
function loginController($scope, $http, $window, $location) {
    $scope.getToken = function() {
        $scope.loginRequesting = true;
        var request = $http({
            url: '/token',
            method: 'POST',
            params: {'username': $scope.username, 'passwordMd5': $.md5($scope.password)}
        });

        request.success(function(data) {
            $scope.loginRequesting = false;
            var user = {username: data.username, accessToken: data.accessToken};
            $window.sessionStorage.user = JSON.stringify(user);
            $location.path('/music');
        });

        request.error(function(status, data, response, header) {
            $scope.loginRequesting = false;
            delete $window.sessionStorage.user;
            document.getElementById('login-username').classList.add('has-error');
            document.getElementById('login-password').classList.add('has-error');
        });
    };
}*/

function navbarController($scope, $http, $window, $location) {
    $scope.logout = function() {
        delete $window.sessionStorage.user;
        $location.path('/');
    };

    $scope.isActive = function(route) {
        return route === $location.path();
    }
}