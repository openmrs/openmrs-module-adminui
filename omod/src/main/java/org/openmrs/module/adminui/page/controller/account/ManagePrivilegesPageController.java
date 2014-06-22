package org.openmrs.module.adminui.page.controller.account;

import org.openmrs.module.adminui.account.AccountService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class ManagePrivilegesPageController {
	
	public void get(PageModel model, @SpringBean("accountService") AccountService accountService) {
		model.addAttribute("privilegeLevels", accountService.getAllPrivilegeLevels());
    }

}
