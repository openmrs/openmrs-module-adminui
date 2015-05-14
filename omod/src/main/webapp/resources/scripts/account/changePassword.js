var passwordMinLength;

function MinimumLengthFieldValidator() {
    this.messageIdentifier = "minLength";
}

//FieldValidator is in uicommon module's validators.js
MinimumLengthFieldValidator.prototype = new FieldValidator();
MinimumLengthFieldValidator.prototype.constructor = MinimumLengthFieldValidator;
MinimumLengthFieldValidator.prototype.validate = function(field) {

    var hasValue = field.value() && field.value().length > 0;
    //The required field validator already took care of requiring the field
    if(hasValue && field.value().length < passwordMinLength){
        //emrMessages is in uicommons's validationMessages.gsp
        return emrMessages[this.messageIdentifier];
    }

    return null;
}

function MatchedInputFieldValidator() {
    this.messageIdentifier = "matchedInput";
}

MatchedInputFieldValidator.prototype = new FieldValidator();
MatchedInputFieldValidator.prototype.constructor = MatchedInputFieldValidator;
MatchedInputFieldValidator.prototype.validate = function(field) {


    var newPassword = jQuery("#newPassword").val();
    var matchPassword = jQuery("confirmPassword").val();

    if(newPassword != matchPassword){
        return emrMessages[this.messageIdentifier];
    }


   /* var otherField = jQuery('#'+field.element.attr('matched-field-id'));
    var hasValue = field.value() && field.value().length > 0;
    var otherFieldHasValue = otherField.val() && otherField.val().length > 0;
    if(hasValue && otherFieldHasValue && field.value() != otherField.val()){
        return emrMessages[this.messageIdentifier];
    }*/

    return null;
}


//Register teh valitors, Validators is defined in uicommons module's validators.js
Validators["min-length"] = new MinimumLengthFieldValidator();
Validators["matched-input"] = new MatchedInputFieldValidator();