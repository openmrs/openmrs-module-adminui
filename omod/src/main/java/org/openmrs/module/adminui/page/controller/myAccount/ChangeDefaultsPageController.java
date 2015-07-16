/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.adminui.page.controller.myaccount;

import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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

    public String post(PageModel model, @BindParams UserDefaults userDefaults,
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
