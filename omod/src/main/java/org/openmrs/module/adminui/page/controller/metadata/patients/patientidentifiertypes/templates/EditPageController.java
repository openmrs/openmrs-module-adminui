/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.metadata.patients.patientidentifiertypes.templates;

import java.util.Collection;

import org.openmrs.PatientIdentifierType.LocationBehavior;
import org.openmrs.api.PatientService;
import org.openmrs.patient.IdentifierValidator;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class EditPageController {

	public void get(PageModel model, @SpringBean("patientService") PatientService patientService) {
		Collection<IdentifierValidator> pivs = patientService.getAllIdentifierValidators();
    	model.addAttribute("validators", pivs);
    	model.addAttribute("uniquenessBehaviors", UniquenessBehavior.values());
    	model.addAttribute("locationBehaviors", LocationBehavior.values());
    }
	
	/**
	 * Enumeration for the way to handle uniqueness among identifiers for a given identifier type
	 */
	public enum UniquenessBehavior {
		
		/**
		 * Indicates that identifiers should be globally unique
		 */
		UNIQUE,
		
		/**
		 * Indicates that duplicates identifiers are allowed
		 */
		NON_UNIQUE,
		
		/**
		 * Indicates that identifiers should be unique only across a location if the identifier's
		 * location property is not null
		 */
		LOCATION
	}
}
