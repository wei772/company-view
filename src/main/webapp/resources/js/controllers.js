var appControllers = angular.module('appControllers', []);
appControllers.controller('LoginController', loginController);
appControllers.controller('RegistrationController', registrationController);
appControllers.controller('NavbarController', navbarController);
appControllers.controller('UpdatePasswordController', updatePasswordController);
appControllers.controller('UpdateDetailsController', updateDetailsController);
appControllers.controller('NewInternshipController', newInternshipController);

function newInternshipController($scope, $http) {
    var addOffer = function(doPublish) {
        $scope.isSubmitting = true;

        var request = $http({
            url: '/offer/internship',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'title': $scope.title,
                'expirationTime': $('#expirationtime').val(),
                'content': $('#internship-content').code(),
                'publish': doPublish
            }
        });

        request.success(function(data) {
            $scope.isSubmitting = false;
            if (!data.success) {
                if (data.errorFields) {
                    var contentIndex = jQuery.inArray("content", data.errorFields);
                    if (contentIndex != -1) { data.errorFields.splice(contentIndex, 1); }
                    addErrorHighlights(convertFieldNamesToFieldIds(data.errorFields));
                    showFailMessage("Failed to create offer.", createErrorMessagesHtml(data.errorMessages));
                }
            } else {
                console.log("redirect to the view of this offer.");
                showSuccessMessage("New offer created.", "Your offer has been added.");
            }
        });

        request.error(function() {
            $scope.isSubmitting = false;
            showFailMessage("Failed to create offer.", "Server might be down or broken.");
        });
    };

    $scope.add = function() {
        addOffer(false);
    };

    $scope.addAndPublish = function () {
        addOffer(true);
    };
}

function updateDetailsController($scope, $http) {
    $http.get('/account/mydetails').then(function(res){
        $scope.email = res.data.email;
        $scope.emailConf = res.data.email;
        $scope.firstName = res.data.firstName;
        $scope.lastName = res.data.lastName;
        $scope.organisation = res.data.companyName;
        $scope.telephone = res.data.phone;
        $scope.address = res.data.address;
    });

    $scope.updateDetails = function() {
        var confErrors = [];
        validateConfirmations('cv-email-field', 'cv-emailconf-field', confErrors, 'E-mails do not match.');
        if (confErrors.length > 0) {
            showFailMessage('Failed to change details.', createErrorMessagesHtml(confErrors));
            return;
        }

        $scope.isUpdating = true;
        var request = $http({
            url: '/account/mydetails',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'email': $scope.email,
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
                    addErrorHighlights(errorFieldIds);
                    showFailMessage("Failed to update details.", createErrorMessagesHtml(data.errorMessages));
                }
            } else {
                showSuccessMessage("Details changed.", "Your details have been successfully changed.");
            }
        });

        request.error(function() {
            $scope.isUpdating = false;
            showFailMessage("Failed to update details.", "Server might be down or broken.");
        });
    }
}

function updatePasswordController($scope, $http) {
    $scope.updatePassword = function() {
        var confErrors = [];
        validateConfirmations('cv-newpassword-field', 'cv-newpasswordconf-field', confErrors, 'Passwords do not match.');
        if (confErrors.length > 0) {
            showFailMessage('Failed to change password.', createErrorMessagesHtml(confErrors));
            return;
        }

        $scope.isUpdatingPassword = true;
        var request = $http({
            url: '/account/password',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'password': $scope.password,
                'newPassword': $scope.newPassword
            }
        });

        request.success(function(data) {
            $scope.isUpdatingPassword = false;
            if (!data.success) {
                if (data.errorFields) {
                    addBothIfOneExists(data.errorFields, "newPassword", "newPasswordConf");
                    addErrorHighlights(convertFieldNamesToFieldIds(data.errorFields));
                    showFailMessage("Failed to change password.", createErrorMessagesHtml(data.errorMessages));
                }
            } else {
                showSuccessMessage("Password changed.", "Your password has been successfully changed");
                emptyAllInputs();
            }
        });

        request.error(function() {
            $scope.isUpdatingPassword = false;
            showFailMessage("Failed to change password.", "Server might be down or broken.");
        });
    }
}

function registrationController($scope, $http, $timeout, $location) {
    $scope.register = function() {
        var confErrors = [];
        validateConfirmations('cv-email-field', 'cv-emailconf-field', confErrors, 'E-mails do not match.');
        validateConfirmations('cv-password-field', 'cv-passwordconf-field', confErrors, 'Passwords do not match.');
        if (confErrors.length > 0) {
            showFailMessage('Failed to register.', createErrorMessagesHtml(confErrors));
            return;
        }

        $scope.isRegistering = true;
        var request = $http({
            url: '/register',
            contentType: "application/json",
            dataType: "json",
            method: 'POST',
            params: {
                'username': $scope.username,
                'email': $scope.email,
                'firstName': $scope.firstName,
                'lastName': $scope.lastName,
                'organisation': $scope.organisation,
                'telephone': $scope.telephone,
                'address': $scope.address,
                'password': $scope.password
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
                    showFailMessage("Failed to register.", createErrorMessagesHtml(data.errorMessages));
                }
            } else {
                hideForm("cv-registration-block");
                showSuccessMessage("Account created.", "Redirecting in " + 4 + " seconds...");
                $timeout(function() {showSuccessMessage("Account created.", "Redirecting in " + 3 + " seconds...");}, 1000);
                $timeout(function() {showSuccessMessage("Account created.", "Redirecting in " + 2 + " seconds...");}, 2000);
                $timeout(function() {showSuccessMessage("Account created.", "Redirecting in " + 1 + " seconds...");}, 3000);
                $timeout(function() {$location.path("/login");}, 4000);
            }
        });

        request.error(function() {
            $scope.isRegistering = false;
            showFailMessage("Failed to register.", "Server might be down or broken.");
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
                showFailMessage('Login failed.', data.errorMessages[0]);
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
            showFailMessage("Failed to login.", "Server might be down or broken.");
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