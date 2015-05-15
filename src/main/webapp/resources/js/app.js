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
            templateUrl: "/views/offer/internship/all.html"
        }).state('register', {
            url: "/register",
            templateUrl: "/views/register.html",
            data: {requireLogin: false}
        }).state('yourInternshipOffers', {
            url: "/offer/internship/your",
            templateUrl: "/views/offer/internship/your.html",
            data: {requireLogin: true}
        }).state('allInternshipOffers', {
            url: "/offer/internship/all?page",
            templateUrl: "/views/offer/internship/all.html",
            data: {requireLogin: true}
        }).state('newInternshipOffer', {
            url: "/offer/internship/new",
            templateUrl: "/views/offer/internship/new.html",
            data: {requireLogin: true}
        }).state('viewInternshipOffer', {
            url: "/offer/internship/view/:internshipOfferId",
            templateUrl: "/views/offer/internship/view.html",
            data: {requireLogin: true}
        }).state('editInternshipOffer', {
            url: "/offer/internship/edit/:internshipOfferId",
            templateUrl: "/views/offer/internship/edit.html",
            data: {requireLogin: true}
        }).state('accountPassword', {
            url: "/account/password",
            templateUrl: "/views/account/password.html",
            data: {requireLogin: true}
        }).state('accountDetails', {
            url: "/account/details",
            templateUrl: "/views/account/details.html",
            data: {requireLogin: true}
        }).state('searchInternships', {
            url: "/offer/internship/search?page&keyword",
            templateUrl: "/views/offer/internship/search.html",
            data: {requireLogin: true}
        });
});

app.run(function ($rootScope, $window, $location) {
    $rootScope.$on('$stateChangeStart', function (event, toState) {
        var requireLogin = toState.data.requireLogin;
        if (requireLogin && (!$window.localStorage['username'] || !$window.localStorage['token'])) {
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
            if ($window.localStorage['username'] && $window.localStorage['token']) {
                var auth = {
                    'username': $window.localStorage['username'].toString(),
                    'token': $window.localStorage['token'].toString()
                };
                config.headers.Authorization = JSON.stringify(auth);
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