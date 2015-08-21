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
            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }

            function loadRoles() {
                // TODO standard function for failure of REST call
                Role.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.roles = sortWithRetiredLast(response.results);
                });
            }

            $scope.retire = function(role) {
                // TODO manage roles retire
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: "templates/retireRoleDialog.page",
                    controller: function($scope) {
                        $scope.role = role;
                    }
                }).then(function(role, reason) {
                    var toSave = {
                        uuid: role.uuid,
                        retired: true,
                        reason: reason
                    }
                    // will fail until RESTWS-456
                    Role.save(
                        toSave
                    ).$promise.then(function() {
                        $state.reload(); 
                    });
                });
            }

            $scope.unretire = function(role) {
                // TODO manage roles unretire
                var toSave = {
                    uuid: role.uuid,
                    retired: false
                }
                // will fail until RESTWS-456
                Role.save(
                    toSave
                ).$promise.then(function() {
                    $state.reload(); 
                })
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
                    }).$promise.then(function() {
                        emr.successMessage(emr.message("adminui.role.purge.success"));
                        $state.reload(); 
                     })
                });
            }

            loadRoles();
        }])

    .controller("EditRoleController", [ "$scope", "$state", "Role", "role", "Privilege",
        function($scope, $state, Role, role, Privilege) {
            $scope.role = role;

            function sortWithRetiredLast(list) {
                return _.sortBy(list, "retired");
            }
            
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
            function loadRoles() {
                // TODO standard function for failure of REST call
                Role.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.roles = sortWithRetiredLast(response.results);
                    
                    //remove self from array
                    var idx = $scope.roles.length;
                    while(idx--){
                        if ($scope.roles[idx].uuid === role.uuid)  { 
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
               });               
               loadPrivileges();
               loadInheritedPrivileges();
            }

              // load privileges with indication of those contained in current role      
            function loadPrivileges() {                
                // TODO standard function for failure of REST call
                Privilege.query({ v: "full", includeAll: true }).$promise.then(function(response) {
                    // TODO handle multiple pages of results in a standard way
                    $scope.privileges = sortWithRetiredLast(response.results);                    
                    $scope.privilegeFlags = Array($scope.privileges.length);
                    if ($scope.privileges != null) {
                        $scope.privileges.forEach(function(val, idx) { 
                            $scope.privilegeFlags[idx] = isInArray($scope.role.privileges, val);                            
                        });
                    }
                });
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

               // TODO: utility function for doing this, until RESTWS-460 is fixed
                var toSave = {
                    uuid: $scope.role.uuid,
                    name: $scope.role.name,
                    description: $scope.role.description,
                    inheritedRoles: $scope.role.inheritedRoles,
                    privileges: $scope.role.privileges
                }
                Role.save(toSave).$promise.then(function() {
                    $state.go("list");
                    emr.successMessage(emr.message("adminui.role.purge.success"));
                }, function() {
                    // TODO handle server-side errors
                })
            }

            loadRoles();
    }]);
