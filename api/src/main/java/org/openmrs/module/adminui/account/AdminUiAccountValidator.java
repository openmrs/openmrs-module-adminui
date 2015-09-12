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

import java.util.List;

import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.annotation.Handler;
import org.openmrs.validator.ValidateUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = { Account.class }, order = 50)
public class AdminUiAccountValidator implements Validator {
	
	/**
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Account.class.isAssignableFrom(clazz);
	}
	
	/**
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 *      org.springframework.validation.Errors)
	 * @should reject an account with no user or provider account
	 **/
	
	@Override
	public void validate(Object obj, Errors errors) {
		if (obj == null || !(obj instanceof Account)) {
			throw new IllegalArgumentException("The parameter obj should not be null and must be of type" + Account.class);
		}
		
		Account account = (Account) obj;
		if (account.getProviderAccounts().size() == 0 && account.getUserAccounts().size() == 0) {
			errors.reject("adminui.account.userOrProvider.required");
		}
		
		try {
			errors.pushNestedPath("person");
			ValidateUtil.validate(account.getPerson(), errors);
		}
		finally {
			errors.popNestedPath();
		}
		
		List<User> userAccounts = account.getUserAccounts();
		for (int i = 0; i < userAccounts.size(); i++) {
			try {
				errors.pushNestedPath("userAccounts[" + i + "]");
				ValidateUtil.validate(account.getUserAccounts().get(0), errors);
			}
			finally {
				errors.popNestedPath();
			}
		}
		
		List<Provider> providerAccounts = account.getProviderAccounts();
		for (int i = 0; i < providerAccounts.size(); i++) {
			try {
				errors.pushNestedPath("providerAccounts[" + i + "]");
				ValidateUtil.validate(account.getProviderAccounts().get(0), errors);
			}
			finally {
				errors.popNestedPath();
			}
		}
		
	}
	
}
