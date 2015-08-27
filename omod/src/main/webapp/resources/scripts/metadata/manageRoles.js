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

    .controller("ManageRolesController", [ "$scope", "$state", "Role", "ngDialog", 
        function($scope, $state, Role, ngDialog) {
    		
            function loadRoles() {
                 Role.query({ v: "default", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
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
	                    var toDelete = {
	                    		uuid: role.uuid
	                    }
                    Role.delete(toDelete)
                    .$promise.then(function() {
                        emr.successMessage(emr.message("adminui.role.purge.success"));
                        $state.reload(); 
                    }, function() {
                        emr.errorMessage(emr.message("adminui.role.purge.error"));
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
                    myArray.map(function(arrayObj){
                        if (arrayObj.uuid === myValue.uuid) {
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
                    
                    $scope.inheritedRoles = Array($scope.roles.length);
                    loadInheritedRoles(true);  
                    
                    // remove self and bases from inheritable roles
                    idx = $scope.roles.length;
                    while(idx--){
                        if ($scope.roles[idx].uuid === $scope.role.uuid 
                        		|| isInArray($scope.dependantRoles, $scope.roles[idx]))  { 
                            $scope.roles.splice(idx, 1);
                        }
                    }

                    // load privileges with indication of those contained in current role      
                    Privilege.query({ v: "default", includeAll: true }).$promise.then(function(response) {
                        // TODO handle multiple pages of results in a standard way
                        $scope.privileges = response.results;          
                        $scope.inheritedPrivilegeFlags = Array($scope.privileges.length);
                        $scope.privilegeFlags = Array($scope.privileges.length);
                                                
                        loadInheritedPrivileges();

                    }, function() {
                        emr.errorMessage(emr.message("adminui.role.getPrivileges.error"));
                    });
                    
                }, function() {
                    emr.errorMessage(emr.message("adminui.role.getRoles.error"));
                })
            }
 
            function loadPrivilegeFlags() {
                if ($scope.privileges != null) {
                    $scope.privileges.forEach(function(val, idx) { 
                        $scope.inheritedPrivilegeFlags[idx] = false; 
                        $scope.privilegeFlags[idx] = false;
                    	if (isInArray($scope.inheritedPrivileges, val)) {
                    		// ok, because inheritedPrivileges take precendence
                            $scope.inheritedPrivilegeFlags[idx] = true; 
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
            			$scope.roles.map(function(arrayObj){
                            if (arrayObj.uuid === val.uuid) {
                            	arrayObj.privileges.forEach(function(inp, inpx){                                
                                    if (!isInArray($scope.inheritedPrivileges, inp)) { // no duplicates
                                        var inpPrivilege = {
                                            uuid: inp.uuid
                                        }
                                        $scope.inheritedPrivileges.push(inpPrivilege);
                                    }
                                });
                            }
                        });
                    });
                } 
                loadPrivilegeFlags();
            }

            function loadInheritedRoles(loadDependants) {
                if (loadDependants)
                	$scope.dependantRoles = []; 
                
                if ($scope.roles != null) {                                         
                	$scope.roles.forEach(function(val, idx) { 
	                    $scope.inheritedRoles[idx] = isInArray($scope.role.inheritedRoles, val);	                    
	                    if (loadDependants) {
	                        // load dependant roles
	                        if (!isInArray( $scope.dependantRoles, val)) { // no duplicates
	                            if (val.inheritedRoles != null) {
	                                if (isInArray(val.inheritedRoles, $scope.role)) {
	                                    var depRole = {
	                                        uuid: val.uuid,
	                                        name: val.name
	                                    }
	                                    $scope.dependantRoles.push(depRole);                        
	                                }
	                            }
	                        }
	                    }
                    });   
                }
            }
                
            function updateInheritedRoles() {
                // save selected inherited roles
                $scope.role.inheritedRoles = []; // clear list
                var idx = $scope.inheritedRoles.length;
                while(idx--){
                    if ($scope.inheritedRoles[idx])  { // inherited role selected
                        var inheritedRole = {};                         
                        inheritedRole.uuid = $scope.roles[idx].uuid;                         
                        $scope.role.inheritedRoles.push(inheritedRole);  // add role to list 
                    }
                }               
            }
            
            function updatePrivileges() {
                // save selected privileges
                $scope.role.privileges = [];  // clear list
                idx = $scope.privilegeFlags.length;
                while(idx--){
                	// privilege which is not inherited selected
                    if ($scope.privilegeFlags[idx] && !$scope.inheritedPrivilegeFlags[idx])  { 
                        var privilege = {};                         
                        privilege.uuid = $scope.privileges[idx].uuid;                         
                        $scope.role.privileges.push(privilege);   
                    }
                }
            }
            
            $scope.load = function() {
            	loadRole();
            }
            
            // update inherited privileges when list of inherited roles changes
            $scope.selectInheritedRole = function() {
            	updateInheritedRoles();
                loadInheritedPrivileges();
            }

            $scope.save = function() {
            	updateInheritedRoles();
            	updatePrivileges();

                var toSave = {
                    uuid: $scope.role.uuid,
                    name: $scope.role.name,
                    description: $scope.role.description,
                    inheritedRoles: $scope.role.inheritedRoles,
                    privileges: $scope.role.privileges
                }
                Role.save(toSave).$promise.then(function() {
                    emr.successMessage(emr.message("adminui.role.save.success"));
                    $state.go("list");
                }, function() {
                    emr.errorMessage(emr.message("adminui.role.save.error"));
                })
            }
    }]);
