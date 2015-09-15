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

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

public class ChangeDefaultsPageController {

    public void get(PageModel pageModel) {
        User user = Context.getAuthenticatedUser();
        Map<String, String> props = user.getUserProperties();
        UserDefaults userDefaults = new UserDefaults();
        userDefaults.setDefaultLocale(props.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE));
        userDefaults.setProficientLocales(props.get(OpenmrsConstants.USER_PROPERTY_PROFICIENT_LOCALES));
        pageModel.addAttribute("userDefaults", userDefaults);
        pageModel.addAttribute("locales", Context.getAdministrationService().getPresentationLocales());
    }

    public String post(PageModel model, @MethodParam("getUserDefaults") @BindParams UserDefaults userDefaults,
                       BindingResult errors,
                       @SpringBean("userService") UserService userService,
                       HttpServletRequest request) {

        //TODO do some validation
        try {
            User user = Context.getAuthenticatedUser();
            Map<String, String> props = user.getUserProperties();
            props.put(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE, userDefaults.getDefaultLocale());
            props.put(OpenmrsConstants.USER_PROPERTY_PROFICIENT_LOCALES, userDefaults.getProficientLocales());
            user.setUserProperties(props);
            userService.saveUser(user, null);
            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.account.defaults.success");
        } catch (Exception ex) {
            request.getSession().setAttribute(
                    UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.account.defaults.fail");
            return "account/changeDefaults";
        }

        model.addAttribute("userDefaults", userDefaults);
        model.addAttribute("locales", Context.getAdministrationService().getPresentationLocales());
        return "myaccount/myAccount";
    }
    
    public UserDefaults getUserDefaults(@RequestParam(value = "defaultLocale", required = false) String defaultLocale,
    		@RequestParam(value = "proficientLocales", required = false) String proficientLocales) {
    	
    	return new UserDefaults(defaultLocale, proficientLocales);
    }

    public class UserDefaults {

        private String defaultLocale;
        private String proficientLocales = "";

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
