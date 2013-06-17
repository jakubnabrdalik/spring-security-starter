var app = angular.module('angular-auth', []);

app.config(function($httpProvider) {

    var AUTHORIZATION_HEADER = "Authorization";
   	var WWW_AUTHENTICATE_HEADER = 'WWW-Authenticate';
    var cnonce = 'testCnonce'
    var nc = '00000001'

    var authInterceptor = ['$rootScope', '$q', function(scope, $q) {

        function success(response) {
            return response;
        }

        function error(response) {
            var status = response.status;
            var auth = response.config.auth;
            if (status == 403 && !auth) {
                return retryWithDigest(response);
            }
            return $q.reject(response);
        }

        function retryWithDigest(response) {
            var deferred = $q.defer();
            var authHeader = response.headers(WWW_AUTHENTICATE_HEADER);
            var params = parseAuthenticateParams(authHeader);
            var uri = response.config.url;
            var username = response.config.username;
            var password = response.config.password;
            var calcResponse = calculateResponse(username, password, uri, params.nonce, params['Digest realm'], params.qop);
            var authorizationHeaderValue = generateAuthorizationHeader(username, authHeader, calcResponse, uri);
            var headers = {};
            headers[AUTHORIZATION_HEADER] = authorizationHeaderValue;
            response.config.headers = headers;
            response.config.auth = true;
            scope.$emit('retry', {config: response.config, deferred: deferred})
            return deferred.promise;
        }

        function parseAuthenticateParams(authenticateParams) {
            var params = authenticateParams.split(',');
            var map = new Object();
            angular.forEach(params, function(param) {
                var name = param.substring(0, param.indexOf("=")).trim()
                var value = param.substring(param.indexOf("=")+2, param.length-1)
                this[name] = value
            }, map)
            return map;

        }

        function calculateResponse(username, password, uri, nonce, realm, qop) {
       		var a2 = "GET:" + uri;
       		var a2Md5 = hex_md5(a2);
       		var a1Md5 = hex_md5(username + ":" + realm + ":" + password);
       		var digest = a1Md5 + ":" + nonce + ":" + nc + ":" + cnonce + ":" + qop + ":" +a2Md5;
       		return hex_md5(digest);
       	}

       	function generateAuthorizationHeader(username, wwwAuthenticationHeader, response, uri) {
       		return wwwAuthenticationHeader+', username="'+username+'", uri="'+
       			   uri+'", response="'+response+'", nc='+
       			   nc+', cnonce="'+cnonce+'"';
       	}
        return function(promise) {
            return promise.then(success, error);
        }
    }]
    $httpProvider.responseInterceptors.push(authInterceptor);
});


app.run(['$rootScope', '$http', function(scope, $http) {
    scope.$on('retry', function(event, request) {
        $http(request.config).then(function(response) {
            request.deferred.resolve(response);
        }, function(response) {
            request.deferred.reject(response);
        })
    });
}]);

function SecuredCtrl($scope, $http) {
    $scope.data = '';

    $scope.push = function(login, password) {
        console.log(login);
        console.log(password);
        $http({method: 'GET', url: '/spring-security-starter/backend/password',
               auth: false, username: login, password: password}).
            success(function(data, status, headers, config) {
                $scope.data = data;
            }).
            error(function(data, status, headers, config) {
                $scope.data = 'Access denied!'
            });
    }
};