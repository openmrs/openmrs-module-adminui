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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Provider;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.adminui.account.Account;
import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class ManageAccountsPageController {
	
	/**
	 * @param model
	 * @param accountService
	 */
	public void get(PageModel model, @SpringBean("adminAccountService") AccountService accountService,
	                @SpringBean("adminService") AdministrationService adminService) {
		
		List<Account> accounts = accountService.getAllAccounts();
		model.addAttribute("accounts", accounts);
		Map<Provider, String> providerNameMap = new HashMap<Provider, String>();
		for (Account a : accountService.getAllAccounts()) {
			//There seems to be some sort of bug in hbm mappings for provider
			//in the provider management module where when a provider
			//account is not linked to the user the name field never gets
			//populated, this is a hack to fetch the name via raw SQL
			for (Provider p : a.getProviderAccounts()) {
				if (p.getPerson() == null) {
					List<List<Object>> rows = adminService.executeSQL(
					    "select name from provider where provider_id=" + p.getProviderId(), true);
					
					providerNameMap.put(p, (String) rows.get(0).get(0));
				}
			}
		}
		
		model.addAttribute("providerNameMap", providerNameMap);
	}
	
}
