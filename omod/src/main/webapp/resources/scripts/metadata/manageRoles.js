angular.module("manageRoles", [ "roleService", "privilegeService", "ngDialog", "ui.router", "uicommons.filters", "colTable" ])

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
                	roleUuid: null,
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
                 Role.query({ v: "full", includeAll: true }).$promise.then(function(response) {
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
	                    		uuid: role.uuid,
	                    		name: role.name
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
                            inArray = true;
                        }
                    });
                }
                return inArray;
            }
            
            // load inherited privileges
            function loadInheritedPrivileges() {
                $scope.inheritedPrivileges = [];         
                       
                if ($scope.role.allInheritedRoles != null) {
                    
                    $scope.role.inheritedRoles.forEach(function(val, idx) {
                        
                        var inhRole = Role.get({ uuid: val.uuid, v: "full", includeAll: true });
                        
                        if (inhRole.privileges != null) {
                            
                            inhRole.privileges.forEach(function(inp, inpx){
                                
                                    var inpPrivilege = {
                                        uuid: inp.uuid,
                                        name: inp.name
                                    }
                                    $scope.inheritedPrivileges.push(inpPrivilege);
                            });
                        }
                    });
                } 
           }
            
            // load roles with indication of those contained in current role inherited roles
            function loadRole() {
                Role.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.roles = response.results;
                    
                    //remove self from array
                    var idx = $scope.roles.length;
                    while(idx--){
                        if ($scope.roles[idx].uuid === $scope.role.uuid)  { 
                            break;
                        }
                    }
                    $scope.roles.splice(idx, 1);  
                                      
                    $scope.inheritedRoles = Array($scope.roles.length);
                    if ($scope.roles != null) {
                        
                        $scope.dependantRoles = []; 
                         
                        $scope.roles.forEach(function(val, idx) { 
                            
                            $scope.inheritedRoles[idx] = isInArray($scope.role.inheritedRoles, val);
                            
                            // load dependant roles
                            if (!isInArray( $scope.dependantRoles, val)) { // no duplicates
                                if (val.allInheritedRoles != null) {
                                    if (isInArray(val.allInheritedRoles, $scope.role)) {
                                        var depRole = {
                                            uuid: val.uuid,
                                            name: val.name
                                        }
                                        $scope.dependantRoles.push(depRole);                        
                                    }
                                }
                            }
                        });
                    }
                }, function() {
                    emr.errorMessage(emr.message("adminui.role.getRoles.error"));
                })
                loadPrivileges();
                loadInheritedPrivileges();
            }

              // load privileges with indication of those contained in current role      
            function loadPrivileges() {                
                Privilege.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.privileges = response.results;                    
                    $scope.privilegeFlags = Array($scope.privileges.length);
                    if ($scope.privileges != null) {
                        $scope.privileges.forEach(function(val, idx) { 
                            $scope.privilegeFlags[idx] = isInArray($scope.role.privileges, val);                            
                        });
                    }
                }, function() {
                    emr.errorMessage(emr.message("adminui.role.getPrivileges.error"));
                })
            }
            
            $scope.load = function() {
            	loadRole();
            }

            $scope.save = function() {
                // save selected inherited roles
                $scope.role.inheritedRoles = []; // clear list
                var idx = $scope.inheritedRoles.length;
                while(idx--){
                    if ($scope.inheritedRoles[idx])  { // role selected
                        var inheritedRole = {};                         
                        inheritedRole.uuid = $scope.roles[idx].uuid;                         
                        $scope.role.inheritedRoles.push(inheritedRole);  // add role to list 
                    }
                }               
                // save selected privilegess
                $scope.role.privileges = []; 
                idx = $scope.privilegeFlags.length;
                while(idx--){
                    if ($scope.privilegeFlags[idx])  { 
                        var privilege = {};                         
                        privilege.uuid = $scope.privileges[idx].uuid;                         
                        $scope.role.privileges.push(privilege);   
                    }
                }

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
