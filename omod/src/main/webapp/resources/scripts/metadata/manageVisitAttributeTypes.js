angular.module("manageVisitAttributeTypes", [ "visitAttributeTypeService", "customDatatypeService", "ngDialog", "ui.router", "uicommons.filters", "uicommons.common.error"])

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
                            return VisitAttributeType.get({ uuid: $stateParams.visitAttributeTypeUuid, v: "full" }).$promise;
                        }
                        return {};
                    },
                	customDatatypes: function(CustomDatatypeService) {
                		return CustomDatatypeService.getCustomDatatypes({ v: "full" });
                	}
                },
                controller: "EditVisitAttributeTypeController"
            });
    }])

    .controller("ManageVisitAttributeTypesController", [ "$scope", "$state", "VisitAttributeType", "VisitAttributeTypeService", "ngDialog",
        function($scope, $state, VisitAttributeType, VisitAttributeTypeService, ngDialog) {
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadVisitAttributeTypes() {
                VisitAttributeTypeService.getVisitAttributeTypes({ v: "default", includeAll: true }).then(function(results) {
                    $scope.visitAttributeTypes = sortWithRetiredLast(results);
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
                        emr.successMessage(emr.message("adminui.retired"));
                    });
                });
            }

            $scope.unretire = function(visitAttributeType) {
                VisitAttributeType.save({
                    uuid: visitAttributeType.uuid,
                    retired: false
                }).$promise.then(function() {
                    loadVisitAttributeTypes();
                    emr.successMessage(emr.message("adminui.restored"));
                })
            }
            
            $scope.purge = function(visitAttributeType) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/purgeVisitAttributeTypeDialog.page",
                    controller: function($scope) {
                        $scope.visitAttributeType = visitAttributeType;
                    }
                }).then(function() {
                	VisitAttributeType.delete({
                        uuid: visitAttributeType.uuid,
                        purge: ""
                    }).$promise.then(function() {
                    	loadVisitAttributeTypes();
                        emr.successMessage(emr.message("adminui.purged"));
                    })
                });
            }

            $scope.edit = function(visitAttributeType) {
                $state.go("edit", { visitAttributeTypeUuid: visitAttributeType.uuid });
            }

            loadVisitAttributeTypes();
        }])

    .controller("EditVisitAttributeTypeController", [ "$scope", "$state", "VisitAttributeType", "visitAttributeType", "customDatatypes",
        function($scope, $state, VisitAttributeType, visitAttributeType, customDatatypes) {
			$scope.customDatatypes = customDatatypes;	
			$scope.updateHandlerClassnames = function(reset) {
	        	for (i = 0; i < $scope.customDatatypes.length; i++) {
	        		if ($scope.customDatatypes[i].datatypeClassname == $scope.visitAttributeType.datatypeClassname) {
	        			$scope.handlers = customDatatypes[i].handlers;
	        			
	        			if (reset) {
	        				$scope.visitAttributeType.preferredHandlerClassname = "";
	        			}
	        			break;
	        		}
	        	}
	        }
			
	    	$scope.visitAttributeType = visitAttributeType;
	        if (!$scope.visitAttributeType.minOccurs) {
	        	$scope.visitAttributeType.minOccurs = 0;
	        }
            
            $scope.save = function() {   
            	var toSave = {
	                uuid: $scope.visitAttributeType.uuid,
	                name: $scope.visitAttributeType.name,
	                description: $scope.visitAttributeType.description,
	                minOccurs: $scope.visitAttributeType.minOccurs,
	                maxOccurs: $scope.visitAttributeType.maxOccurs,
	                datatypeClassname: $scope.visitAttributeType.datatypeClassname || null,
	                datatypeConfig: $scope.visitAttributeType.datatypeConfig,
	                preferredHandlerClassname: $scope.visitAttributeType.preferredHandlerClassname || null,
	                handlerConfig: $scope.visitAttributeType.handlerConfig
                };

                var successMessageCode = ($scope.visitAttributeType.uuid) ? "adminui.savedChanges" : "adminui.saved";
                VisitAttributeType.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message(successMessageCode));
                }, function(error) {
                	$scope.error = error.data.error;
                })
            }
            
            
        }]);