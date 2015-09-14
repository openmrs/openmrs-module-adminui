/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.fragment.controller.systemadmin.accounts;

import java.util.Date;

import org.openmrs.Person;
import org.openmrs.User;
import org.openmrs.module.adminui.account.Account;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.springframework.web.bind.annotation.RequestParam;

public class AccountDetailsFragmentController {
	
	public SimpleObject getAuditInfo(@RequestParam(value = "uuid") Person person, UiUtils ui) {
		
		Account account = new Account(person);
		SimpleObject auditInfo = new SimpleObject();
		User changedBy = account.getChangedBy();
		Date dateChanged = account.getDateChanged();
		if (changedBy != null) {
			auditInfo.put("changedBy", ui.format(changedBy));
		}
		if (dateChanged != null) {
			auditInfo.put("dateChanged", ui.format(account.getDateChanged()));
		}
		
		return auditInfo;
	}
}
