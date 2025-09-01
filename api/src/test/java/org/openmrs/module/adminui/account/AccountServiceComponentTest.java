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
import org.openmrs.Provider;
import org.openmrs.User;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.test.jupiter.BaseModuleContextSensitiveTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountServiceComponentTest extends BaseModuleContextSensitiveTest{
	
	private AccountService accountService;
	
	private UserService userService;

	private ProviderService providerService;
	
	@BeforeEach
	public void setup() {
		userService = Context.getUserService();
		accountService = Context.getService(AccountService.class);
		providerService = Context.getProviderService();
	}
	
	/**
	 * @verifies get all unique accounts
	 * @see AccountService#getAllAccounts()
	 */
	@Test
	public void saveAccount_shouldSaveNewAccountWithAProviderAndUserAccount() throws Exception {
		executeDataSet("accountComponentTestDataset.xml");
		User user = new User();
		user.setUsername("stester");
		user.addRole(userService.getRole("Privilege Level: Full"));//privilege level
		user.addRole(userService.getRole("Application Role: Archives"));//capability
		
		Provider provider = new Provider();
		provider.setProviderRole(providerService.getProviderRole(1001));
		Account account = new Account(null);
		account.setFamilyName("some");
		account.setGivenName("tester");
		account.setGender("M");
		account.addUserAccount(user);
		account.addProviderAccount(provider);
		account.setPassword(user, "Tester123");
		accountService.saveAccount(account);
		
		assertNotNull(user.getId());
		assertNotNull(provider.getId());
		assertNotNull(account.getPerson().getId());
	}
	
}
