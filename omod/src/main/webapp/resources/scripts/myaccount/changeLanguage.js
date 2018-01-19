angular.module("changeLanguage",[])
    .controller("changeLanguageController", function($scope) {
	      emr.loadMessages("adminui.account.primaryLang.desc",function() {
		      jq("#default-locale > label,select").attr({ title:emr.message("adminui.account.primaryLang.desc") });
	      });
	   
	      jq("select").on('change', function() {
	           $scope.value = this.value;
		       if ($scope.value.indexOf("_") >- 1) {
		    	   $scope.value = value.slice(0, value.indexOf("_"));
		        }
	           getCheckBoxValues();
	      });
	    
	      function getCheckBoxValues() {
	               jq("[name='proficientLocales']").each(function() { 
	   		          if (this.value == $scope.value) {
	   		              unCheckInitialValue();
	   		              jq(this).prop('checked',true);
	   	              }
	   	           });
	      }
	      
	      function unCheckInitialValue() {
	              jq("[name='proficientLocales']").each(function() {
	    	         if (jq(this).prop('checked') == true) {
	    	            jq(this).prop('checked',false);
	                 }
	              });
	      }
    });
