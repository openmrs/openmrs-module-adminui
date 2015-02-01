/**
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

package org.openmrs.module.adminui.page.controller.account;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.Privilege;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

public class PrivilegePageController {
	
	public void get(PageModel model, @RequestParam(value = "privilegeName", required = false) String privilegeName,
	                @SpringBean("adminAccountService") AccountService accountService) {
		
		Privilege privilege = new Privilege();
		if (privilegeName != null) {
			
		}
		model.addAttribute("privilege", privilege);
	}
	
	public String post(PageModel model, @ModelAttribute("privilege") @BindParams Privilege privilege, BindingResult errors,
	                   @SpringBean("locationService") LocationService locationService,
	                   @RequestParam(required = false, value = "save") String saveFlag, HttpServletRequest request) {
		
		Errors newErrors = new BindException(privilege, "privilege");
		
		if (!newErrors.hasErrors()) {
			try {
				if (saveFlag.length() > 3) {
					Context.getUserService().savePrivilege(privilege);
					request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE,
					    "adminui.privilege.saved");
				}
				return "redirect:/adminui/account/managePrivileges.page";
			}
			catch (Exception e) {
				request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
			}
		}
		
		else {
			
		}
		
		model.addAttribute("errors", newErrors);
		model.addAttribute("privilege", privilege);
		
		return "account/managePrivileges";
	}
}
