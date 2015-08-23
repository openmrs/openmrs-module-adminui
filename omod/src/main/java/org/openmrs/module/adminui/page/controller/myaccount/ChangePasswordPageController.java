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
import org.openmrs.api.APIException;
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
import org.springframework.validation.BeanPropertyBindingResult;
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
	
	public String post(PageModel model, @SpringBean("userService") UserService userService,
	                   @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
	                   @RequestParam("confirmPassword") String confirmPassword, HttpServletRequest request) {
		BindingResult errors = new BeanPropertyBindingResult(userService, "userService");
		PasswordException exception = null;
		
		try {
			OpenmrsUtil.validatePassword(Context.getAuthenticatedUser().getUsername(), newPassword, Context
			        .getAuthenticatedUser().getSystemId());
		}
		catch (ShortPasswordException e) {
			errors.reject(e.getMessage());
			exception = e;
		}
		catch (InvalidCharactersPasswordException e) {
			errors.reject(e.getMessage());
			exception = e;
		}
		catch (WeakPasswordException e) {
			errors.reject(e.getMessage());
			exception = e;
		}
		catch (PasswordException e) {
			errors.reject(e.getMessage());
			exception = e;
		}
		
		if (errors.hasErrors() && exception != null) {
			model.addAttribute("errors", errors);
			Object[] passChars = new Object[1];
			passChars[0] = Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH);
			setModelAttributes(model);
			if (exception instanceof ShortPasswordException) {
				request.getSession().setAttribute(
				    UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    Context.getMessageSourceService().getMessage("adminui.account.changePassword.password.short", passChars,
				        Context.getLocale()));
			} else if (exception instanceof WeakPasswordException) {
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    Context.getMessageSourceService().getMessage("error.password.weak"));
			} else {
				request.getSession().setAttribute(
				    UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    Context.getMessageSourceService().getMessage("adminui.account.changePassword.fail") + "<br />"
				            + exception.getLocalizedMessage());
			}
			
			return "myaccount/changePassword";
		} else {
			if (!StringUtils.equals(newPassword, confirmPassword)) {
				request.getSession().setAttribute(
				    UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    Context.getMessageSourceService().getMessage(
				        "adminui.account.changePassword.newAndConfirmPassword.should.match"));
			} else {
				try {
					userService.changePassword(oldPassword, newPassword);
					InfoErrorMessageUtil.flashInfoMessage(request.getSession(), Context.getMessageSourceService()
					        .getMessage("adminui.changePassword.success"));
					Context.refreshAuthenticatedUser();
					
					return "myaccount/myAccount";
				}
				catch (DAOException e) {
					failedToChangePassword(request, e);
				}
				catch (APIException e) {
					failedToChangePassword(request, e);
				}
			}
			setModelAttributes(model);
			
			return "myaccount/changePassword";
		}
	}
	
	private void failedToChangePassword(HttpServletRequest request, Exception e) {
		String failureMSg = null;
		
		if (e instanceof DAOException) {
			failureMSg = Context.getMessageSourceService().getMessage("adminui.changePassword.failed.oldMissmatch");
		}
		request.getSession()
		        .setAttribute(
		            UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
		            Context.getMessageSourceService().getMessage("adminui.account.changePassword.fail") + "<br />"
		                    + failureMSg != null ? failureMSg : e.getLocalizedMessage());
	}
}
