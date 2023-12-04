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

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.adminui.TestUtils;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
@PowerMockIgnore("jdk.internal.reflect.*")
public class AccountServiceTest {
	
	private AccountServiceImpl accountService;
	
	private UserService userService;
	
	private PersonService personService;
	
	private ProviderService providerService;
	
	private ProviderManagementService providerManagementService;
	
	private AdministrationService adminService;
	
	@Before
	public void setup() {
		PowerMockito.mockStatic(Context.class);
		userService = mock(UserService.class);
		personService = mock(PersonService.class);
		providerService = mock(ProviderService.class);
		providerManagementService = mock(ProviderManagementService.class);
		adminService = mock(AdministrationService.class);
		
		accountService = new AccountServiceImpl();
		accountService.setUserService(userService);
		accountService.setPersonService(personService);
		accountService.setProviderService(providerService);
		accountService.setAdminService(adminService);
		when(Context.getUserService()).thenReturn(userService);
		when(Context.getProviderService()).thenReturn(providerService);
	}
	
	/**
	 * @verifies get all unique accounts
	 * @see AccountService#getAllAccounts()
	 */
	@Test
	public void getAllAccounts_shouldGetAllUniqueAccounts() throws Exception {
		Person person1 = new Person();
		User user1 = new User();
		user1.setPerson(person1);
		User user2 = new User();
		Person person2 = new Person();
		user2.setPerson(person2);
		User user3 = new User();
		user3.setPerson(person1);//duplicate
		User daemonUser = new User();
		daemonUser.setUuid(AdminUiConstants.DAEMON_USER_UUID);
		Person daemonPerson = new Person();
		daemonUser.setPerson(daemonPerson);
		
		Provider provider1 = new Provider();
		provider1.setPerson(person1);//duplicate
		Provider provider2 = new Provider();
		Person personA = new Person();
		provider2.setPerson(personA);
		Person personB = new Person();
		String unknownProviderUuid = "unknown";
		Provider unknownProvider = new Provider();
		unknownProvider.setPerson(personB);
		
		when(adminService.getGlobalProperty("emr.unknownProvider")).thenReturn(unknownProviderUuid);
		when(providerService.getProviderByUuid(unknownProviderUuid)).thenReturn(unknownProvider);
		when(providerService.getUnknownProvider()).thenReturn(unknownProvider);
		when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2, user3, daemonUser));
		when(providerService.getAllProviders()).thenReturn(Arrays.asList(provider1, provider2, unknownProvider));
		
		Assert.assertEquals(3, accountService.getAllAccounts().size());
	}
	
	/**
	 * @verifies return all roles with the capability prefix
	 * @see AccountService#getAllCapabilities()
	 */
	@Test
	public void getAllCapabilities_shouldReturnAllRolesWithTheCapabilityPrefix() throws Exception {
		Role role1 = new Role(AdminUiConstants.ROLE_PREFIX_CAPABILITY + "role1");
		Role role3 = new Role("role2");
		Role role2 = new Role(AdminUiConstants.ROLE_PREFIX_CAPABILITY + "role3");
		
		when(userService.getAllRoles()).thenReturn(Arrays.asList(role1, role2, role3));
		List<Role> capabilities = accountService.getAllCapabilities();
		Assert.assertEquals(2, capabilities.size());
		assertThat(capabilities, TestUtils.isCollectionOfExactlyElementsWithProperties("role",
		    AdminUiConstants.ROLE_PREFIX_CAPABILITY + "role1", AdminUiConstants.ROLE_PREFIX_CAPABILITY + "role3"));
	}
	
	/**
	 * @verifies return all roles with the privilege level prefix
	 * @see AccountService#getAllPrivilegeLevels()
	 */
	@Test
	public void getAllPrivilegeLevels_shouldReturnAllRolesWithThePrivilegeLevelPrefix() throws Exception {
		Role role1 = new Role(AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL + "role1");
		Role role3 = new Role("role2");
		Role role2 = new Role(AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL + "role3");
		
		when(userService.getAllRoles()).thenReturn(Arrays.asList(role1, role2, role3));
		List<Role> privilegeLevels = accountService.getAllPrivilegeLevels();
		Assert.assertEquals(2, privilegeLevels.size());
		assertThat(
		    privilegeLevels,
		    TestUtils.isCollectionOfExactlyElementsWithProperties("role", AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL
		            + "role1", AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL + "role3"));
	}
	
}
