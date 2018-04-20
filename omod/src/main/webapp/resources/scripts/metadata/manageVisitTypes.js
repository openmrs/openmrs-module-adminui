angular.module("manageVisitTypes", [ "visitTypeService", "ngDialog", "ui.router", "uicommons.filters", "uicommons.common.error"])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManageVisitTypesController"
            })
            .state("edit", {
                url: "/edit/:visitTypeUuid",
                templateUrl: "templates/edit.page",
                params: {
                    visitTypeUuid: null,
                },
                resolve: {
                    visitType: function($stateParams, VisitType) {
                        if ($stateParams.visitTypeUuid) {
                            return VisitType.get({ uuid: $stateParams.visitTypeUuid, v: "full" });
                        }
                        return {};
                    }
                },
                controller: "EditVisitTypeController"
            });
    }])

    .controller("ManageVisitTypesController", [ "$scope", "$state", "VisitType", "VisitTypeService", "ngDialog",
        function($scope, $state, VisitType, VisitTypeService, ngDialog) {
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadVisitTypes() {
                VisitTypeService.getVisitTypes({ v: "default", includeAll: true }).then(function(results) {
                    $scope.visitTypes = sortWithRetiredLast(results);
                });
            }

            $scope.retire = function(visitType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/retireVisitTypeDialog.page",
                    controller: function($scope) {
                        $scope.visitType = visitType;
                    }
                }).then(function(reason) {
                    VisitType.delete({
                        uuid: visitType.uuid,
                        reason: reason
                    }).$promise.then(function() {
                        loadVisitTypes();
                        emr.successMessage(emr.message("adminui.retired"));
                    });
                });
            }

            $scope.unretire = function(visitType) {
                VisitType.save({
                    uuid: visitType.uuid,
                    retired: false
                }).$promise.then(function() {
                    loadVisitTypes();
                    emr.successMessage(emr.message("adminui.restored"));
                })
            }

            $scope.edit = function(visitType) {
                $state.go("edit", { visitvisitTypeUuid: visitType.uuid });
            },

                $scope.purge = function(visitType) {
                    ngDialog.openConfirm({
                        showClose: false,
                        closeByEscape: true,
                        closeByDocument: true,
                        template: "templates/purgeVisitTypeDialog.page",
                        controller: function($scope) {
                            $scope.visitType = visitType;
                        }
                    }).then(function() {
                        VisitType.delete({
                            uuid: visitType.uuid,
                            purge: ""
                        }).$promise.then(function() {
                            loadVisitTypes();
                            emr.successMessage(emr.message("adminui.purged"));
                        })
                    });
                }

            loadVisitTypes();
        }])

    .controller("EditVisitTypeController", [ "$scope", "$state", "VisitType", "visitType",
        function($scope, $state, VisitType, visitType) {
            $scope.visitType = visitType;

            $scope.save = function() {
                var toSave = {
                    uuid: $scope.visitType.uuid,
                    name: $scope.visitType.name,
                    description: $scope.visitType.description
                }

                var successMessageCode = ($scope.visitType.uuid) ? "adminui.savedChanges" : "adminui.saved";
                VisitType.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message(successMessageCode));
                })
            }
        }]);