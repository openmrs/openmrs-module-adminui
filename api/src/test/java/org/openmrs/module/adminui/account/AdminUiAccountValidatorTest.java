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

import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Role;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.util.OpenmrsUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

@RunWith(PowerMockRunner.class)
@PrepareForTest(OpenmrsUtil.class)
public class AdminUiAccountValidatorTest {

    private AdminUiAccountValidator validator;

    private AccountDomainWrapper account;

    private AccountService accountService;

    private UserService userService;

    private ProviderService providerService;

    private ProviderManagementService providerManagementService;

    private PersonService personService;

    private Role fullPrivileges;

    private Role someCapability;

    private Set<Role> someCapabilitySet;

    @Before
    public void setValidator() {

        accountService = Mockito.mock(AccountService.class);
        userService = Mockito.mock(UserService.class);
        providerService = Mockito.mock(ProviderService.class);
        providerManagementService = Mockito.mock(ProviderManagementService.class);
        personService = Mockito.mock(PersonService.class);

        validator = new AdminUiAccountValidator();
        validator.setMessageSourceService(Mockito.mock(MessageSourceService.class));
        validator.setUserService(userService);
        validator.setProviderManagementService(providerManagementService);

        fullPrivileges = new Role(AdminUiConstants.PRIVILEGE_LEVEL_FULL_ROLE);
        when(accountService.getAllPrivilegeLevels()).thenReturn(Collections.singletonList(fullPrivileges));

        someCapability = new Role(AdminUiConstants.ROLE_PREFIX_CAPABILITY + "Some Capability");
        someCapabilitySet = new HashSet<Role>();
        someCapabilitySet.add(someCapability);
        when(accountService.getAllCapabilities()).thenReturn(Collections.singletonList(someCapability));

        Person person = new Person();
        person.addName(new PersonName());

        account = new AccountDomainWrapper(person, accountService, userService, providerService,
                providerManagementService, personService);
    }

    /**
     * @verifies reject an empty givenname
     * @see AdminUiAccountValidator#validate(Object, Errors)
     */
    @Test
    public void validate_shouldRejectAnEmptyGivenname() throws Exception {
        Errors errors = new BindException(account, "account");
        validator.validate(account, errors);
        assertTrue(errors.hasFieldErrors("givenName"));
    }

    /**
     * @verifies reject an empty familyname
     * @see AdminUiAccountValidator#validate(Object, Errors)
     */
    @Test
    public void validate_shouldRejectAnEmptyFamilyname() throws Exception {
        account.setGivenName("give name");

        Errors errors = new BindException(account, "account");
        validator.validate(account, errors);
        assertTrue(errors.hasFieldErrors("familyName"));
    }

    @Test
    public void validate_shouldRejectAnEmptyGender() throws Exception {
        account.setGivenName("givenName");
        account.setFamilyName("familyName");

        Errors errors = new BindException(account, "account");
        validator.validate(account, errors);
        assertTrue(errors.hasFieldErrors("gender"));
    }

    /**
     * @verifies reject an empty privilegeLevel if user is not null
     * @see AdminUiAccountValidator#validate(Object, Errors)
     */
    @Test
    public void validate_shouldRejectAnEmptyPrivilegeLevelIfUserIsNotNull() throws Exception {
        account.setGivenName("give name");
        account.setFamilyName("family name");
        account.setGender("M");
        //account.setUsername("username");

        Errors errors = new BindException(account, "account");
        validator.validate(account, errors);
        assertTrue(errors.hasFieldErrors("privilegeLevel"));
    }


    @Test
    public void shouldCreateAnErrorMessageWhenUserIsNullAndNoProviderRole() {
        mockStatic(OpenmrsUtil.class);

        account.setFamilyName("family name");
        account.setGivenName("given Name");

        Errors errors = new BindException(account, "account");
        validator.validate(account, errors);

        assertTrue(errors.hasErrors());
    }
}
