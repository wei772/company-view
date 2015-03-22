var app = angular.module('app', ['ui.router', 'appControllers']);

app.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('', '/');
    $stateProvider
        .state('login', {
            url: "/login",
            templateUrl: "/views/login.html",
            data: {requireLogin: false}
        }).state('home', {
            url: "/",
            data: {requireLogin: true},
            templateUrl: "/views/home.html"
        }).state('register', {
            url: "/register",
            templateUrl: "/views/register.html",
            data: {requireLogin: false}
        }).state('users', {
            url: "/users",
            templateUrl: "/views/users.html",
            data: {requireLogin: true}
        }).state('offers', {
            url: "/offers",
            templateUrl: "/views/offers.html",
            data: {requireLogin: true}
        }).state('accountPassword', {
            url: "/account/password",
            templateUrl: "/views/account/password.html",
            data: {requireLogin: true}
        }).state('accountDetails', {
            url: "/account/details",
            templateUrl: "/views/account/details.html",
            data: {requireLogin: true}
        }).state('accountInbox', {
            url: "/account/inbox",
            templateUrl: "/views/account/inbox.html",
            data: {requireLogin: true}
        });
});

app.run(function ($rootScope, $window, $location) {
    $rootScope.$on('$stateChangeStart', function (event, toState) {
        var requireLogin = toState.data.requireLogin;
        if (requireLogin && !$window.localStorage.authInfo) {
            console.log("User is not authenticated, redirecting to login.");
            $location.path("/login");
        }
    });
});

// Removes # from URL.
app.config(['$locationProvider', function($locationProvider){
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    }).hashPrefix('!');
}]);

app.factory('tokenInterceptor', function ($rootScope, $q, $window) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            if ($window.localStorage.authInfo) {
                config.headers.Authorization = JSON.stringify($window.localStorage.authInfo);
            }
            return config;
        },
        response: function (response) {
            return response || $q.when(response);
        }
    };
});

// Add Authorization header.
app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('tokenInterceptor');
});