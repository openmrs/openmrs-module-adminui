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

import static org.powermock.api.mockito.PowerMockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
public class AdminUiAccountValidatorTest {
	
	private AdminUiAccountValidator validator;
	
	private AdministrationService adminService;
	
	@Before
	public void setValidator() {
		PowerMockito.mockStatic(Context.class);
		adminService = Mockito.mock(AdministrationService.class);
		when(Context.getAdministrationService()).thenReturn(adminService);
		
		validator = new AdminUiAccountValidator();
	}
	
	/**
	 * @see AdminUiAccountValidator#validate(Object,org.springframework.validation.Errors)
	 * @verifies reject an account with no user or provider account
	 */
	@Test
	public void validate_shouldRejectAnAccountWithNoUserOrProviderAccount() throws Exception {
		Account account = new Account(null);
		Errors errors = new BindException(account, "account");
		validator.validate(account, errors);
		Assert.assertTrue(errors.hasErrors());
		Assert.assertEquals(1, errors.getAllErrors().size());
		Assert.assertEquals("adminui.account.userOrProvider.required", errors.getAllErrors().get(0).getCode());
	}
}
