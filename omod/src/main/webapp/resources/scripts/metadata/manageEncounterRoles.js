angular.module("manageEncounterRoles", [ "encounterRoleService", "ngDialog", "ui.router", "uicommons.filters", "uicommons.common.error"])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManageEncounterRolesController"
            })
            .state("edit", {
                url: "/edit/:encounterRoleUuid",
                templateUrl: "templates/edit.page",
                params: {
                    encounterRoleUuid: null,
                },
                resolve: {
                    encounterRole: function($stateParams, EncounterRole) {
                        if ($stateParams.encounterRoleUuid) {
                            return EncounterRole.get({ uuid: $stateParams.encounterRoleUuid, v: "full" });
                        }
                        return {};
                    }
                },
                controller: "EditEncounterRoleController"
            });
    }])

    .controller("ManageEncounterRolesController", [ "$scope", "$state", "EncounterRole", "EncounterRoleService", "ngDialog",
        function($scope, $state, EncounterRole, EncounterRoleService, ngDialog) {
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadEncounterRoles() {
                // TODO standard function for failure of REST call
                EncounterRoleService.getEncounterRoles({ v: "default", includeAll: true }).then(function(results) {
                    $scope.encounterRoles = sortWithRetiredLast(results);
                });
            }

            $scope.retire = function(encounterRole) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/retireEncounterRoleDialog.page",
                    controller: function($scope) {
                        $scope.encounterRole = encounterRole;
                    }
                }).then(function(reason) {
                    EncounterRole.delete({
                        uuid: encounterRole.uuid,
                        reason: reason
                    }).$promise.then(function() {
                        loadEncounterRoles();
                            emr.successMessage(emr.message("adminui.retired"));
                    });
                });
            }

            $scope.unretire = function(encounterRole) {
                EncounterRole.save({
                    uuid: encounterRole.uuid,
                    retired: false
                }).$promise.then(function() {
                    loadEncounterRoles();
                        emr.successMessage(emr.message("adminui.restored"));
                })
            }

            $scope.edit = function(encounterRole) {
                $state.go("edit", { encounterRoleUuid: encounterRole.uuid });
            },

            $scope.purge = function(encounterRole) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/purgeEncounterRoleDialog.page",
                    controller: function($scope) {
                        $scope.encounterRole = encounterRole;
                    }
                }).then(function() {
                    EncounterRole.delete({
                        uuid: encounterRole.uuid,
                        purge: ""
                    }).$promise.then(function() {
                        loadEncounterRoles();
                        emr.successMessage(emr.message("adminui.purged"));
                    })
                });
            }

            loadEncounterRoles();
        }])

    .controller("EditEncounterRoleController", [ "$scope", "$state", "EncounterRole", "encounterRole",
        function($scope, $state, EncounterRole, encounterRole) {
            $scope.encounterRole = encounterRole;

            $scope.save = function() {
                // TODO: utility function for doing this, until RESTWS-460 is fixed
                var toSave = {
                    uuid: $scope.encounterRole.uuid,
                    name: $scope.encounterRole.name,
                    description: $scope.encounterRole.description
                }

                var successMessageCode = ($scope.encounterRole.uuid) ? "adminui.savedChanges" : "adminui.saved";
                EncounterRole.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message(successMessageCode));
                })
            }
        }]);