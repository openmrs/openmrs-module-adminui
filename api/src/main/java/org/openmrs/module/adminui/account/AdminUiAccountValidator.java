/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.adminui.account;

import org.apache.commons.lang.StringUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.api.UserService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = {AccountDomainWrapper.class}, order = 50)
public class AdminUiAccountValidator implements Validator {

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
     * 
     *  @see org.springframework.validation.Validator#validate(java.lang.Object,
     *      org.springframework.validation.Errors)
	**/
    
    @Override
    public void validate(Object obj, Errors errors) { 
        if (obj == null || !(obj instanceof AccountDomainWrapper))
            throw new IllegalArgumentException("The parameter obj should not be null and must be of type" + AccountDomainWrapper.class);

        AccountDomainWrapper account = (AccountDomainWrapper) obj;

        checkIfGivenAndFamilyNameAreNotNull(errors, account);
        checkIfGenderIsNull(errors, account);
        checkIfUserAndProviderAreNull(errors, account);   	
    	
    }
    
    
    
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
    
}