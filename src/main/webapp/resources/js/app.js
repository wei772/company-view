var app = angular.module('app', ['ui.router', 'appControllers']);

app.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.when('', '/');

    $stateProvider
        .state('login', {
            url: "/",
            templateUrl: "/views/login.html"
        })
        .state('home', {
            url: "/home",
            templateUrl: "/views/home.html"
        }).state('register', {
            url: "/register",
            templateUrl: "/views/register.html"
        });
});

// Removes # from URL.
app.config(['$locationProvider', function($locationProvider){
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    }).hashPrefix('!');
}]);

// Adds authorization header.
/*
app.factory('tokenInterceptor', function ($rootScope, $q, $window) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            if ($window.sessionStorage.user) {
                config.headers.Authorization = $window.sessionStorage.user;
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
*/