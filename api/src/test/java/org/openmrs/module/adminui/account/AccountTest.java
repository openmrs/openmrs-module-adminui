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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.User;

public class AccountTest {
	
	private Account initializeNewAccount(Person person) {
		return new Account(person);
	}
	
	@Test
	public void settingAccountDomainWrapperShouldSetPerson() {
		
		Account account = initializeNewAccount(new Person());
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
		
		Account account = initializeNewAccount(person);
		assertEquals("Mark", account.getGivenName());
		assertEquals("Jones", account.getFamilyName());
		assertEquals("M", account.getGender());
	}
	
	/**
	 * @see Account#getChangedBy()
	 * @verifies return the most recent changedBy value
	 */
	@Test
	public void getChangedBy_shouldReturnTheMostRecentChangedByValue() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
		Person person = new Person();
		person.setDateChanged(df.parse("2015-01-01"));
		person.setChangedBy(new User());
		
		User user = new User();
		user.setPerson(person);
		User expectedChangedByUser = new User();
		Date expectedDate = df.parse("2015-01-03");
		user.setDateChanged(expectedDate);
		user.setChangedBy(expectedChangedByUser);
		
		Provider provider = new Provider();
		provider.setDateChanged(df.parse("2015-01-02"));
		provider.setChangedBy(new User());
		
		Account account = new Account(person);
		account.addProviderAccount(provider);
		account.addUserAccount(user);
		
		assertNotNull(account.getChangedBy());
		assertEquals(expectedChangedByUser, account.getChangedBy());
	}
	
	/**
	 * @see Account#getDateChanged()
	 * @verifies return the most recent dateChanged value
	 */
	@Test
	public void getDateChanged_shouldReturnTheMostRecentDateChangedValue() throws Exception {
		DateFormat df = new SimpleDateFormat("yyyy-MM-DD");
		Person person = new Person();
		person.setDateChanged(df.parse("2015-01-01"));
		
		User user = new User();
		Date expectedDate = df.parse("2015-01-03");
		user.setDateChanged(expectedDate);
		
		Provider provider = new Provider();
		provider.setDateChanged(df.parse("2015-01-02"));
		
		Account account = new Account(person);
		account.addProviderAccount(provider);
		account.addUserAccount(user);
		
		assertNotNull(account.getDateChanged());
		assertEquals(expectedDate, account.getDateChanged());
	}
}
