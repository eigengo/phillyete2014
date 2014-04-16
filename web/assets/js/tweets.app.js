angular.module('tweets.app', ['d3.directives', 'tweets.directives'])
  .controller('TweetsCtrl', ['$scope', function($scope) {
    $scope.values = {};
    $scope.expression = "eclipse";
    $scope.running = false;

    var xhr = null;
    var offset = 3;
    $scope.stop = function() {
      if (xhr != null) xhr.abort();
      xhr = null;
      $scope.running = false;
    };

    $scope.executeExpression = function() {
      if (xhr != null) xhr.abort();
      var xhr = new XMLHttpRequest();
      xhr.onprogress = function () {
        $scope.$apply(function() {
          var chunk = xhr.responseText.substring(offset);
          offset += chunk.length;
          if (chunk.length > 0) $scope.values = JSON.parse(chunk);
        });
      };
      xhr.open("POST", "http://localhost:8080/tweets/" + encodeURIComponent($scope.expression), true);
      xhr.send();
      $scope.running = true;
    };

  }]);
