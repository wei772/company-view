var appControllers = angular.module('appControllers', []);
appControllers.controller('LoginController', loginController);
appControllers.controller('RegistrationController', registrationController);
appControllers.controller('NavbarController', navbarController);

function registrationController($scope, $http, $timeout, $location) {
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
            if (!data.success) {
                if (data.errorFields) {
                    // If password has error then mark both, password and passwordConf red. Same goes to e-mail.
                    addBothIfOneExists(data.errorFields, "password", "passwordConf");
                    addBothIfOneExists(data.errorFields, "email", "emailConf");

                    var fieldIds = convertFieldNamesToFieldIds(data.errorFields);
                    addErrorHighlights(fieldIds);

                    var errorMessages = data.errorMessages;
                    var index;
                    var message = "<ul class='list-unstyled'>";
                    for (index = 0; index < errorMessages.length; index += 1) {
                        message += "<li>" + errorMessages[index] + "</li>";
                    }
                    message += "</ul>";
                    showFailMessage("register-message", "Failed to register.", message)
                }
            } else {
                hideForm("cv-registration-block");
                showSuccessMessage("register-message", "Account created.", "Redirecting in " + 5 + " seconds...");
                $timeout(function() {showSuccessMessage("register-message", "Account created.", "Redirecting in " + 4 + " seconds...");}, 1000);
                $timeout(function() {showSuccessMessage("register-message", "Account created.", "Redirecting in " + 3 + " seconds...");}, 2000);
                $timeout(function() {showSuccessMessage("register-message", "Account created.", "Redirecting in " + 2 + " seconds...");}, 3000);
                $timeout(function() {showSuccessMessage("register-message", "Account created.", "Redirecting in " + 1 + " seconds...");}, 4000);
                $timeout(function() {$location.path("/login");}, 5000);
            }
        });

        request.error(function() {
            $scope.isRegistering = false;
            showFailMessage("register-message", "Failed to register.", "Server might be down or broken.");
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
            if (!data.success) {
                addErrorHighlights(['cv-username-field', 'cv-password-field']);
                showFailMessage('login-fail-message', 'Login failed.', data.message);
            } else {
                $window.localStorage['username'] = $scope.username;
                $window.localStorage['token'] = data.token;
                $location.path("/");
            }
        });

        request.error(function() {
            $scope.isRequestingLogin = false;
            $window.localStorage.removeItem('username');
            $window.localStorage.removeItem('token');
            showFailMessage("login-fail-message", "Failed to login.", "Server might be down or broken.");
            addErrorHighlights(['cv-username-field', 'cv-password-field']);
        });
    };
}

function navbarController($scope, $window) {
    $scope.username = $window.localStorage['username'];
}