
var personAttributesMap;

function initPersonDetails(personAttributes){
    personAttributesMap = personAttributes;
}

angular.module("adminui.personDetails", ["personService"])

    .controller("EditPersonDetailsController", ["$scope", "Person",
        function($scope, Person) {
            $scope.inEditMode = false;
            $scope.requesting = false;
            $scope.person = {};

            $scope.init = function(personUuid, gender, personNameUuid, familyName, givenName, male, female){
                $scope.person.personUuid = personUuid;
                $scope.person.gender = gender;
                $scope.person.personNameUuid = personNameUuid;
                $scope.person.familyName = familyName;
                $scope.person.givenName = givenName;
                $scope.genders = {M: male, F: female};
                $scope.personAttributesMap = personAttributesMap;

                //cache the original state so that we can use it to reset later on cancel
                $scope.originalState = angular.copy($scope.person);
            }

            $scope.beforeRequest = function(){
                $scope.requesting = true;
            }

            $scope.afterRequest = function(){
                $scope.requesting = false;
            }

            $scope.toggleOtherActions = function(value){
                //Disable the edit,remove,add buttons in the other apps
                var userScope = angular.element("#adminui-user-details").scope();
                userScope.$apply(function(){
                    userScope.inEditMode = value;
                });
                var providerScope = angular.element("#adminui-provider-details").scope();
                providerScope.$apply(function(){
                    providerScope.inEditMode = value;
                });
            }

            $scope.edit = function(){
                $scope.toggleOtherActions(true);
                jq('.adminui-account-person-details').toggle();
            }

            $scope.save = function() {
                $scope.beforeRequest();
                attributesSet = []
                angular.forEach($scope.personAttributesMap , function(value, key) {
                    personAttributeUuid = value.personAttributeUuid;
                    formFieldName = value.formFieldName;
                    currentValue = document.getElementById(formFieldName).value;
                    if (currentValue) {
                        attributesSet.push({
                            uuid : personAttributeUuid,
                            value : currentValue
                        })
                    }
                });

                var personToSave = {
                    uuid: $scope.person.personUuid,
                    gender: $scope.person.gender,
                    names: [{
                        uuid: $scope.person.personNameUuid,
                        familyName: $scope.person.familyName,
                        givenName: $scope.person.givenName
                    }],
                    attributes: attributesSet
                }

                Person.save(personToSave).$promise.then(function () {
                    //Update the cache with new state
                    $scope.originalState = angular.copy($scope.person);
                    $scope.toggleOtherActions(false);
                    //notify the audit info app so that it updates the audit info
                    angular.element('#account-audit-info').scope().$broadcast('event.auditInfo.changed');
                    jq('.adminui-account-person-details').toggle();
                    emr.successMessage(messages["savedChanges"]);
                }).finally(function(){
                    $scope.afterRequest();
                })
            }

            $scope.cancel = function(){
                //reset the form values
                $scope.person = angular.copy($scope.originalState);
                $scope.personDetailsForm.$setPristine();
                $scope.personDetailsForm.$setUntouched();
                jq('.adminui-account-person-details').toggle();
                $scope.toggleOtherActions(false);
            }
        }
    ]);
