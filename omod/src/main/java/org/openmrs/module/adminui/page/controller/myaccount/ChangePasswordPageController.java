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

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PasswordException;
import org.openmrs.api.UserService;
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
	
	public void get(PageModel model, @SpringBean("adminService") AdministrationService adminService) {
		setModelAttributes(model, adminService);
	}
	
	public void setModelAttributes(PageModel model, AdministrationService adminService) {
		model.addAttribute("passwordMinLength",
		    adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8"));
	}
	
	public String post(PageModel model, @SpringBean("userService") UserService userService,
	                   @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword,
	                   @RequestParam("confirmPassword") String confirmPassword,
	                   @SpringBean("adminService") AdministrationService adminService,
	                   @SpringBean("messageSourceService") MessageSourceService mss, HttpServletRequest request) {
		
		User user = Context.getAuthenticatedUser();
		String errorMessage = null;
		if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword) || StringUtils.isBlank(confirmPassword)) {
			errorMessage = "adminui.missing.requiredFields";
		}
		
		if (StringUtils.isNotBlank(newPassword) && StringUtils.isNotBlank(confirmPassword)
		        && !newPassword.equals(confirmPassword)) {
			errorMessage = "adminui.account.changePassword.newAndConfirmPassword.dontMatch";
		}
		
		if (errorMessage == null) {
			try {
				OpenmrsUtil.validatePassword(user.getUsername(), newPassword, user.getSystemId());
				
				String nextPage = "redirect:/index.htm";
				try {
					userService.changePassword(oldPassword, newPassword);
				}
				catch (DAOException e) {
					log.warn("Failed to change user password:", e);
					throw new PasswordException("adminui.account.changePassword.fail.hint");
				}
				UserProperties userProperties = new UserProperties(user.getUserProperties());
				if (userProperties.isSupposedToChangePassword()) {
					try {
						//Why does hibernate see this as a different user instance?
						//Reload so that we have the instance in the cache
						user = userService.getUser(user.getUserId());
						userProperties = new UserProperties(user.getUserProperties());
						userProperties.setSupposedToChangePassword(false);
						try {
							userService.saveUser(user, null);
						}
						catch (NoSuchMethodError ex) {
			            	//must be running platforms 2.0 and above which do not have the above method
			            	Method method = userService.getClass().getMethod("saveUser", new Class[] { User.class });
			            	method.invoke(userService, new Object[] { user });
			            }
					}
					catch (Exception e) {
						log.warn("Failed to set forcePassword user property to false", e);
						throw new PasswordException("adminui.account.changePassword.error.newPasswordSaved");
					}
				} else {
					nextPage = "myaccount/myAccount";
				}
				
				InfoErrorMessageUtil
				        .flashInfoMessage(request.getSession(), mss.getMessage("adminui.changePassword.success"));
				
				Context.refreshAuthenticatedUser();
				
				return nextPage;
			}
			catch (PasswordException e) {
				errorMessage = e.getMessage();
			}
			catch (Exception e) {
				log.error("Failed to change user password:", e);
			}
		}
		
		if (errorMessage == null) {
			errorMessage = "adminui.account.changePassword.fail";
		}
		
		request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, errorMessage);
		
		setModelAttributes(model, adminService);
		
		return "myaccount/changePassword";
	}
}
