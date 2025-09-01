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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class AdminUiAccountValidatorTest {

	private Account account;
	private Errors errors;

	@Mock
	private AdministrationService adminService;

	private AdminUiAccountValidator validator;

	@BeforeEach
	public void setUp() {
		account = new Account(null);
		errors = new BindException(account, "account");
		validator = new AdminUiAccountValidator();
	}

	/**
	 * @see AdminUiAccountValidator#validate(Object,org.springframework.validation.Errors)
	 * @verifies reject an account with no user or provider account
	 */
	@Test
	public void validate_shouldRejectAnAccountWithNoUserOrProviderAccount() throws Exception {
		try (MockedStatic<Context> mockedContext = Mockito.mockStatic(Context.class)) {
			mockedContext.when(Context::getAdministrationService).thenReturn(adminService);

			validator.validate(account, errors);

			assertTrue(errors.hasErrors());
			assertEquals(1, errors.getAllErrors().size());
			assertEquals("adminui.account.userOrProvider.required", errors.getAllErrors().get(0).getCode());
		}
	}
}
