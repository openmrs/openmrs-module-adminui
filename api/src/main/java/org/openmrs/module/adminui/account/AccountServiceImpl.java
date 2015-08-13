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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AccountServiceImpl extends BaseOpenmrsService implements AccountService {
	
	private UserService userService;
	
	private PersonService personService;
	
	private ProviderService providerService;
	
	private ProviderManagementService providerManagementService;
	
	/**
	 * @param userService the userService to set
	 */
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * @param providerService
	 */
	public void setProviderService(ProviderService providerService) {
		this.providerService = providerService;
	}
	
	/**
	 * @param providerManagementService
	 */
	public void setProviderManagementService(ProviderManagementService providerManagementService) {
		this.providerManagementService = providerManagementService;
	}
	
	/**
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	/**
	 * @see org.openmrs.module.adminui.account.AccountService#saveAccount(Account)
	 */
	@Override
	@Transactional
	public void saveAccount(Account account) {
		account.save();
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Account> getAllAccounts() {
		
		Map<Person, Account> byPerson = new LinkedHashMap<Person, Account>();
		
		for (User user : userService.getAllUsers()) {
			//exclude daemon user
			if (AdminUiConstants.DAEMON_USER_UUID.equals(user.getUuid()))
				continue;
			
			if (!user.getPerson().isVoided()) {
				byPerson.put(user.getPerson(), new Account(user.getPerson(), this, userService, providerService,
				        providerManagementService, personService));
			}
		}
		
		/*	Currently, OpenMRS core API doesn't support to view account associated with multiple providers. 
		 * To implement it, below function will do the task, but supportive functions need to be added.
		 */
		
		/*for (Provider provider : providerService.getAllProviders()) {

		    // skip the baked-in unknown provider
		    if (provider.equals(emrApiProperties.getUnknownProvider())) {
		        continue;
		    }

		    if (provider.getPerson() == null)
		        throw new APIException("Providers not associated to a person are not supported");

		    AccountDomainWrapper account = byPerson.get(provider.getPerson());
		    if (account == null && !provider.getPerson().isVoided()) {
		        byPerson.put(provider.getPerson(), new AccountDomainWrapper(provider.getPerson(), this, userService,
		                providerService, providerManagementService, personService, providerIdentifierGenerator));
		    }
		}*/
		
		List<Account> accounts = new ArrayList<Account>();
		for (Account account : byPerson.values()) {
			accounts.add(account);
		}
		
		return accounts;
	}
	
	/**
	 * @see org.openmrs.module.adminui.account.AccountService#getAllCapabilities()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Role> getAllCapabilities() {
		List<Role> capabilities = new ArrayList<Role>();
		for (Role candidate : userService.getAllRoles()) {
			if (candidate.getName().startsWith(AdminUiConstants.ROLE_PREFIX_CAPABILITY))
				capabilities.add(candidate);
		}
		return capabilities;
	}
	
	/**
	 * @see org.openmrs.module.adminui.account.AccountService#getAllPrivilegeLevels()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Role> getAllPrivilegeLevels() {
		List<Role> privilegeLevels = new ArrayList<Role>();
		for (Role candidate : userService.getAllRoles()) {
			if (candidate.getName().startsWith(AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL))
				privilegeLevels.add(candidate);
		}
		return privilegeLevels;
	}
	
	/**
	 * @see org.openmrs.module.adminui.account.AccountService#getAccountByPerson(org.openmrs.Person)
	 */
	@Override
	@Transactional(readOnly = true)
	public Account getAccountByPerson(Person person) {
		return new Account(person, this, userService, providerService, providerManagementService, personService);
	}
	
}
