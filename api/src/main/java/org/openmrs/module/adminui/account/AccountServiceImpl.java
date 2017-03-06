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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;
import org.openmrs.Provider;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AccountServiceImpl extends BaseOpenmrsService implements AccountService {
	
	private UserService userService;
	
	private PersonService personService;
	
	private ProviderService providerService;
	
	private AdministrationService adminService;
	
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
	 * @param personService the personService to set
	 */
	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}
	
	/**
	 * @param adminService the adminService to set
	 */
	public void setAdminService(AdministrationService adminService) {
		this.adminService = adminService;
	}
	
	/**
	 * @see AccountService#saveAccount(Account)
	 */
	@Override
	public void saveAccount(Account account) {
		personService.savePerson(account.getPerson());
		for (Provider provider : account.getProviderAccounts()) {
			providerService.saveProvider(provider);
		}
		for (User user : account.getUserAccounts()) {
			if (user.getUserId() == null && StringUtils.isNotBlank(account.getPassword(user))) {
				String password = account.getPassword(user);
				userService.createUser(user, password);
			}
			else {
				try {
					userService.saveUser(user, null);
				}
				catch (NoSuchMethodError ex) {
					try {
		            	//must be running platforms 2.0 and above which do not have the above method
		            	Method method = userService.getClass().getMethod("saveUser", new Class[] { User.class });
		            	method.invoke(userService, new Object[] { user });
					}
					catch (Exception e) {
						throw new APIException("Failed to save user account", e);
					}
	            }
			}
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Account> getAllAccounts() {
		
		Map<Person, Account> personAccountMap = new LinkedHashMap<Person, Account>();
		
		for (User user : userService.getAllUsers()) {
			//exclude daemon user
			if (AdminUiConstants.DAEMON_USER_UUID.equals(user.getUuid())) {
				continue;
			}
			
			Person person = user.getPerson();
			Account account = personAccountMap.get(person);
			if (account == null) {
				account = new Account(person);
				personAccountMap.put(person, account);
			}
			if (!account.getUserAccounts().contains(user)) {
				account.addUserAccount(user);
			}
		}
		
		List<Provider> unknownProviders = new ArrayList<Provider>();
		String unknownProviderUuid = adminService.getGlobalProperty("emr.unknownProvider");
		if (StringUtils.isNotBlank(unknownProviderUuid)) {
			unknownProviders.add(providerService.getProviderByUuid(unknownProviderUuid));
		}
		//Include also the unknown provider from core
		if (providerService.getUnknownProvider() != null) {
			unknownProviders.add(providerService.getUnknownProvider());
		}
		for (Provider provider : providerService.getAllProviders()) {
			//skip the unknown providers
			if (unknownProviders.contains(provider)) {
				continue;
			}
			
			Person person = provider.getPerson();
			if (person == null) {
				person = new Person();
			}
			Account account = personAccountMap.get(person);
			if (account == null) {
				account = new Account(person);
				personAccountMap.put(person, account);
			}
			if (!account.getProviderAccounts().contains(provider)) {
				//We don't use account.addProviderAccount because
				//If the session gets auto flushed, the new person object
				//that account.addProviderAccount sets might get saved to the DB
				account.getProviderAccounts().add(provider);
			}
		}
		
		List<Account> accounts = new ArrayList<Account>();
		accounts.addAll(personAccountMap.values());
		
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
	
}
