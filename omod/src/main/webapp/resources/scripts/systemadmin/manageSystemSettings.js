angular.module("manageSystemSettings", [ "systemSettingService", "ngDialog", "ui.router", "uicommons.filters", "uicommons.common.error"])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManageSystemSettingsController"
            })
            .state("edit", {
                url: "/edit/:systemSettingUuid",
                templateUrl: "templates/edit.page",
                params: {
                	systemSettingUuid: null,
                },
                resolve: {
                	systemSetting: function($stateParams, SystemSetting) {
                        if ($stateParams.systemSettingUuid) {
                            return SystemSetting.get({ uuid: $stateParams.systemSettingUuid, v: "full" });
                        }
                        return {};
                    }
                },
                controller: "EditSystemSettingController"
            });
    }])

    .controller("ManageSystemSettingsController", [ "$scope", "$state", "SystemSetting", "ngDialog", "SystemSettingService", 
        function($scope, $state, SystemSetting, ngDialog, SystemSettingService) {
    	
            function loadSystemSettings() {
            	SystemSettingService.getSystemSettings({ v: "default", includeAll: true }).then(function (result) {
            		$scope.systemSettings = result;
            	});
            }

            $scope.edit = function(systemSetting) {
                $state.go("edit", { systemSettingUuid: systemSetting.uuid });
            }
            
            $scope.purge = function(systemSetting) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/purgeSystemSettingDialog.page",
                    controller: function($scope) {
                        $scope.systemSetting = systemSetting;
                    }
                }).then(function() {
                	SystemSetting.delete({
                        uuid: systemSetting.uuid,
                        purge: ""
                    }).$promise.then(function() {
                        loadSystemSettings();
                        emr.successMessage(emr.message("adminui.purged"));
                    })
                });
            }

            loadSystemSettings();
        }])

    .controller("EditSystemSettingController", [ "$scope", "$state", "SystemSetting", "systemSetting",
        function($scope, $state, SystemSetting, systemSetting) {
            $scope.systemSetting = systemSetting;

            $scope.save = function() {
                // TODO: utility function for doing this, until RESTWS-460 is fixed
                var toSave = {
                    uuid: $scope.systemSetting.uuid,
                    property: $scope.systemSetting.property,
                    description: $scope.systemSetting.description,
                    value: $scope.systemSetting.value
                }

                var successMessageCode = ($scope.systemSetting.uuid) ? "adminui.savedChanges" : "adminui.saved";
                SystemSetting.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message(successMessageCode));
                })
            }
        }]);