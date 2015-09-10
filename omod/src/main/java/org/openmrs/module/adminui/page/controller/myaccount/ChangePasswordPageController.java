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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.InvalidCharactersPasswordException;
import org.openmrs.api.PasswordException;
import org.openmrs.api.ShortPasswordException;
import org.openmrs.api.UserService;
import org.openmrs.api.WeakPasswordException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.web.user.UserProperties;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public class ChangePasswordPageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(method = RequestMethod.GET)
	public String overrideGetChangePasswordPage() {
		return "forward:/adminui/myaccount/changePassword.page";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String overridePostChangePassword() {
		return "forward:/adminui/myaccount/changePassword.page";
	}
	
	public void get(PageModel model) {
		setModelAttributes(model);
	}
	
	public void setModelAttributes(PageModel model) {
		model.addAttribute("passwordMinLength",
		    Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8"));
	}
	
	public String post(PageModel model, @SpringBean("userService") UserService userService,
	                   @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
	                   @RequestParam("confirmPassword") String confirmPassword,
	                   @SpringBean("messageSourceService") MessageSourceService mss, HttpServletRequest request) {
		
		BindingResult errors = new BeanPropertyBindingResult(userService, "userService");
		PasswordException exception = null;
		User user = Context.getAuthenticatedUser();
		
		try {
			OpenmrsUtil.validatePassword(user.getUsername(), newPassword, user.getSystemId());
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
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    mss.getMessage("adminui.account.changePassword.password.short", passChars, Context.getLocale()));
			} else if (exception instanceof WeakPasswordException) {
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    mss.getMessage("error.password.weak"));
			} else {
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    mss.getMessage("adminui.account.changePassword.fail") + "<br />" + exception.getLocalizedMessage());
			}
			
			return "myaccount/changePassword";
		} else {
			if (!StringUtils.equals(newPassword, confirmPassword)) {
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    mss.getMessage("adminui.account.changePassword.newAndConfirmPassword.should.match"));
			} else {
				try {
					String nextPage = "redirect:/index.htm";
					UserProperties userProperties = new UserProperties(user.getUserProperties());
					userService.changePassword(oldPassword, newPassword);
					if (userProperties.isSupposedToChangePassword()) {
						userProperties.setSupposedToChangePassword(false);
						try {
							//Why does hibernate see this as a different user instance?
							//Reload so that we have the instance in the cache
							user = userService.getUser(user.getUserId());
							userService.saveUser(user, null);
						}
						catch (Exception e) {
							log.warn("Failed to set forcePassword user property to false", e);
							throw new PasswordException("admin.changePassword.error.newPasswordSaved");
						}
					} else {
						nextPage = "myaccount/myAccount";
					}
					
					InfoErrorMessageUtil.flashInfoMessage(request.getSession(),
					    mss.getMessage("adminui.changePassword.success"));
					
					Context.refreshAuthenticatedUser();
					
					return nextPage;
				}
				catch (DAOException e) {
					failedToChangePassword(request, e, mss);
				}
				catch (APIException e) {
					failedToChangePassword(request, e, mss);
				}
			}
			
			setModelAttributes(model);
			
			return "myaccount/changePassword";
		}
	}
	
	private void failedToChangePassword(HttpServletRequest request, Exception e, MessageSourceService mss) {
		String failureMSg = null;
		
		if (e instanceof DAOException) {
			failureMSg = mss.getMessage("adminui.changePassword.failed.oldMissmatch");
		}
		request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
		    mss.getMessage("adminui.account.changePassword.fail") + "<br />" + failureMSg != null ? failureMSg : "");
	}
}
