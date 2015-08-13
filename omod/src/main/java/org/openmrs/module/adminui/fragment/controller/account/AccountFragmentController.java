/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.fragment.controller.account;

import org.openmrs.Person;
import org.openmrs.module.adminui.account.Account;
import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.action.FailureResult;
import org.openmrs.ui.framework.fragment.action.FragmentActionResult;
import org.openmrs.ui.framework.fragment.action.SuccessResult;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 */
public class AccountFragmentController {
	
	public FragmentActionResult unlock(@RequestParam("personId") Person person,
	                                   @SpringBean("adminAccountService") AccountService accountService, UiUtils ui) {
		
		try {
			Account account = accountService.getAccountByPerson(person);
			//account.unlock();
			return new SuccessResult(ui.message("emr.account.unlocked.successMessage"));
		}
		catch (Exception e) {
			return new FailureResult(ui.message("emr.account.unlock.failedMessage"));
		}
	}
	
}
