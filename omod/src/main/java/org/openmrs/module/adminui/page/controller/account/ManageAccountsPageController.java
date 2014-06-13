package org.openmrs.module.admintoolsui.page.controller.account;

import org.openmrs.module.emrapi.account.AccountService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class ManageAccountsPageController {

    public void get(PageModel model, @SpringBean("accountService") AccountService accountService) {
        model.addAttribute("accounts", accountService.getAllAccounts());
    }

}
