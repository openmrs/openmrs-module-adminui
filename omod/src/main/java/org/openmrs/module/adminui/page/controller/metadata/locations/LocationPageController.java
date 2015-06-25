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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.adminui.AdminUiConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationValidator;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LocationPageController {

    protected final Log log = LogFactory.getLog(getClass());

    /**
     * @param model
     * @param locationId
     * @param locationService
     */
    public void get(PageModel model, @RequestParam(value = "locationId", required = false) Integer locationId,
                    @SpringBean("locationService") LocationService locationService) {

        Location location = new Location();
        if (locationId != null) {
            location = locationService.getLocation(Integer.valueOf(locationId));
        }

        model.addAttribute("location", location);
        model.addAttribute("existingLocations", locationService.getAllLocations());
        model.addAttribute("locationTags", locationService.getAllLocationTags());
        model.addAttribute("attributeTypes", locationService.getAllLocationAttributeTypes());
    }

    /**
     *
     * @param model
     * @param location
     * @param errors
     * @param locationService
     * @param locationValidator
     * @param messageSource
     * @param messageSourceService
     * @param request
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String post(PageModel model, @ModelAttribute("location") @BindParams Location location,
                       BindingResult errors,
                       @SpringBean("locationService") LocationService locationService,
                       @SpringBean("locationValidator") LocationValidator locationValidator,
                       @SpringBean("messageSource") MessageSource messageSource,
                       @RequestParam(required = false, value = "save") String saveFlag,
                       @RequestParam(required = false, value = "retire") String retireFlag,
                       HttpServletRequest request) {

        Errors newErrors = new BindException(location, "location");
        locationValidator.validate(location, newErrors);

        String[] locationTags = request.getParameterValues("locTags");
        Set<LocationTag> tags = new HashSet<LocationTag>();

        if (locationTags != null && locationTags.length > 0) {
            for (String x : locationTags) {
                LocationTag tag = locationService.getLocationTagByName(x);
                tags.add(tag);
            }
            location.setTags(tags);
        }

        LocationAttribute attr = new LocationAttribute();
        List<LocationAttributeType> locationAttributeList = locationService.getAllLocationAttributeTypes();
        for (LocationAttributeType locAttr : locationAttributeList) {
            attr.setLocationAttributeId(locAttr.getId());
            attr.setValue(request.getParameter("attribute." + locAttr.getId()));
            attr.setLocation(location);
        }

        if (!newErrors.hasErrors()) {
            try {
                if (saveFlag.length() > 3) {
                    locationService.saveLocation(location);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.location.saved");
                } else if (retireFlag.length() > 3) {
                    String reason = request.getParameter("retireReason");
                    locationService.retireLocation(location, reason);
                    request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.location.retired");
                }
                return "redirect:/adminui/metadata/locations/manageLocations.page";
            } catch (Exception e) {
                log.warn("Some error occurred while saving location details:", e);
                request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                        "adminui.save.fail");
            }
        } else {
            sendErrorMessage(errors, messageSource, request);
        }

        model.addAttribute("errors", newErrors);
        model.addAttribute("location", location);

        return "metadata/locations/location";

    }

    private void sendErrorMessage(BindingResult errors, MessageSource messageSource, HttpServletRequest request) {
        List<ObjectError> allErrors = errors.getAllErrors();
        String message = getMessageErrors(messageSource, allErrors);
        request.getSession().setAttribute(AdminUiConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                message);
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
}