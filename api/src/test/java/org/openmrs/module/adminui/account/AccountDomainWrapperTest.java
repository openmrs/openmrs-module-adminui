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

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.providermanagement.api.ProviderManagementService;

public class AccountDomainWrapperTest {

    private AccountService accountService;

    private UserService userService;

    private PersonService personService;

    private ProviderService providerService;

    private ProviderManagementService providerManagementService;

    private Role fullPrivileges;

    private Role limitedPrivileges;

    private Role receptionApp;

    private Role archiveApp;

    private Role adminApp;

    @Before
    public void setup() {
        accountService = mock(AccountService.class);
        userService = mock(UserService.class);
        personService = mock(PersonService.class);
        providerService = mock(ProviderService.class);
        providerManagementService = mock(ProviderManagementService.class);

        fullPrivileges = new Role();
        fullPrivileges.setRole(AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL + "Full");
        limitedPrivileges = new Role();
        limitedPrivileges.setRole(AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL + "Limited");
        when(accountService.getAllPrivilegeLevels()).thenReturn(Arrays.asList(fullPrivileges, limitedPrivileges));

        receptionApp = new Role();
        receptionApp.setRole(AdminUiConstants.ROLE_PREFIX_CAPABILITY + "Reception");
        archiveApp = new Role();
        archiveApp.setRole(AdminUiConstants.ROLE_PREFIX_CAPABILITY + "Archives");
        adminApp = new Role();
        adminApp.setRole(AdminUiConstants.ROLE_PREFIX_CAPABILITY + "Admin");
        when(accountService.getAllCapabilities()).thenReturn(Arrays.asList(receptionApp, archiveApp, adminApp));

    }
    
    private AccountDomainWrapper initializeNewAccountDomainWrapper(Person person) {
        return new AccountDomainWrapper(person, accountService, userService,
                providerService, providerManagementService, personService);
    }

    @Test
    public void settingAccountDomainWrapperShouldSetPerson() {

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(new Person());
        account.setGivenName("Mark");
        account.setFamilyName("Jones");
        account.setGender("M");

        Person person = account.getPerson();
        assertEquals("Mark", person.getGivenName());
        assertEquals("Jones", person.getFamilyName());
        assertEquals("M", person.getGender());
    }
    
    @Test
    public void gettingAccountDomainWrapperShouldFetchFromPerson() {

        Person person = new Person();
        person.addName(new PersonName());
        person.getPersonName().setGivenName("Mark");
        person.getPersonName().setFamilyName("Jones");
        person.setGender("M");

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        assertEquals("Mark", account.getGivenName());
        assertEquals("Jones", account.getFamilyName());
        assertEquals("M", account.getGender());
    }
    
    @Test
    public void testCreatingPersonWithoutCreatingUsers() {

        Person person = new Person();

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        account.setGivenName("Mark");
        account.setFamilyName("Jones");
        account.setGender("M");

        // mimic spring binding blanks
        account.setUserEnabled(false);

        // make sure the person has been created, but not the user or provider
        assertNotNull(account.getPerson());
        assertEquals(0,account.getUsersCount());
    }
    
    @Test
    public void testCreatingPersonWithoutCreatingProviders() {

        Person person = new Person();

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        account.setGivenName("Mark");
        account.setFamilyName("Jones");
        account.setGender("M");

        // mimic spring binding blanks
        account.setProviderEnabled(false);

        // make sure the person has been created, but not the user or provider
        assertNotNull(account.getPerson());
        assertNull(account.getProviders());
    }
    
    @Test
    public void testCreatingPersonWithFourUsers() {

        Person person = new Person();

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        account.setGivenName("Mark");
        account.setFamilyName("Jones");
        account.setGender("M");

        account.setUserEnabled(true);
        account.createRequiredUsers(4);
        
        ArrayList<String> username = new ArrayList<String>();
        username.add("tom");
        username.add("dave");
        username.add("mike");
        username.add("john");
        
    	ArrayList<String> password = new ArrayList<String>();
    	password.add("Pass1word");
    	password.add("Pass2word");
    	password.add("Pass3word");
    	password.add("Pass4word");

        account.setUsernames(username);
        account.setPasswords(password);
        
        assertEquals("dave",account.getUsername(1));
        assertEquals("Pass3word",account.getPassword(2));
        assertEquals(4,account.getUsersCount());
        assertNull(account.getProviders());   
    }
    
    @Test
    public void shouldEnableNewUserAccount() {

        Person person = new Person();

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        account.setUserEnabled(true);

        assertNotNull(account.getUsers());
    }
    
    @Test
    public void shouldReturnFalseIfUserRetired() {

        Person person = new Person();
        person.setId(1);
        User user = new User();
        user.setPerson(person);
        user.setRetired(true);

        when(userService.getUsersByPerson((person), (false))).thenReturn(Collections.singletonList(user));

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        assertFalse(account.getUserEnabled());

    }
    
    @Test
    public void shouldReturnNullIfNoUser() {

        Person person = new Person();

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        assertFalse(account.getUserEnabled());

    }
    
    @Test
    public void shouldChangeExistingUserInformation() {

        Person person = new Person();
        person.setId(1);
        User user = new User();
        user.setPerson(person);
        user.setUsername("mjones");
        user.addRole(fullPrivileges);
        user.addRole(archiveApp);
        user.addRole(receptionApp);

        when(userService.getUsersByPerson((person), (false))).thenReturn(Collections.singletonList(user));

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        account.setUserEnabled(true);
        account.createRequiredUsers(1);
        
        ArrayList<String> username = new ArrayList<String>();
        username.add("msmith");
        account.setUsernames(username);
        
        ArrayList<String> privilegeLevel = new ArrayList<String>();
        privilegeLevel.add(limitedPrivileges.toString());
        account.setPrivilegeLevels(privilegeLevel);
        
        ArrayList<String[]> role = new ArrayList<String[]>();
        String[] roles = new String[2];
        roles[0] = archiveApp.toString();
        roles[1] = adminApp.toString();
        role.add(roles);
        account.setCapabilities(role);

        assertEquals("msmith", account.getUsername(0));
    }
    
    @Test
    public void testSaveAccountWithOnlyPerson() throws Exception {

        Person person = new Person();

        AccountDomainWrapper account = initializeNewAccountDomainWrapper(person);
        account.save();

        verify(personService).savePerson(person);

        verify(userService, never()).saveUser(any(User.class), anyString());
    }
}