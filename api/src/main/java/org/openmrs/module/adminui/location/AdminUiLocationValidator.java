/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.adminui.location;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.annotation.Handler;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Handler(supports = {Location.class}, order = 50)
public class AdminUiLocationValidator implements Validator {
	
	/**
	 * Log for this class and subclasses
	 */
	protected final Log log = LogFactory.getLog(getClass());
	
	 /**
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
	/**
	 * Determines if the command object being submitted is a valid type
	 *
	 * @see org.springframework.validation.Validator#supports(Class)
	 */
	
	public boolean supports(Class<?> c) {
		if (log.isDebugEnabled()) {
			log.debug(this.getClass().getName() + ".supports: " + c.getName());
		}

		return c.equals(Location.class);
	} 
	
	public void validate(Object obj, Errors errors) {
		if (log.isDebugEnabled()) {
			log.debug(this.getClass().getName() + ".validate...");
		}

		if (obj == null || !(obj instanceof Location)) {
			throw new IllegalArgumentException(
					"The parameter obj should not be null and must be of type " + Location.class);
		}

		Location location = (Location) obj;
		validateNameField(errors, location);
		validateDescriptionField(errors, location.getDescription());
	}
	
	private void validateNameField(Errors errors, Location location) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "error.name");
		if (verifyIfNameHasMoreThan100Characters(location.getName())) {//TODO adjust to not longer than 255
			errors.rejectValue("name", "adminui.location.longName.errorMessage");
		}
	}

	private boolean verifyIfNameHasMoreThan100Characters(String locationName) {
		if (locationName != null) {
			return (locationName.length() > 100) ? true : false;
		}
		return false;
	}

	private void validateDescriptionField(Errors errors, String description) {
		if (verifyIfDescriptionHasMoreThan1024Characters(description)) {
			errors.rejectValue("description", "adminui.location.description.errorMessage");
		}
	}

	private boolean verifyIfDescriptionHasMoreThan1024Characters(String description) {
		if (description != null) {
			return (description.length() > 1024) ? true : false;
		}
		return false;
	}

}
