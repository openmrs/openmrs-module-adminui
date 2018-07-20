var uuidAndUserMap;
var privilegeLevels;
var capabilities;
var userTabs;
var yesOrNo = {};

$(document).ready(function(){
    userTabs = $('#adminui-users').tabs();
});

function initUserDetails(uuidAndUser, userPrivLevels, userCapabilities, yes, no){
    uuidAndUserMap = uuidAndUser;
    privilegeLevels = userPrivLevels;
    capabilities = userCapabilities;
    yesOrNo[true] = yes;
    yesOrNo[false] = no;
}

angular.module("adminui.userDetails", ["userService", "ngDialog", "adminui.should-match", "uicommons.common.error"])

    .controller("UserDetailsController", ["$scope", "ngDialog" ,"User",
        function($scope, ngDialog, User) {
            //This is use across all apps on teh account form
            $scope.inEditMode = false;
            //This is only used by this app
            $scope.editing = false;
            $scope.requesting = false;
            $scope.uuidUserMap = uuidAndUserMap;
            $scope.originalState = angular.copy($scope.uuidUserMap);
            $scope.privilegeLevels = privilegeLevels;
            $scope.capabilities = capabilities;
            $scope.yesOrNo = yesOrNo;

            $scope.printCapabilities = function(map){
                var toReturn = [];
                jq.each(map, function(key, value){
                    if(value == true){
                        toReturn.push($scope.capabilities[key]);
                    }
                });
                return toReturn.join(", ");
            }

            $scope.getTabLabel = function(userUuid){
                var u = $scope.uuidUserMap[userUuid];
                var label = "";
                if(u){
                    if(u.username && jq.trim(u.username) != '') {
                        label = u.username;
                    }
                    else {
                        label = u.systemId;
                    }
                }
                return label;
            }

            $scope.beforeRequest = function(){
                $scope.requesting = true;
            }

            $scope.afterRequest = function(){
                $scope.requesting = false;
            }

            $scope.toggleOtherActions = function(value){
                $scope.editing = value;
                //Disable the edit,remove,add buttons in the other apps
                var personScope = angular.element("#adminui-person-details").scope();
                personScope.$apply(function(){
                    personScope.inEditMode = value;
                });
                var providerScope = angular.element("#adminui-provider-details").scope();
                providerScope.$apply(function(){
                    providerScope.inEditMode = value;
                });
            }

            $scope.disableActionsAndOtherTabs = function(uuid){
                $scope.toggleOtherActions(true);
                jq('.add-action, .edit-action, .delete-action').addClass('invisible');
                userTabs.tabs("disable");
                userTabs.tabs("enable", "#"+uuid);
            }

            $scope.enableActionsAndOtherTabs = function(){
                jq('.add-action, .edit-action, .delete-action').removeClass('invisible');
                userTabs.tabs("enable");
                $scope.toggleOtherActions(false);
            }

            $scope.add = function(uuid){
                $scope.toggleOtherActions(true);
                jq('.add-action, .edit-action, .delete-action').addClass('invisible');
                //We are adding the first user account
                if(getMapSize($scope.uuidUserMap) == 1){
                    jq('.adminui-first-user-ele').toggle();
                } else {
                    userTabs.tabs("disable");
                    userTabs.tabs("enable", "#"+uuid);
                }
            }

            $scope.edit = function(uuid){
                $scope.disableActionsAndOtherTabs(uuid);
                jq('.user-'+uuid).toggle();
            }

            $scope.cancel = function(userUuid, isNew){
                //reset the form values
                $scope.uuidUserMap[userUuid] = angular.copy($scope.originalState[userUuid]);
                $scope.userDetailsForm.$setPristine();
                $scope.userDetailsForm.$setUntouched();
                jq('.add-action, .edit-action, .delete-action').removeClass('invisible');
                if(getMapSize($scope.uuidUserMap) == 1){
                    jq('.adminui-first-user-ele').toggle();
                } else {
                    userTabs.tabs("enable");
                    if (isNew) {
                        userTabs.tabs("option", "active", 0);
                    } else {
                        jq('.user-' + userUuid).toggle();
                    }
                }
                $scope.toggleOtherActions(false);
            }

            $scope.save = function(userUuid, personUuid){
                $scope.beforeRequest();
                var modelUser = $scope.uuidUserMap[userUuid];
                var privilegesLevelAndCapabilities = [modelUser.privilegeLevel];
                jq.each(modelUser.capabilities, function(key, value){
                    if(value == true){
                        privilegesLevelAndCapabilities.push(key);
                    }
                });
                var uProperties = {};
                if(modelUser.userProperties.forcePassword){
                    uProperties.forcePassword = "true";
                }
                angular.forEach(modelUser.userProperties, function(value, key) {
                    if(key != "forcePassword") {
                        var domElement = document.getElementById(key + userUuid);
                        if (domElement) {
                            uProperties[key] = domElement.value;
                        }
                    }
                });
                
                var toSave = {
                    username: modelUser.username,
                    roles: privilegesLevelAndCapabilities,
                    userProperties: uProperties
                };
                if(modelUser.password && jq.trim(modelUser.password) != ''){
                    toSave['password'] = modelUser.password;
                }

                var successMessageKey = "savedChanges";
                if(personUuid){
                    toSave['person'] = personUuid;
                    successMessageKey = "saved";
                } else{
                    toSave['uuid'] = userUuid;
                }

                User.save(toSave).$promise.then(function () {
                    emr.successMessage(messages[successMessageKey]);
                    if(personUuid){
                        emr.navigateTo({
                            provider:"adminui",
                            page: "systemadmin/accounts/account",
                            query: {personId: personUuid}
                        });
                    } else{
                        //update cache
                        $scope.originalState[userUuid] = angular.copy($scope.uuidUserMap[userUuid]);
                        jq('.user-'+userUuid).toggle();
                        $scope.enableActionsAndOtherTabs();
                        //notify the audit info app so that it updates the audit info
                        angular.element('#account-audit-info').scope().$broadcast('event.auditInfo.changed');
                        $scope.afterRequest();
                    }
                },
                function () {
                    $scope.afterRequest();
                });
            }

            $scope.retire = function(userUuid) {
                var user = $scope.uuidUserMap[userUuid];
                var userLabel = $scope.getTabLabel(userUuid);
                ngDialog.openConfirm({
                    showClose: false,
                    closeByEscape: true,
                    closeByDocument: true,
                    template: 'retireUserTemplate',
                    controller: function($scope) {
                        $scope.display = userLabel;

                    }
                }).then(function(reason) {
                    $scope.beforeRequest();
                    $scope.disableActionsAndOtherTabs(userUuid);
                    User.delete({
                        uuid: userUuid,
                        reason: reason
                    }).$promise.then(function() {
                        emr.successMessage(messages["retired"]);
                        //Update the cache with new state
                        user.retired = true;
                        $scope.originalState[userUuid] = angular.copy(user);
                        angular.element('#account-audit-info').scope().$broadcast('event.auditInfo.changed');
                    }).finally(function(){
                        $scope.afterRequest();
                        $scope.enableActionsAndOtherTabs();
                    });
                });
            }

            $scope.restore = function(userUuid) {
                $scope.beforeRequest();
                $scope.disableActionsAndOtherTabs(userUuid);
                User.save({
                    uuid: userUuid,
                    retired: false
                }).$promise.then(function() {
                    emr.successMessage(messages["restored"]);
                    var user = $scope.uuidUserMap[userUuid];
                    user.retired = false;
                    $scope.originalState[userUuid] = angular.copy(user);
                    angular.element('#account-audit-info').scope().$broadcast('event.auditInfo.changed');
                }).finally(function(){
                    $scope.afterRequest();
                    $scope.enableActionsAndOtherTabs();
                });
            }
        }
    ]);