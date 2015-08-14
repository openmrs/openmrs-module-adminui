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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.APIException;
import org.openmrs.api.AdministrationService;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.adminui.account.Account;
import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.module.adminui.account.AdminUiAccountValidator;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestParam;

public class AccountPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public Account getAccount(@RequestParam(value = "personId", required = false) Person person,
	                          @SpringBean("adminAccountService") AccountService accountService) {
		
		Account account;
		
		if (person == null) {
			account = accountService.getAccountByPerson(new Person());
		} else {
			account = accountService.getAccountByPerson(person);
			if (account == null)
				throw new APIException("Failed to find user account matching person with id:" + person.getPersonId());
		}
		
		return account;
	}
	
	/**
	 * @param model
	 * @param account
	 * @param accountService
	 * @param providerManagementService
	 */
	public void get(PageModel model, @MethodParam("getAccount") Account account,
	                @SpringBean("adminAccountService") AccountService accountService,
	                @SpringBean("adminService") AdministrationService administrationService,
	                @SpringBean("providerManagementService") ProviderManagementService providerManagementService) {
		
		setModelAttributes(model, account, accountService, administrationService, providerManagementService);
	}
	
	/**
	 * @param account
	 * @param errors
	 * @param userEnabled
	 * @param providerEnabled
	 * @param countTabs
	 * @param messageSourceService
	 * @param accountService
	 * @param administrationService
	 * @param providerManagementService
	 * @param accountValidator
	 * @param model
	 * @param request
	 * @return
	 */
	public String post(@MethodParam("getAccount") @BindParams Account account, BindingResult errors,
	                   @RequestParam(value = "userEnabled", defaultValue = "false") boolean userEnabled,
	                   @RequestParam(value = "providerEnabled", defaultValue = "false") boolean providerEnabled,
	                   @RequestParam(value = "countTabs", defaultValue = "1") String countTabs,
	                   @SpringBean("messageSourceService") MessageSourceService messageSourceService,
	                   @SpringBean("adminAccountService") AccountService accountService,
	                   @SpringBean("adminService") AdministrationService administrationService,
	                   @SpringBean("accountValidator") AdminUiAccountValidator accountValidator,
	                   @SpringBean("providerManagementService") ProviderManagementService providerManagementService,
	                   PageModel model, HttpServletRequest request) {
		
		int countUsers = Integer.parseInt(countTabs);
		
		ArrayList<String> username = new ArrayList<String>();
		ArrayList<String> password = new ArrayList<String>();
		ArrayList<String> confirmPassword = new ArrayList<String>();
		ArrayList<String> privilegeLevel = new ArrayList<String>();
		ArrayList<String[]> roles = new ArrayList<String[]>();
		
		if (userEnabled) {
			
			for (int i = 1; i <= countUsers; i++) {
				username.add(request.getParameter("user" + i + "_username"));
				password.add(request.getParameter("user" + i + "_password"));
				confirmPassword.add(request.getParameter("user" + i + "_confirmPassword"));
				privilegeLevel.add(request.getParameter("user" + i + "_privilegeLevel"));
				roles.add(request.getParameterValues("user" + i + "_capabilities"));
			}
			
			account.createRequiredUsers(countUsers);
			account.setUsernames(username);
			account.setPasswords(password);
			account.setConfirmPasswords(confirmPassword);
			account.setPrivilegeLevels(privilegeLevel);
			account.setCapabilities(roles);
		}
		
		if (providerEnabled) {
			//TODO set provider details
		}
		
		accountValidator.validate(account, errors);
		if (!errors.hasErrors()) {
			try {
				accountService.saveAccount(account);
				InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.account.saved");
				return "redirect:/adminui/systemadmin/accounts/manageAccounts.page";
			}
			catch (Exception e) {
				errors.reject("adminui.account.error.save.fail");
			}
		}
		
		setModelAttributes(model, account, accountService, administrationService, providerManagementService);
		sendErrorMessage(errors, model, messageSourceService, request);
		
		return "systemadmin/accounts/account";
		
	}
	
	public void setModelAttributes(PageModel model, Account account, AccountService accountService,
	                               AdministrationService administrationService,
	                               ProviderManagementService providerManagementService) {
		model.addAttribute("account", account);
		model.addAttribute("capabilities", accountService.getAllCapabilities());
		model.addAttribute("privilegeLevels", accountService.getAllPrivilegeLevels());
		model.addAttribute("rolePrefix", AdminUiConstants.ROLE_PREFIX_CAPABILITY);
		model.addAttribute("allowedLocales", administrationService.getAllowedLocales());
		model.addAttribute("providerRoles", providerManagementService.getAllProviderRoles(false));
	}
	
	private void sendErrorMessage(BindingResult errors, PageModel model, MessageSourceService mss, HttpServletRequest request) {
		model.addAttribute("errors", errors);
		StringBuffer errorMessage = new StringBuffer(mss.getMessage("error.failed.validation"));
		errorMessage.append("<ul>");
		for (ObjectError error : errors.getAllErrors()) {
			errorMessage.append("<li>");
			errorMessage.append(mss.getMessage(error.getCode(), error.getArguments(), error.getDefaultMessage(), null));
			errorMessage.append("</li>");
		}
		errorMessage.append("</ul>");
		request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, errorMessage.toString());
	}
}
