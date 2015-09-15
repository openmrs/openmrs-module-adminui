angular.module("managePatientIdentifierTypes", [ "patientIdentifierTypeService", "ngDialog", "ui.router", "uicommons.filters", "uicommons.common.error"])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManagePatientIdentifierTypesController"
            })
            .state("edit", {
                url: "/edit/:patientIdentifierTypeUuid",
                templateUrl: "templates/edit.page",
                params: {
                	patientIdentifierTypeUuid: null,
                },
                resolve: {
                	patientIdentifierType: function($stateParams, PatientIdentifierType) {
                        if ($stateParams.patientIdentifierTypeUuid) {
                            return PatientIdentifierType.get({ uuid: $stateParams.patientIdentifierTypeUuid, v: "full" });
                        }
                        return {};
                    }
                },
                controller: "EditPatientIdentifierTypeController"
            });
    }])

    .controller("ManagePatientIdentifierTypesController", [ "$scope", "$state", "PatientIdentifierType", "PatientIdentifierTypeService", "ngDialog",
        function($scope, $state, PatientIdentifierType, PatientIdentifierTypeService, ngDialog) {
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadPatientIdentifierTypes() {
                // TODO standard function for failure of REST call
            	PatientIdentifierTypeService.getPatientIdentifierTypes({ v: "default", includeAll: true }).then(function(results) {
                    $scope.patientIdentifierTypes = sortWithRetiredLast(results);
                });
            }

            $scope.retire = function(patientIdentifierType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/retirePatientIdentifierTypeDialog.page",
                    controller: function($scope) {
                        $scope.patientIdentifierType = patientIdentifierType;
                    }
                }).then(function(reason) {
                	PatientIdentifierType.delete({
                        uuid: patientIdentifierType.uuid,
                        reason: reason
                    }).$promise.then(function() {
                        loadPatientIdentifierTypes();
                            emr.successMessage(emr.message("adminui.retired"));
                    });
                });
            }

            $scope.unretire = function(patientIdentifierType) {
            	PatientIdentifierType.save({
                    uuid: patientIdentifierType.uuid,
                    retired: false
                }).$promise.then(function() {
                    loadPatientIdentifierTypes();
                        emr.successMessage(emr.message("adminui.restored"));
                })
            }

            $scope.edit = function(patientIdentifierType) {
                $state.go("edit", { patientIdentifierTypeUuid: patientIdentifierType.uuid });
            },

            $scope.purge = function(patientIdentifierType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/purgePatientIdentifierTypeDialog.page",
                    controller: function($scope) {
                        $scope.patientIdentifierType = patientIdentifierType;
                    }
                }).then(function() {
                	PatientIdentifierType.delete({
                        uuid: patientIdentifierType.uuid,
                        purge: ""
                    }).$promise.then(function() {
                        loadPatientIdentifierTypes();
                        emr.successMessage(emr.message("adminui.purged"));
                    })
                });
            }

            loadPatientIdentifierTypes();
        }])

    .controller("EditPatientIdentifierTypeController", [ "$scope", "$state", "PatientIdentifierType", "patientIdentifierType",
        function($scope, $state, PatientIdentifierType, patientIdentifierType) {
            $scope.patientIdentifierType = patientIdentifierType;

            $scope.save = function() {
                // TODO: utility function for doing this, until RESTWS-460 is fixed
                var toSave = {
                    uuid: $scope.patientIdentifierType.uuid,
                    name: $scope.patientIdentifierType.name,
                    description: $scope.patientIdentifierType.description,
                    format: $scope.patientIdentifierType.format,
                    formatDescription: $scope.patientIdentifierType.formatDescription,
                    required: $scope.patientIdentifierType.required,
                    locationBehavior: $scope.patientIdentifierType.locationBehavior ? $scope.patientIdentifierType.locationBehavior : null,
                    uniquenessBehavior: $scope.patientIdentifierType.uniquenessBehavior ? $scope.patientIdentifierType.uniquenessBehavior : null,
                    validator: $scope.patientIdentifierType.validator
                }

                var successMessageCode = ($scope.patientIdentifierType.uuid) ? "adminui.savedChanges" : "adminui.saved";
                PatientIdentifierType.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message(successMessageCode));
                })
            }
        }]);