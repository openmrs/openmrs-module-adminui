package org.openmrs.module.adminui.page.controller.systemAdmin;

import java.util.List;

import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.ui.framework.page.PageModel;

public class GlobalPropsPageController {
	
	public void get(PageModel model) {
		
		AdministrationService administrationService = Context.getAdministrationService();
		List<GlobalProperty> globalProps = administrationService.getAllGlobalProperties();
		model.addAttribute("globalProperties", globalProps);
    }
}
