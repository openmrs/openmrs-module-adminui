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
