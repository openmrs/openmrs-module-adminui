package org.openmrs.module.adminui.account;

import org.apache.commons.lang.StringUtils;
import org.openmrs.User;
import org.openmrs.annotation.Handler;
import org.openmrs.api.PasswordException;
import org.openmrs.api.UserService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = {AccountDomainWrapper.class}, order = 50)
public class AccountFormValidator implements Validator {

    @Autowired
    @Qualifier("messageSourceService")
    private MessageSourceService messageSourceService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("providerManagementService")
    private ProviderManagementService providerManagementService;

    public static final String USERNAME_MIN_LENGTH = "2";
    public static final String USERNAME_MAX_LENGTH = "50";
    public boolean ProviderEnabled = false;

    /**
     * @param messageSourceService the messageSourceService to set
     */
    public void setMessageSourceService(MessageSourceService messageSourceService) {
        this.messageSourceService = messageSourceService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setProviderManagementService(ProviderManagementService providerManagementService) {
        this.providerManagementService = providerManagementService;
    }

    /**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return AccountDomainWrapper.class.isAssignableFrom(clazz);
    }
    
    /**
     * @should reject an empty family name
     * @should reject an empty given name
     * @should reject an empty gender
     * @should reject if none of the checkbox (user or provider) ticked
     * @should reject empty username if user enabled
     * @should reject empty password/confirm password if user enabled
     * @should require password if confirm password is provided
     * @should require confirm password if password is provided
     * @should reject an empty providerRole if provider enabled
     *  @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
	**/
    
    @Override
    public void validate(Object obj, Errors errors) { /*
        if (obj == null || !(obj instanceof AccountDomainWrapper))
            throw new IllegalArgumentException("The parameter obj should not be null and must be of type" + AccountDomainWrapper.class);

        AccountDomainWrapper account = (AccountDomainWrapper) obj;

        User user = account.getUser();

        checkIfGivenAndFamilyNameAreNotNull(errors, account);
        checkIfGenderIsNull(errors, account);
        checkIfUserAndProviderAreNull(errors, account);
        
        if(account.getUserEnabled()==true) {
        	if (account.getUser() != null) {
        		checkIfUserNameIsCorrect(errors, account.getUsername());
        		checkIfDuplicateUsername(errors, account.getUser());
        		checkIfPrivilegeLevelIsCorrect(errors, account);
        		//checkIfNoCapabilities(errors, account);
        	}

        	if (checkIfUserWasCreated(user) || StringUtils.isNotBlank(account.getPassword()) || StringUtils.isNotBlank(account.getConfirmPassword())) {
        		checkIfPasswordIsCorrect(errors, account);
        	}
        }
        
        if(ProviderEnabled==true) {
    		checkIfProviderRolesAreNull(errors, account);
    	}
    	
    	*/
    }
    
    /*
    
    private void checkIfGivenAndFamilyNameAreNotNull(Errors errors, AccountDomainWrapper account) {
        if (StringUtils.isBlank(account.getGivenName())) {
            errors.rejectValue("givenName", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.person.givenName")}, null);
        }
        if (StringUtils.isBlank(account.getFamilyName())) {
            errors.rejectValue("familyName", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.person.familyName")}, null);
        }
    }
    
    private void checkIfGenderIsNull(Errors errors, AccountDomainWrapper account) {
        if (StringUtils.isBlank(account.getGender())) {
            errors.rejectValue("gender", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.gender")}, null);
        }
    }
    
    private void checkIfUserAndProviderAreNull(Errors errors, AccountDomainWrapper account) {
    	if (account.getUserEnabled()==false && account.getProviderEnabled()==false) {
            errors.rejectValue("userEnabled", "error.required",
                    new Object[]{messageSourceService.getMessage("Both User and Provider can't be null")}, null);
    	}
    }
    
    private void checkIfUserNameIsCorrect(Errors errors, String username) {
        if (StringUtils.isNotBlank(username)) {
            if (!username.matches("[A-Za-z0-9\\._\\-]{" + USERNAME_MIN_LENGTH + "," + USERNAME_MAX_LENGTH + "}")) {
                errors.rejectValue("username", "adminui.user.username.error",
                        new Object[]{messageSourceService.getMessage("adminui.user.username.error")}, null);
            }

        } else {
            errors.rejectValue("username", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.user.username")}, null);
        }
    }
    
    private void checkIfDuplicateUsername(Errors errors, User user) {
        if (userService.hasDuplicateUsername(user)) {
            errors.rejectValue("username", "adminui.user.duplicateUsername",
                    new Object[]{messageSourceService.getMessage("adminui.user.duplicateUsername")}, null);
        }
    }
    
    private void checkIfPrivilegeLevelIsCorrect(Errors errors, AccountDomainWrapper account) {
        if (account.getPrivilegeLevel() == null) {
            errors.rejectValue("privilegeLevel", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.user.privilegeLevel")}, null);
        }
    }
    
    private void checkIfNoCapabilities(Errors errors, AccountDomainWrapper account) {
        if (account.getCapabilities() == null || account.getCapabilities().size() == 0) {
            errors.rejectValue("capabilities", "adminui.user.Capabilities.required",
                    new Object[]{messageSourceService.getMessage("adminui.user.Capabilities.required")}, null);
        }
    }
    
    private boolean checkIfUserWasCreated(User user) {
        return (user != null && user.getUserId() == null);
    }
    
    private void checkIfPasswordIsCorrect(Errors errors, AccountDomainWrapper account) {
        String password = account.getPassword();
        String confirmPassword = account.getConfirmPassword();

        if (checkIfPasswordWasCreated(password, confirmPassword)) {
            validatePassword(errors, account, password, confirmPassword);
        } else {
            errors.rejectValue("password", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.user.password")}, null);
            errors.rejectValue("confirmPassword", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.user.confirmPassword")}, null);
        }

    }

    private void validatePassword(Errors errors, AccountDomainWrapper account, String password, String confirmPassword) {
        if (password.equals(confirmPassword)) {
            getErrorInPassword(errors, account);
        } else {
            errors.rejectValue("password", "adminui.account.error.passwordDontMatch",
                    new Object[]{messageSourceService.getMessage("adminui.user.password")}, null);
        }
    }

    private void getErrorInPassword(Errors errors, AccountDomainWrapper account) {
        try {
            OpenmrsUtil.validatePassword(account.getUsername(), account.getPassword(), account.getUser().getSystemId());
        } catch (PasswordException e) {
            errors.rejectValue("password", "adminui.account.error.passwordError",
                    new Object[]{messageSourceService.getMessage("adminui.account.error.passwordError")}, null);
        }
    }

    private boolean checkIfPasswordWasCreated(String password, String confirmPassword) {
        return (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword));
    }
    
    private void checkIfProviderRolesAreNull(Errors errors, AccountDomainWrapper accountDomainWrapper) {
        if (accountDomainWrapper.getProvidersCount() <= 0) {
            errors.rejectValue("providerRole", "error.required",
                    new Object[]{messageSourceService.getMessage("adminui.account.providerRole.label")}, null);
        }
    }
    
    */
}