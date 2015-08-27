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

import java.util.List;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;

public class ManageGlobalPropertiesPageController {
	
	public void get(PageModel model) {
		
		AdministrationService administrationService = Context.getAdministrationService();
		List<GlobalProperty> globalProps = administrationService.getAllGlobalProperties();
		
		//remove those with the .started and .mandatory suffixes
		for (int index = 0; index < globalProps.size(); index++) {
			String property = globalProps.get(index).getProperty();
			if (property.endsWith(".started") || property.endsWith("mandatory")) {
				globalProps.remove(index);
				index -= 1;
			}
		}
		
		//TODO Remove module global properties
		model.addAttribute("globalProperties", globalProps);
	}
}
