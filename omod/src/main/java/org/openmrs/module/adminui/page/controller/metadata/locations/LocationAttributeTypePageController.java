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

import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationAttributeTypeValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public class LocationAttributeTypePageController {

    public void get(PageModel model, @RequestParam(value = "locationAttributeTypeId", required = false) Integer locationAttributeTypeId,
                    @SpringBean("locationService") LocationService locationService) {

        LocationAttributeType locationAttributeType = new LocationAttributeType();
        if (locationAttributeTypeId != null) {
            locationAttributeType = locationService.getLocationAttributeType(Integer.valueOf(locationAttributeTypeId));
        }

        model.addAttribute("locationAttributeType", locationAttributeType);
        model.addAttribute("datatypes", getDatatypes());
        model.addAttribute("handlers", getHandlers());
    }

    public String post(PageModel model, @ModelAttribute("locationAttributeType") @BindParams LocationAttributeType locationAttributeType,
                       BindingResult errors,
                       @SpringBean("locationService") LocationService locationService,
                       @SpringBean("locationAttributeTypeValidator") LocationAttributeTypeValidator locationAttributeTypeValidator,
                       @RequestParam(required = false, value = "save") String saveFlag,
                       @RequestParam(required = false, value = "retire") String retireFlag,
                       @RequestParam(required = false, value = "purge") String purgeFlag,
                       HttpServletRequest request) {

        Errors newErrors = new BindException(locationAttributeType, "locationAttributeType");
        locationAttributeTypeValidator.validate(locationAttributeType, newErrors);

        if (!newErrors.hasErrors()) {
            try {
                if (saveFlag.length() > 3) {
                    locationService.saveLocationAttributeType(locationAttributeType);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationAttributeType.saved");
                } else if (retireFlag.length() > 3) {
                    String reason = request.getParameter("retireReason");
                    locationService.retireLocationAttributeType(locationAttributeType, reason);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationAttributeType.retired");
                } else if (purgeFlag.length() > 3) {
                    locationService.purgeLocationAttributeType(locationAttributeType);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.locationAttributeType.purged");
                }
                return "redirect:/adminui/metadata/locations/manageLocationAttributeTypes.page";
            } catch (Exception e) {
                request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.save.fail");
            }
        } else {
        }

        model.addAttribute("errors", newErrors);
        model.addAttribute("locationAttributeType", locationAttributeType);
        model.addAttribute("datatypes", getDatatypes());
        model.addAttribute("handlers", getHandlers());

        return "metadata/locations/locationAttributeType";
    }

    public Collection<String> getDatatypes() {
        return CustomDatatypeUtil.getDatatypeClassnames();
    }

    public Collection<String> getHandlers() {
        return CustomDatatypeUtil.getHandlerClassnames();
    }
}