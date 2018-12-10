/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;

public final class PasswordValidation {
	private PasswordValidation() {

	}

	public static void addPasswordValidationAttributes(PageModel model, AdministrationService adminService, MessageSourceService mss) {
		String passwordMinLength = adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8");
		boolean passwordReqUpperAndLowerCase = "true".equals(adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_UPPER_AND_LOWER_CASE, "true"));
		boolean passwordReqDigit = "true".equals(adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_DIGIT, "true"));
		boolean passwordReqNonDigit = "true".equals(adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_REQUIRES_NON_DIGIT, "true"));
		String passwordReqRegex = adminService.getGlobalProperty(OpenmrsConstants.GP_PASSWORD_CUSTOM_REGEX, "");

		String pattern = "";
		String patternErrorMessage = "";

		if (passwordReqUpperAndLowerCase || passwordReqDigit || passwordReqNonDigit || StringUtils.isNotEmpty(passwordReqRegex)) {
			pattern += "/^";
			patternErrorMessage += mss.getMessage("adminui.field.require.pattern.begin");
			if (passwordReqUpperAndLowerCase) {
				pattern += "(?=.*?[A-Z])(?=.*?[a-z])";
				patternErrorMessage += ' ' + mss.getMessage("adminui.field.require.pattern.reqUpperAndLowerCase") + ',';
			}
			if (passwordReqDigit) {
				pattern += "(?=.*\\d)";
				patternErrorMessage += ' ' + mss.getMessage("adminui.field.require.pattern.reqDigit") + ',';
			}
			if (passwordReqNonDigit) {
				pattern += "(?=.*[^\\d])";
				patternErrorMessage += ' ' + mss.getMessage("adminui.field.require.pattern.reqNonDigit") + ',';
			}
			if (StringUtils.isNotEmpty(passwordReqRegex)) {
				pattern += "(?=";
				pattern += passwordReqRegex;
				pattern += ')';
				patternErrorMessage += ' ' + mss.getMessage("adminui.field.require.pattern.reqRegex", new Object[]{passwordReqRegex}, Context.getLocale()) + ',';
			}
			pattern += "[\\w|\\W]*$/";
			patternErrorMessage = patternErrorMessage.substring(0, patternErrorMessage.length() - 1);
		}

		model.addAttribute("passwordMinLength", passwordMinLength);
		model.addAttribute("pattern", pattern);
		model.addAttribute("patternErrorMessage", patternErrorMessage);
	}
}
