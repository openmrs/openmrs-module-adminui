/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
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

@Handler(supports = { Account.class }, order = 50)
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
		return Account.class.isAssignableFrom(clazz);
	}
	
	/**
	 * @should reject an empty family name
	 * @should reject an empty given name
	 * @should reject an empty gender
	 * @should reject if none of the checkbox (user or provider) ticked
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 **/
	
	@Override
	public void validate(Object obj, Errors errors) {
		if (obj == null || !(obj instanceof Account))
			throw new IllegalArgumentException("The parameter obj should not be null and must be of type" + Account.class);
		
		Account account = (Account) obj;
		
		checkIfGivenAndFamilyNameAreNotNull(errors, account);
		checkIfGenderIsNull(errors, account);
		checkIfUserAndProviderAreNull(errors, account);
		
	}
	
	private void checkIfGivenAndFamilyNameAreNotNull(Errors errors, Account account) {
		if (StringUtils.isBlank(account.getGivenName())) {
			errors.rejectValue("givenName", "error.required",
			    new Object[] { messageSourceService.getMessage("adminui.person.givenName") }, null);
		}
		if (StringUtils.isBlank(account.getFamilyName())) {
			errors.rejectValue("familyName", "error.required",
			    new Object[] { messageSourceService.getMessage("adminui.person.familyName") }, null);
		}
	}
	
	private void checkIfGenderIsNull(Errors errors, Account account) {
		if (StringUtils.isBlank(account.getGender())) {
			errors.rejectValue("gender", "error.required",
			    new Object[] { messageSourceService.getMessage("adminui.gender") }, null);
		}
	}
	
	private void checkIfUserAndProviderAreNull(Errors errors, Account account) {
		if (account.getUserEnabled() == false && account.getProviderEnabled() == false) {
			errors.rejectValue("userEnabled", "error.required",
			    new Object[] { messageSourceService.getMessage("Both User and Provider can't be null") }, null);
		}
	}
	
}
