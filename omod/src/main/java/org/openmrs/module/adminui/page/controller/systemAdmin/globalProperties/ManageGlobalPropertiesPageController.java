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
		//TODO Remove module global properties
		model.addAttribute("globalProperties", globalProps);
	}
}
