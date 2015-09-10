/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.systemadmin.accounts;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.Role;

public class OtherAccountData {
	
	private String password;
	
	private boolean forceChangePassword = false;
	
	private String secretQuestion;
	
	private String secretAnswer;
	
	private boolean addProviderAccount = false;
	
	private boolean addUserAccount = false;
	
	private Role privilegeLevel;
	
	private List<Role> capabilities;
	
	public boolean getAddProviderAccount() {
		return addProviderAccount;
	}
	
	public void setAddProviderAccount(boolean addProviderAccount) {
		this.addProviderAccount = addProviderAccount;
	}
	
	public boolean getAddUserAccount() {
		return addUserAccount;
	}
	
	public void setAddUserAccount(boolean addUserAccount) {
		this.addUserAccount = addUserAccount;
	}
	
	public boolean getForceChangePassword() {
		return forceChangePassword;
	}
	
	public void setForceChangePassword(boolean forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getSecretAnswer() {
		return secretAnswer;
	}
	
	public void setSecretAnswer(String secretAnswer) {
		this.secretAnswer = secretAnswer;
	}
	
	public String getSecretQuestion() {
		return secretQuestion;
	}
	
	public void setSecretQuestion(String secretQuestion) {
		this.secretQuestion = secretQuestion;
	}
	
	public Role getPrivilegeLevel() {
		return privilegeLevel;
	}
	
	public void setPrivilegeLevel(Role privilegeLevel) {
		this.privilegeLevel = privilegeLevel;
	}
	
	public List<Role> getCapabilities() {
		if (capabilities == null) {
			capabilities = new ArrayList<Role>();
		}
		return capabilities;
	}
	
	public void setCapabilities(List<Role> capabilities) {
		this.capabilities = capabilities;
	}
}
