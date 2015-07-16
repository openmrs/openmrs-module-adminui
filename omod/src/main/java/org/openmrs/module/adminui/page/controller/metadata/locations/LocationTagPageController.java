/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.metadata.locations;

import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.module.uicommons.UiCommonsConstants;
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
                    request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationTag.saved");
                } else if (retireFlag.length() > 3) {
                    String reason = request.getParameter("retireReason");
                    locationService.retireLocationTag(locationTag, reason);
                    request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationTag.retired");
                }
                return "redirect:/adminui/metadata/locations/manageLocationTags.page";
            } catch (Exception e) {
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
            }
        } else {

        }

        model.addAttribute("errors", errors);
        model.addAttribute("locationTag", locationTag);

        return "metadata/locations/locationTag";
    }
}
