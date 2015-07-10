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

package org.openmrs.module.adminui.page.controller.metadata.locations;

import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationTagValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class LocationTagPageController {

    public void get(PageModel model, @RequestParam(value = "locationTagId", required = false) Integer locationTagId,
                    @SpringBean("locationService") LocationService locationService) {

        LocationTag locationTag = new LocationTag();
        if (locationTagId != null) {
            locationTag = locationService.getLocationTag(Integer.valueOf(locationTagId));
        }

        model.addAttribute("locationTag", locationTag);
    }

    public String post(PageModel model, @ModelAttribute("locationTag") @BindParams LocationTag locationTag,
                       BindingResult errors,
                       @SpringBean("locationService") LocationService locationService,
                       @SpringBean("locationTagValidator") LocationTagValidator locationTagValidator,
                       @RequestParam(required = false, value = "save") String saveFlag,
                       @RequestParam(required = false, value = "retire") String retireFlag,
                       HttpServletRequest request) {

        locationTagValidator.validate(locationTag, errors);

        if (!errors.hasErrors()) {
            try {
                if (saveFlag.length() > 3) {
                    locationService.saveLocationTag(locationTag);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationTag.saved");
                } else if (retireFlag.length() > 3) {
                    String reason = request.getParameter("retireReason");
                    locationService.retireLocationTag(locationTag, reason);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationTag.retired");
                }
                return "redirect:/adminui/metadata/locations/manageLocationTags.page";
            } catch (Exception e) {
                request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
            }
        } else {

        }

        model.addAttribute("errors", errors);
        model.addAttribute("locationTag", locationTag);

        return "metadata/locations/locationTag";
    }
}
