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

import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class ChangeDefaultsPageController {

	public String get(PageModel pageModel) {
		if (Context.isAuthenticated()) {
			User user = Context.getAuthenticatedUser();
			Map<String, String> props = user.getUserProperties();
			UserDefaults userDefaults = new UserDefaults();
			userDefaults.setDefaultLocale(props.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE));
			userDefaults.setProficientLocales(props.get(OpenmrsConstants.USER_PROPERTY_PROFICIENT_LOCALES));
			pageModel.addAttribute("userDefaults", userDefaults);
			pageModel.addAttribute("locales", Context.getAdministrationService().getPresentationLocales());
		}
		return "account/changeDefaults";
	}

	public String post(@MethodParam("createUserDefaults") @BindParams UserDefaults defaults,
	                   BindingResult errors,
	                   @SpringBean("userService") UserService userService,
	                   @SpringBean("messageSourceService") MessageSourceService messageSourceService,
	                   @SpringBean("messageSource") MessageSource messageSource,
	                   HttpServletRequest request,
	                   PageModel model) {
		if (errors.hasErrors()) {
			sendErrorMessage(errors, messageSource, request);
			model.addAttribute("errors", errors);
			return "account/changePassword";
		}
		return saveDefaults(defaults, userService, messageSourceService, request);
	}

	private void sendErrorMessage(BindingResult errors, MessageSource messageSource, HttpServletRequest request) {
		List<ObjectError> allErrors = errors.getAllErrors();
		String message = getMessageErrors(messageSource, allErrors);
		request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, message);
	}

	private String getMessageErrors(MessageSource messageSource, List<ObjectError> allErrors) {
		String message = "";
		for (ObjectError error : allErrors) {
			Object[] arguments = error.getArguments();
			String errorMessage = messageSource.getMessage(error.getCode(), arguments, Context.getLocale());
			message = message.concat(replaceArguments(errorMessage, arguments).concat("<br>"));
		}
		return message;
	}

	private String replaceArguments(String message, Object[] arguments) {
		if (arguments != null) {
			for (int i = 0; i < arguments.length; i++) {
				String argument = (String) arguments[i];
				message = message.replaceAll("\\{" + i + "\\}", argument);
			}
		}
		return message;
	}

	public String saveDefaults(UserDefaults defaults, UserService userService, MessageSourceService messageSourceService,
	                           HttpServletRequest request) {
		try {
			User user = Context.getAuthenticatedUser();
			Map<String, String> props = user.getUserProperties();
			props.put(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE, defaults.getDefaultLocale());
			props.put(OpenmrsConstants.USER_PROPERTY_PROFICIENT_LOCALES, defaults.getProficientLocales());
			user.setUserProperties(props);
			userService.saveUser(user, null);
			request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE,
					messageSourceService.getMessage("adminui.account.defaults.success", null, Context.getLocale()));
			request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_TOAST_MESSAGE, "true");
		} catch (Exception ex) {
			request.getSession().setAttribute(
					AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
					messageSourceService.getMessage("adminui.account.defaults.fail", new Object[]{ex.getMessage()},
							Context.getLocale()));
			return "account/changeDefaults";
		}
		return "account/myAccount";
	}

	public UserDefaults createUserDefaults(String defaultLocale, String proficientLocales) {
		return new UserDefaults(defaultLocale, proficientLocales);
	}

	public class UserDefaults {

		private String defaultLocale;
		private String proficientLocales;

		public UserDefaults() {
		}

		public UserDefaults(String defaultLocale, String proficientLocales) {
			this.defaultLocale = defaultLocale;
			this.proficientLocales = proficientLocales;
		}

		public String getDefaultLocale() {
			return defaultLocale;
		}

		public void setDefaultLocale(String defaultLocale) {
			this.defaultLocale = defaultLocale;
		}

		public String getProficientLocales() {
			return proficientLocales;
		}

		public void setProficientLocales(String proficientLocales) {
			this.proficientLocales = proficientLocales;
		}
	}
}
