angular.module('adminui.should-match', [])

    .directive('shouldMatch', function() {
        return {
            restrict: 'A',
            require: 'ngModel',
            scope: {
                matchedInputModelValue: "=shouldMatch"
            },
            link: function(scope, elm, attrs, ngModel) {
                ngModel.$validators.shouldMatch = function(modelValue) {
                    return jq.trim(modelValue) == jq.trim(scope.matchedInputModelValue);
                };

                scope.$watch("matchedInputModelValue", function() {
                    ngModel.$validate();
                });
            }
        };
    });