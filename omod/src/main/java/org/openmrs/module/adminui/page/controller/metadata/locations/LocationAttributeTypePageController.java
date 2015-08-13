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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.ValidateUtil;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.RequestParam;

public class LocationAttributeTypePageController {
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public void get(PageModel model,
	                @RequestParam(value = "locationAttributeTypeId", required = false) LocationAttributeType locationAttributeType) {
		
		if (locationAttributeType == null) {
			locationAttributeType = new LocationAttributeType();
		}
		
		model.addAttribute("locationAttributeType", locationAttributeType);
		model.addAttribute("datatypesMap", getDatatypes());
		model.addAttribute("handlersMap", getHandlers());
	}
	
	public String post(PageModel model,
	                   @RequestParam(value = "locationAttributeTypeId", required = false) @BindParams LocationAttributeType locationAttributeType,
	                   @SpringBean("locationService") LocationService locationService, HttpServletRequest request) {
		
		Errors errors = new BeanPropertyBindingResult(locationAttributeType, "locationAttributeType");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "adminui.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "datatypeClassname", "adminui.field.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "minOccurs", "adminui.field.required");
		if (locationAttributeType.getMinOccurs() == null) {
			locationAttributeType.setMinOccurs(0);
		}
		
		if (!errors.hasErrors()) {
			ValidateUtil.validate(locationAttributeType, errors);
		}
		
		if (!errors.hasErrors()) {
			try {
				locationService.saveLocationAttributeType(locationAttributeType);
				InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.locationAttributeType.save.success");
				return "redirect:/adminui/metadata/locations/manageLocationAttributeTypes.page";
			}
			catch (Exception e) {
				log.error("Failed to save location attribute type:", e);
				request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
				    "adminui.locationAttributeType.save.fail");
			}
		}
		
		model.addAttribute("errors", errors);
		model.addAttribute("locationAttributeType", locationAttributeType);
		model.addAttribute("datatypesMap", getDatatypes());
		model.addAttribute("handlersMap", getHandlers());
		
		return "metadata/locations/locationAttributeType";
	}
	
	public Map<String, String> getDatatypes() {
		Collection<String> datatypes = CustomDatatypeUtil.getDatatypeClassnames();
		Map<String, String> ret = new HashMap<String, String>();
		for (String dt : datatypes) {
			ret.put(dt, beautify(dt));
		}
		return ret;
	}
	
	public Map<String, String> getHandlers() {
		Collection<String> handlers = CustomDatatypeUtil.getHandlerClassnames();
		Map<String, String> ret = new HashMap<String, String>();
		for (String h : handlers) {
			ret.put(h, beautify(h));
		}
		return ret;
	}
	
	/**
	 * Beautifies a fully qualified java class name
	 */
	private String beautify(String input) {
		String classname = input.toString();
		classname = classname.substring(classname.lastIndexOf(".") + 1);
		String[] sections = StringUtils.splitByCharacterTypeCamelCase(classname);
		return StringUtils.join(sections, " ");
	}
}
