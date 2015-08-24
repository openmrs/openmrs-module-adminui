angular.module("colTable", [  ])
    
    .directive('omColtable', function() {
        return {
            scope: {
                main: "=omCtMain", 
                cols: "=omCtCols", 
                ckb: "=omCtCkb", 
                sec: "=omCtSec"
            },
            template: '\
            <table>\
                <tbody>\
                    <tr ng-repeat="(idx, val) in main" ng-hide="idx % cols">\
                        <td>\
                            <input type="checkbox" ng-model="sec[idx]"  ng-if="ckb">\
                            {{main[idx].name}}\
                        </td>\
                        <td ng-hide="!main[idx + 1]" ng-if="cols > 1">\
                            <input type="checkbox" ng-model="sec[idx + 1]"  ng-if="ckb">\
                            {{main[idx + 1].name}}\
                        </td>\
                        <td ng-hide="!main[idx + 2]" ng-if="cols > 2">\
                            <input type="checkbox" ng-model="sec[idx + 2]"  ng-if="ckb">\
                            {{main[idx + 2].name}}\
                        </td>\
                    </tr>\
                </tbody>\
            </table>\
                      '                      
        };
    });