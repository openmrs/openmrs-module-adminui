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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.LocationTag;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.LocationValidator;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
    public void get(PageModel model, @RequestParam(value = "locationId", required = false) Location location,
                    @SpringBean("locationService") LocationService locationService) {

        List<Location> locations = locationService.getAllLocations(false);
        
        //if editing a location, it should not be in the parent list
        if (location != null) {
        	locations.remove(location);
        }
        else {
        	location = new Location();
        }
        
        //remove descendant locations
        Set<Location> descendants = getDescendants(location.getChildLocations());
        for (Location loc : descendants) {
        	locations.remove(loc);
        }

        model.addAttribute("location", location);
        model.addAttribute("existingLocations", locations);
        model.addAttribute("locationTags", locationService.getAllLocationTags());
        model.addAttribute("attributeTypes", locationService.getAllLocationAttributeTypes());
    }
    
    private Set<Location> getDescendants(Set<Location> childLocations) {
    	Set<Location> locations = new HashSet<Location>();
    	
    	if (childLocations != null) {
    		locations.addAll(childLocations);
	    	for (Location location : childLocations) {
	    		locations.addAll(getDescendants(location.getChildLocations()));
	    	}
    	}
    	
    	return locations;
    }

    /**
     * @param model
     * @param location
     * @param errors
     * @param locationService
     * @param locationValidator
     * @param messageSource
     * @param request
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String post(PageModel model, @RequestParam(value = "locationId", required = false) @BindParams Location location,
                       BindingResult errors,
                       @SpringBean("locationService") LocationService locationService,
                       @SpringBean("locationValidator") LocationValidator locationValidator,
                       @SpringBean("messageSource") MessageSource messageSource,
                       @RequestParam(required = false, value = "save") String saveFlag,
                       @RequestParam(required = false, value = "retire") String retireFlag,
                       HttpServletRequest request) {

        locationValidator.validate(location, errors);
        String[] locationTags = request.getParameterValues("locTags");
        Set<LocationTag> tags = new HashSet<LocationTag>();
        if (!ArrayUtils.isEmpty(locationTags)) {
            for (String x : locationTags) {
                LocationTag tag = locationService.getLocationTag(Integer.valueOf(x));
                if (tag != null) {
                    tags.add(tag);
                }
            }
            location.setTags(tags);
        } else {
            tags = null;
        }
        location.setTags(tags);

        LocationAttribute attr = new LocationAttribute();
        List<LocationAttributeType> locationAttributeList = locationService.getAllLocationAttributeTypes();
        for (LocationAttributeType locAttr : locationAttributeList) {
            attr.setLocationAttributeId(locAttr.getId());
            attr.setValue(request.getParameter("attribute." + locAttr.getId()));
            attr.setLocation(location);
        }

        if (!errors.hasErrors()) {
            try {
                if (saveFlag.length() > 3) {
                    locationService.saveLocation(location);
                    request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.location.saved");
                } else if (retireFlag.length() > 3) {
                    String reason = request.getParameter("retireReason");
                    locationService.retireLocation(location, reason);
                    request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "adminui.location.retired");
                }
                return "redirect:/adminui/metadata/locations/manageLocations.page";
            } catch (Exception e) {
                log.warn("Some error occurred while saving location details:", e);
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                        "adminui.save.fail");
            }
        } else {
            sendErrorMessage(errors, messageSource, request);
        }

        model.addAttribute("errors", errors);
        model.addAttribute("location", location);
        model.addAttribute("existingLocations", locationService.getAllLocations());
        model.addAttribute("locationTags", locationService.getAllLocationTags());
        model.addAttribute("attributeTypes", locationService.getAllLocationAttributeTypes());

        return "metadata/locations/location";

    }

    private void sendErrorMessage(BindingResult errors, MessageSource messageSource, HttpServletRequest request) {
        List<ObjectError> allErrors = errors.getAllErrors();
        String message = getMessageErrors(messageSource, allErrors);
        request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
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