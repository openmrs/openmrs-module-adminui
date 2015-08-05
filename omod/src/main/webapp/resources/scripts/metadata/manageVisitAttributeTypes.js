angular.module("manageVisitAttributeTypes", [ "visitAttributeTypeService", "customDatatypeService", "ngDialog", "ui.router", "uicommons.filters" ])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManageVisitAttributeTypesController"
            })
            .state("edit", {
                url: "/edit/:visitAttributeTypeUuid",
                templateUrl: "templates/edit.page",
                params: {
                    visitAttributeTypeUuid: null,
                },
                resolve: {
                    visitAttributeType: function($stateParams, VisitAttributeType) {
                        if ($stateParams.visitAttributeTypeUuid) {
                            return VisitAttributeType.get({ uuid: $stateParams.visitAttributeTypeUuid, v: "full" });
                        }
                        return {};
                    }
                },
                controller: "EditVisitAttributeTypeController"
            });
    }])

    .controller("ManageVisitAttributeTypesController", [ "$scope", "$state", "VisitAttributeType", "ngDialog",
        function($scope, $state, VisitAttributeType, ngDialog) {
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadVisitAttributeTypes() {
                // TODO standard function for failure of REST call
                VisitAttributeType.query({ v: "default", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.visitAttributeTypes = sortWithRetiredLast(response.results);
                });
            }

            $scope.retire = function(visitAttributeType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/retireVisitAttributeTypeDialog.page",
                    controller: function($scope) {
                        $scope.visitAttributeType = visitAttributeType;
                    }
                }).then(function(reason) {
                    VisitAttributeType.delete({
                        uuid: visitAttributeType.uuid,
                        reason: reason
                    }).$promise.then(function() {
                        loadVisitAttributeTypes();
                    });
                });
            }

            $scope.unretire = function(visitAttributeType) {
                // will fail until RESTWS-456
                VisitAttributeType.save({
                    uuid: visitAttributeType.uuid,
                    retired: false
                }).$promise.then(function() {
                    loadVisitAttributeTypes();
                })
            }

            $scope.edit = function(visitAttributeType) {
                $state.go("edit", { visitAttributeTypeUuid: visitAttributeType.uuid });
            }

            loadVisitAttributeTypes();
        }])

    .controller("EditVisitAttributeTypeController", [ "$scope", "$state", "VisitAttributeType", "visitAttributeType", "CustomDatatype",
        function($scope, $state, VisitAttributeType, visitAttributeType, CustomDatatype) {
            $scope.visitAttributeType = visitAttributeType;
            if (!$scope.visitAttributeType.minOccurs) {
            	$scope.visitAttributeType.minOccurs = 0;
            }
            
            CustomDatatype.query({ v: "full"}).$promise.then(function(response) {
                $scope.customDatatypes = response.results;
            });
            
            $scope.save = function() {
            	$scope.visitAttributeType.datatypeClassname = $scope.visitAttributeType.datatypeClassname.datatypeClassname;
                VisitAttributeType.save($scope.visitAttributeType).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message("uicommons.generalSavedNotification"));
                }, function() {
                    // TODO handle server-side errors
                })
            }
        }]);