angular.module("manageProviderAttributeTypes", [ "providerAttributeTypeService", "ngDialog", "ui.router", "uicommons.filters", "uicommons.common.error"])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManageProviderAttributeTypesController"
            })
            .state("edit", {
                url: "/edit/:providerAttributeTypeUuid",
                templateUrl: "templates/edit.page",
                params: {
                	providerAttributeTypeUuid: null,
                },
                resolve: {
                	providerAttributeType: function($stateParams, ProviderAttributeType) {
                        if ($stateParams.providerAttributeTypeUuid) {
                            return ProviderAttributeType.get({ uuid: $stateParams.providerAttributeTypeUuid, v: "full" });
                        }
                        return {};
                    }
                },
                controller: "EditProviderAttributeTypeController"
            });
    }])

    .controller("ManageProviderAttributeTypesController", [ "$scope", "$state", "ProviderAttributeType", "ProviderAttributeTypeService", "ngDialog",
        function($scope, $state, ProviderAttributeType, ProviderAttributeTypeService, ngDialog) {
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadProviderAttributeTypes() {
                // TODO standard function for failure of REST call
            	ProviderAttributeTypeService.getProviderAttributeTypes({ v: "default", includeAll: true }).then(function(results) {
                    $scope.providerAttributeTypes = sortWithRetiredLast(results);
                });
            }

            $scope.retire = function(providerAttributeType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/retireProviderAttributeTypeDialog.page",
                    controller: function($scope) {
                        $scope.providerAttributeType = providerAttributeType;
                    }
                }).then(function(reason) {
                	ProviderAttributeType.delete({
                        uuid: providerAttributeType.uuid,
                        reason: reason
                    }).$promise.then(function() {
                        loadProviderAttributeTypes();
                            emr.successMessage(emr.message("adminui.retired"));
                    });
                });
            }

            $scope.unretire = function(providerAttributeType) {
            	ProviderAttributeType.save({
                    uuid: providerAttributeType.uuid,
                    retired: false
                }).$promise.then(function() {
                    loadProviderAttributeTypes();
                        emr.successMessage(emr.message("adminui.restored"));
                })
            }

            $scope.edit = function(providerAttributeType) {
                $state.go("edit", { providerAttributeTypeUuid: providerAttributeType.uuid });
            },

            $scope.purge = function(providerAttributeType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/purgeProviderAttributeTypeDialog.page",
                    controller: function($scope) {
                        $scope.providerAttributeType = providerAttributeType;
                    }
                }).then(function() {
                	ProviderAttributeType.delete({
                        uuid: providerAttributeType.uuid,
                        purge: ""
                    }).$promise.then(function() {
                        loadProviderAttributeTypes();
                        emr.successMessage(emr.message("adminui.purged"));
                    })
                });
            }

            loadProviderAttributeTypes();
        }])

    .controller("EditProviderAttributeTypeController", [ "$scope", "$state", "ProviderAttributeType", "providerAttributeType",
        function($scope, $state, ProviderAttributeType, providerAttributeType) {
            $scope.providerAttributeType = providerAttributeType;

            $scope.save = function() {
                var toSave = {
                    uuid: $scope.providerAttributeType.uuid,
                    name: $scope.providerAttributeType.name,
                    description: $scope.providerAttributeType.description,
                    minOccurs: $scope.providerAttributeType.minOccurs,
                    maxOccurs: $scope.providerAttributeType.maxOccurs,
                    datatypeClassname: $scope.providerAttributeType.datatypeClassname ? $scope.providerAttributeType.datatypeClassname : null,
                    datatypeConfig: $scope.providerAttributeType.datatypeConfig,
                    preferredHandlerClassname: $scope.providerAttributeType.preferredHandlerClassname ? $scope.providerAttributeType.preferredHandlerClassname : null,
                    handlerConfig: $scope.providerAttributeType.handlerConfig
                }

                var successMessageCode = ($scope.providerAttributeType.uuid) ? "adminui.savedChanges" : "adminui.saved";
                ProviderAttributeType.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message(successMessageCode));
                })
            }
        }]);