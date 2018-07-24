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
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openmrs.Auditable;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Provider;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.user.UserProperties;
import org.openmrs.PersonAttribute;

public class Account {
	
	private Person person;
	
	private List<User> userAccounts;
	
	private List<Provider> providerAccounts;
	
	private Map<User, String> userPasswordMap = new HashMap<User, String>();
	
	public Account(Person person) {
		this.person = person;
		if (getPerson().getPersonId() != null) {
			List<User> users = Context.getUserService().getUsersByPerson(person, true);
			for (User u : users) {
				addUserAccount(u);
			}
			Collection<Provider> providers = Context.getProviderService().getProvidersByPerson(person, true);
			for (Provider p : providers) {
				addProviderAccount(p);
			}
		}
	}
	
	public Person getPerson() {
		if (person == null) {
			person = new Person();
		}
		return person;
	}
	
	public List<User> getUserAccounts() {
		if (userAccounts == null) {
			userAccounts = new ArrayList<User>();
		}
		return userAccounts;
	}
	
	public List<Provider> getProviderAccounts() {
		if (providerAccounts == null) {
			providerAccounts = new ArrayList<Provider>();
		}
		return providerAccounts;
	}
	
	public void addUserAccount(User user) {
		if (user.getPerson() == null) {
			user.setPerson(getPerson());
		} else if (person == null) {
			person = user.getPerson();
		} else if (!person.equals(user.getPerson())) {
			throw new APIException("User.person should match Account.person");
		}
		getUserAccounts().add(user);
	}
	
	public void addProviderAccount(Provider provider) {
		if (provider.getPerson() == null) {
			provider.setPerson(getPerson());
		} else if (person == null) {
			person = provider.getPerson();
		} else if (!person.equals(provider.getPerson())) {
			throw new APIException("provider.person should match Account.person");
		}
		getProviderAccounts().add(provider);
	}
	
	private void initializePersonNameIfNecessary() {
		if (getPerson().getPersonName() == null) {
			getPerson().addName(new PersonName());
		}
	}
	
	public void setGivenName(String givenName) {
		initializePersonNameIfNecessary();
		getPerson().getPersonName().setGivenName(givenName);
	}
	
	public String getGivenName() {
		return getPerson().getGivenName();
	}
	
	public void setFamilyName(String familyName) {
		initializePersonNameIfNecessary();
		getPerson().getPersonName().setFamilyName(familyName);
	}
	
	public String getFamilyName() {
		return getPerson().getFamilyName();
	}
	
	public void setGender(String gender) {
		getPerson().setGender(gender);
	}
	
	public String getGender() {
		return getPerson().getGender();
	}

	public List<PersonAttribute> getActiveAttributes() {
		return getPerson().getActiveAttributes();
	}

	public PersonAttribute getPersonAttribute(String attributeTypeUuid) {
		Person person = getPerson();
		if (person != null) {
			PersonAttribute attr = person.getAttribute(Context.getPersonService().getPersonAttributeTypeByUuid(
					attributeTypeUuid));
			return attr;
		}
		return null;
	}

	public Role getPrivilegeLevel(User user) {
		if (user != null && user.getRoles() != null) {
			for (Role r : user.getRoles()) {
				if (r.getRole().startsWith(AdminUiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL)) {
					return r;
				}
			}
		}
		return null;
	}
	
	public Set<Role> getCapabilities(User user) {
		Set<Role> capabilities = new HashSet<Role>();
		if (user != null && user.getRoles() != null) {
			for (Role role : user.getRoles()) {
				if (role.getRole().startsWith(AdminUiConstants.ROLE_PREFIX_CAPABILITY)) {
					capabilities.add(role);
				}
			}
		}
		return capabilities;
	}
	
	public User getCreator() {
		return getPerson().getCreator();
	}
	
	public Date getDateCreated() {
		return getPerson().getDateCreated();
	}
	
	/**
	 * Gets changedBy value for the most recently edited entity of the account i.e the person,
	 * providers and user accounts
	 * 
	 * @return the most recent changedBy value
	 * @should return the most recent changedBy value
	 */
	public User getChangedBy() {
		if (getSortedAuditables().isEmpty()) {
			return null;
		}
		
		return getSortedAuditables().get(0).getChangedBy();
	}
	
	/**
	 * Get dateChanged value for the most recently edited entity of the account i.e the person,
	 * providers and user accounts
	 *
	 * @return the most recent dateChanged value
	 * @should return the most recent dateChanged value
	 */
	public Date getDateChanged() {
		if (getSortedAuditables().isEmpty()) {
			return null;
		}
		
		return getSortedAuditables().get(0).getDateChanged();
		
	}
	
	public boolean isSupposedToChangePassword(User user) {
		return new UserProperties(user.getUserProperties()).isSupposedToChangePassword();
	}
	
	public String getPassword(User user) {
		return userPasswordMap.get(user);
	}
	
	public String setPassword(User user, String password) {
		return userPasswordMap.put(user, password);
	}
	
	private List<Auditable> getSortedAuditables() {
		List<Auditable> auditables = new ArrayList<Auditable>();
		auditables.add(getPerson());
		//Add PersonName because in the API when an item in a child
		//collection is edited the parent is not marked as edited
		if (getPerson().getPersonName() != null) {
			auditables.add(getPerson().getPersonName());
		}
		auditables.addAll(getUserAccounts());
		auditables.addAll(getProviderAccounts());
		Collections.sort(auditables, Collections.reverseOrder(new Comparator<Auditable>() {
			
			@Override
			public int compare(Auditable a1, Auditable a2) {
				Date date1 = (a1 != null) ? a1.getDateChanged() : null;
				Date date2 = (a2 != null) ? a2.getDateChanged() : null;
				return OpenmrsUtil.compareWithNullAsEarliest(date1, date2);
			}
		}));
		
		return auditables;
		
	}
	
}
