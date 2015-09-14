angular.module("manageRoles", [ "roleService", "privilegeService", "ngDialog", "ui.router", "uicommons.filters" ])

    .config([ "$stateProvider", "$urlRouterProvider", function($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list");

        $stateProvider
            .state('list', {
                url: "/list",
                templateUrl: "templates/list.page",
                controller: "ManageRolesController"
            })
            .state("edit", {
                url: "/edit/:roleUuid",
                templateUrl: "templates/edit.page",
                params: {
                	roleUuid: null
                },
                resolve: {
                    role: function($stateParams, Role) {
                        if ($stateParams.roleUuid) {
                            return Role.get({ uuid: $stateParams.roleUuid, v: "full", includeAll: true });
                        }
                        return {};
                    }
                },
                controller: "EditRoleController"
            });
    }])

    .controller("ManageRolesController", [ "$scope", "Role", "ngDialog", 
        function($scope, Role, ngDialog) {
    		
            function loadRoles() {
                Role.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    $scope.roles = response.results;
                }, function() {
                    emr.errorMessage(emr.message("adminui.role.purge.success"));
                })
            }
            
            $scope.load = function() {
               	loadRoles();
            }

            $scope.edit = function(role) {
                $state.go("edit", { roleUuid: role.uuid });
            }

            $scope.purge = function(role) {
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/purgeRoleDialog.page",
                    controller: function($scope) {
                        $scope.role = role;
                    }
                	}).then(function() {
                    Role.delete({
                		uuid: role.uuid,
                        purge: ""
                    })
                    .$promise.then(function() {                        
                        emr.successMessage(emr.message("adminui.role.purge.success"));
                        loadRoles();
                    });
                });
            }
        }])

    .controller("EditRoleController", [ "$scope", "$state", "Role", "role", "Privilege",
        function($scope, $state, Role, role, Privilege) {

            $scope.role = role;
            
            // test whether an object is contained within an array
            function isInArray(myArray, myValue)
            {
            	var inArray = false;
                if (myArray != null) {
                    myArray.forEach(function (val, idx){
                        if (myArray[idx].uuid === myValue.uuid) {
                        	inArray =  true;
                        }
                    });
                }
                return inArray;
            }
            
            // load roles with indication of those contained in current role inherited roles
            function loadRole() {
                Role.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.roles = response.results;
                    
                    // load dependant roles
                	$scope.dependantRoles = []; 
                    if ($scope.roles != null) {                                         
                    	$scope.roles.forEach(function(val, idx) { 
    	                    if (!isInArray( $scope.dependantRoles, val)) { // no duplicates
    	                        if (val.allInheritedRoles != null) {
    	                            if (isInArray(val.allInheritedRoles, $scope.role)) {
    	                                $scope.dependantRoles.push({
    	                                    uuid: val.uuid,
    	                                    name: val.name
    	                                });                        
    	                            }
    	                        }
    	                    }
                        });   
                    }
                    
                    // remove self and bases from inheritable roles
                    var idx = $scope.roles.length - 1;
                    while(idx--){
                        if ($scope.roles[idx].uuid === $scope.role.uuid 
                        		|| isInArray($scope.dependantRoles, $scope.roles[idx]))  { 
                            $scope.roles.splice(idx, 1);
                        }
                    }
                    
                    $scope.inheritedRoles = Array($scope.roles.length);
                    loadInheritedRoles();  

                    // load privileges with indication of those contained in current role      
                    Privilege.query({ v: "default", includeAll: true }).$promise.then(function(response) {
                        // TODO handle multiple pages of results in a standard way
                        $scope.privileges = response.results;          
                        $scope.inheritedPrivilegeFlags = Array($scope.privileges.length);
                        $scope.privilegeFlags = Array($scope.privileges.length);
                                                
                        loadInheritedPrivileges();

                    });
                });
            }
 
            function loadPrivilegeFlags() {
                if ($scope.privileges != null) {
                    $scope.privileges.forEach(function(val, idx) { 
                        $scope.inheritedPrivilegeFlags[idx] = false; 
                        $scope.privilegeFlags[idx] = false;
                    	if (isInArray($scope.inheritedPrivileges, val)) {
                            $scope.inheritedPrivilegeFlags[idx] = true; // ok, because inheritedPrivileges take precendence
                            $scope.privilegeFlags[idx] = true;
                    	}
                    	else if (isInArray($scope.role.privileges, val)) {
                            $scope.privilegeFlags[idx] = true;
                    	}
                    });
                }
            }
            
            // load inherited privileges
            function loadInheritedPrivileges() {
                $scope.inheritedPrivileges = [];                         
                if ($scope.role.inheritedRoles != null) {                    
                	$scope.role.inheritedRoles.forEach(function(val, idx) { 
                        $scope.roles.forEach(function(valr, valx){
                            if (val.uuid === $scope.roles[valx].uuid) {
                            	$scope.roles[valx].privileges.forEach(function(inp, inpx){                                
                                    if (!isInArray($scope.inheritedPrivileges, inp)) { // no duplicates
                                        $scope.inheritedPrivileges.push({
                                            uuid: inp.uuid
                                        });
                                    }
                                });
                            }
                        });
                    });
                } 
                loadPrivilegeFlags();
            }

            function loadInheritedRoles() {                
                if ($scope.roles != null) {                                         
                	$scope.roles.forEach(function(val, idx) { 
	                    $scope.inheritedRoles[idx] = isInArray($scope.role.allInheritedRoles, val);	                    
                    });   
                }
            }
                
            function updateInheritedRoles() {
                // save selected inherited roles
                $scope.role.inheritedRoles = []; // clear list
                $scope.roles.forEach(function (val, idx){
                    if ($scope.inheritedRoles[idx])  { // inherited role selected
                        $scope.role.inheritedRoles.push({  // add role to list 
                                uuid: $scope.roles[idx].uuid                         
                        });
                    }
                });               
            }
            
            function updatePrivileges() {
                // save selected privileges
                $scope.role.privileges = [];  // clear list
                $scope.privileges.forEach(function (val, idx){
                	// privilege which is not inherited selected
                    if ($scope.privilegeFlags[idx] && !$scope.inheritedPrivilegeFlags[idx])  { 
                        $scope.role.privileges.push({
                                uuid: $scope.privileges[idx].uuid                         
                        });   
                    }
                });
            }
            
            $scope.load = function() {
            	loadRole();
            }

            $scope.save = function() {
            	updateInheritedRoles();
            	updatePrivileges();

                Role.save({
                    uuid: $scope.role.uuid,
                    name: $scope.role.name,
                    description: $scope.role.description,
                    inheritedRoles: $scope.role.inheritedRoles,
                    privileges: $scope.role.privileges
                }).$promise.then(function() {
                    emr.successMessage(emr.message("adminui.role.save.success"));
                    $state.go("list");
                });
            }
    }]);
