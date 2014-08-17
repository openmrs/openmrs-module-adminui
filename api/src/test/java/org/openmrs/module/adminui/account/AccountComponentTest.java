/*
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.test.BaseModuleContextSensitiveTest;


public class AccountComponentTest extends BaseModuleContextSensitiveTest {

    AccountService accountService;
    
    PersonService personService;

    @Before
    public void beforeAllTests() throws Exception {
        executeDataSet("accountComponentTestDataset.xml");
    }

    @Test
    public void shouldSavePerson() {

        Person person = new Person();

        AccountDomainWrapper account = accountService.getAccountByPerson(person);
        account.setGivenName("Mark");
        account.setFamilyName("Jones");
        account.setGender("M");
        account.save();

        Integer personId = account.getPerson().getPersonId();
        assertNotNull(personId);

        Context.flushSession();
        Context.clearSession();

        Person expectedPerson = personService.getPerson(personId);

        assertEquals("Mark", expectedPerson.getGivenName());
        assertEquals("Jones", expectedPerson.getFamilyName());
        assertEquals("M", expectedPerson.getGender());
        assertEquals(Context.getAuthenticatedUser(), expectedPerson.getPersonCreator());
        assertNotNull(expectedPerson.getPersonDateCreated());

        assertFalse(account.getUserEnabled());

    }

}