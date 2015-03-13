var appControllers = angular.module('appControllers', []);
appControllers.controller('LoginController', loginController);
appControllers.controller('RegistrationController', registrationController);

function registrationController($scope, $http, $window, $location) {
    $scope.register = function() {
        $scope.isRegistering = true;

        var request = $http({
            url: '/register',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'username': $scope.username,
                'email': $scope.email,
                'emailConf': $scope.emailConf,
                'firstName': $scope.firstName,
                'lastName': $scope.lastName,
                'organisation': $scope.organisation,
                'telephone': $scope.telephone,
                'address': $scope.address,
                'password': $scope.password,
                'passwordConf': $scope.passwordConf
            }
        });

        request.success(function(data) {
            $scope.isRegistering = false;
            // TODO: WHEN UNSUCCESSFUL, ADD HIGHLIGHTS
        });

        request.error(function() {
            $scope.isRegistering = false;
            showFailMessage("register-fail-message", "Failed to register.", "Server responded stuff I'm not able to parse or identify.");
        });
    };
}

function loginController($scope, $http, $window, $location) {
    $scope.login = function() {
        $scope.isRequestingLogin = true;
        var request = $http({
            url: '/login',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {'username': $scope.username, 'password': $scope.password}
        });

        request.success(function(data) {
            $scope.isRequestingLogin = false;
            alert("login undone.");
            // TODO: WHEN UNSUCCESSFUL, ADD HIGHLIGHTS
            //var user = {username: data.username, token: data.token};
            //$window.sessionStorage.token = JSON.stringify(user);
            //$location.path('/home');
        });

        request.error(function() {
            $scope.isRequestingLogin = false;
            //delete $window.sessionStorage.token;
            showFailMessage("login-fail-message", "Failed to login.", "Server responded stuff I'm not able to parse or identify.");
            addErrorHighlights(['cv-username-field', 'cv-password-field']);
        });
    };
}