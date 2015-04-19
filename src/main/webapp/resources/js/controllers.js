var appControllers = angular.module('appControllers', []);
appControllers.controller('LoginController', loginController);
appControllers.controller('RegistrationController', registrationController);
appControllers.controller('NavbarController', navbarController);
appControllers.controller('UpdatePasswordController', updatePasswordController);
appControllers.controller('UpdateDetailsController', updateDetailsController);

function updateDetailsController($scope, $http) {
    $http.get('/account/mydetails').then(function(res){
        $scope.username = res.data.username;
        $scope.email = res.data.email;
        $scope.emailConf = res.data.email;
        $scope.firstName = res.data.firstName;
        $scope.lastName = res.data.lastName;
        $scope.organisation = res.data.companyName;
        $scope.telephone = res.data.phone;
        $scope.address = res.data.address;
    });

    $scope.updateDetails = function() {
        $scope.isUpdating = true;

        var request = $http({
            url: '/account/mydetails',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'email': $scope.email,
                'emailConf': $scope.emailConf,
                'firstName': $scope.firstName,
                'lastName': $scope.lastName,
                'organisation': $scope.organisation,
                'telephone': $scope.telephone,
                'address': $scope.address
            }
        });

        request.success(function(data) {
            $scope.isUpdating = false;
            if (!data.success) {
                if (data.errorFields) {
                    addBothIfOneExists(data.errorFields, "email", "emailConf");
                    var errorFieldIds = convertFieldNamesToFieldIds(data.errorFields);
                    console.log(errorFieldIds);
                    addErrorHighlights(errorFieldIds);
                    showFailMessage("response-message", "Failed to update details.", createErrorMessagesHtml(data.errorMessages));
                }
            } else {
                showSuccessMessage("response-message", "Details changed.", "Your details have been successfully changed.");
            }
        });

        request.error(function() {
            $scope.isUpdating = false;
            showFailMessage("response-message", "Failed to update details.", "Server might be down or broken.");
        });
    }
}

function updatePasswordController($scope, $http) {
    $scope.updatePassword = function() {
        $scope.isUpdatingPassword = true;

        var request = $http({
            url: '/account/password',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'password': $scope.password,
                'newPassword': $scope.newPassword,
                'newPasswordConf': $scope.newPasswordConf
            }
        });

        request.success(function(data) {
            $scope.isUpdatingPassword = false;
            if (!data.success) {
                if (data.errorFields) {
                    addBothIfOneExists(data.errorFields, "newPassword", "newPasswordConf");
                    addErrorHighlights(convertFieldNamesToFieldIds(data.errorFields));
                    showFailMessage("response-message", "Failed to change password.", createErrorMessagesHtml(data.errorMessages));
                }
            } else {
                showSuccessMessage("response-message", "Password changed.", "Your password has been successfully changed");
                emptyAllInputs();
            }
        });

        request.error(function() {
            $scope.isUpdatingPassword = false;
            showFailMessage("response-message", "Failed to change password.", "Server might be down or broken.");
        });
    }
}

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

                    addErrorHighlights(convertFieldNamesToFieldIds(data.errorFields));
                    showFailMessage("register-message", "Failed to register.", createErrorMessagesHtml(data.errorMessages));
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
                showFailMessage('login-fail-message', 'Login failed.', data.errorMessages[0]);
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

    $scope.logout = function() {
        $window.localStorage.removeItem('username');
        $window.localStorage.removeItem('token');
    }
}