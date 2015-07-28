/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.systemadmin.globalproperties;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class GlobalPropertyPageController {

    public void get(PageModel model, @RequestParam(value = "globalProperty", required = false) String globalPropertyName) {

        AdministrationService administrationService = Context.getAdministrationService();
        GlobalProperty globalProperty = new GlobalProperty();
        if (globalPropertyName != null) {
            globalProperty = administrationService.getGlobalPropertyObject(globalPropertyName);
        }

        model.addAttribute("globalProperty", globalProperty);
    }

    public String post(PageModel model, @ModelAttribute("globalProperty") @BindParams GlobalProperty globalProperty,
                       BindingResult errors, @RequestParam(required = false, value = "save") String saveFlag,
                       @RequestParam(required = false, value = "purge") String purgeFlag, HttpServletRequest request) {

        AdministrationService administrationService = Context.getAdministrationService();
        Errors newErrors = new BindException(globalProperty, "globalProperty");

        if (!newErrors.hasErrors()) {
            try {
                if (saveFlag.length() > 3) {
                    administrationService.saveGlobalProperty(globalProperty);
                    InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.globalProperty.saved");
                } else if (purgeFlag.length() > 3) {
                    administrationService.purgeGlobalProperty(globalProperty);
                    InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.globalProperty.purged");
                }
                return "redirect:/adminqui/systemadmin/globalproperties/manageGlobalProperties.page";
            } catch (Exception e) {
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
            }
        }

        model.addAttribute("errors", newErrors);
        model.addAttribute("globalProperty", globalProperty);

        return "systemadmin/globalproperties/globalProperty";
    }

}
