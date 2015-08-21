/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.myaccount;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.api.InvalidCharactersPasswordException;
import org.openmrs.api.PasswordException;
import org.openmrs.api.ShortPasswordException;
import org.openmrs.api.UserService;
import org.openmrs.api.WeakPasswordException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

public class ChangePasswordPageController {
	
	public void get(PageModel model) {
		setModelAttributes(model);
	}
	
	public void setModelAttributes(PageModel model) {
		model.addAttribute("passwordMinLength",
		    Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8"));
	}
	
	public String post(PageModel model, BindingResult errors, @SpringBean("userService") UserService userService,
	                   @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
	                   @RequestParam("confirmPassword") String confirmPassword, HttpServletRequest request) {
		try {
			OpenmrsUtil.validatePassword(Context.getAuthenticatedUser().getUsername(), oldPassword, Context
			        .getAuthenticatedUser().getSystemId());
		}
		catch (PasswordException e) {
			errors.reject(e.getMessage());
		}
		if (!StringUtils.equals(newPassword, confirmPassword)) {
			//TODO inform in the ui about the mismatch
		}
		try {
			OpenmrsUtil.validatePassword(Context.getAuthenticatedUser().getUsername(), newPassword, Context
			        .getAuthenticatedUser().getSystemId());
		}
		catch (ShortPasswordException e) {
			// TODO: handle exception, inform in the ui
		}
		catch (InvalidCharactersPasswordException e) {
			// TODO: handle exception, inform in the ui
		}
		catch (WeakPasswordException e) {
			// TODO: handle exception, inform in the ui
		}
		catch (PasswordException e) {
			errors.reject(e.getMessage());
		}
		
		if (errors.hasErrors()) {
			model.addAttribute("errors", errors);
			setModelAttributes(model);
			return "myaccount/changePassword";
		} else {
			try {
				userService.changePassword(oldPassword, newPassword);
				InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "emr.account.changePassword.success");
				return "myaccount/myAccount";
			}
			catch (DAOException e) {
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    "adminui.account.changePassword.fail");
				
				setModelAttributes(model);
				return "myaccount/changePassword";
			}
		}
	}
}
