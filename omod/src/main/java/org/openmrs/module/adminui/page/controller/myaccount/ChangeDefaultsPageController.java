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

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.User;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.LocaleUtility;
import org.openmrs.util.OpenmrsConstants;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

public class ChangeDefaultsPageController {

    public void get(PageModel pageModel) {
        User user = Context.getAuthenticatedUser();
        Map<String, String> props = user.getUserProperties();
        UserDefaults userDefaults = new UserDefaults();
        userDefaults.setDefaultLocale(props.get(OpenmrsConstants.USER_PROPERTY_DEFAULT_LOCALE));
        userDefaults.setProficientLocales(props.get(OpenmrsConstants.USER_PROPERTY_PROFICIENT_LOCALES));
        pageModel.addAttribute("userDefaults", userDefaults);
        pageModel.addAttribute("primaryLocales", Context.getAdministrationService().getPresentationLocales());
        pageModel.addAttribute("proficientLocales", Context.getAdministrationService().getAllowedLocales());
    }

    public String post(PageModel model, @MethodParam("getUserDefaults") @BindParams UserDefaults userDefaults,
                       BindingResult errors, UiUtils ui,
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

            // set the locale based on the locale selected by user
            Locale newLocale = normalizeLocale(userDefaults.getDefaultLocale());
            if (newLocale != null) {
                Context.getUserContext().setLocale(newLocale);
                new CookieLocaleResolver().setDefaultLocale(newLocale);
            }

            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.account.defaults.success");
        } catch (Exception ex) {
            request.getSession().setAttribute(
                    UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.account.defaults.fail");
            return "redirect:" + ui.pageLink("adminui", "myaccount/changeDefaults");
        }
        return "redirect:" + ui.pageLink("adminui", "myaccount/myAccount");
    }
    
    public UserDefaults getUserDefaults(@RequestParam(value = "defaultLocale", required = false) String defaultLocale,
    		@RequestParam(value = "proficientLocales", required = false) String proficientLocales) {
    	
    	return new UserDefaults(defaultLocale, proficientLocales);
    }

    public static Locale normalizeLocale(String localeString) {
        if (localeString == null) {
            return null;
        } else {
            localeString = localeString.trim();
            if (localeString.isEmpty()) {
                return null;
            } else {
                int len = localeString.length();

                for (int locale = 0; locale < len; ++locale) {
                    char c = localeString.charAt(locale);
                    if ((c <= 32 || c >= 127 || (c >= 32 || c <= 127) && !Character.isLetter(c) && c != 95) && c != 9) {
                        localeString = localeString.replaceFirst(Character.valueOf(c).toString(), "");
                        --len;
                        --locale;
                    }
                }

                Locale var4 = LocaleUtility.fromSpecification(localeString);
                if (LocaleUtility.isValid(var4)) {
                    return var4;
                } else {
                    return null;
                }
            }
        }
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
