/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.metadata.privileges;

import javax.servlet.http.HttpServletRequest;

//import org.openmrs.LocationTag;
//import org.openmrs.api.LocationService;
import org.openmrs.Privilege;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.UserService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.validator.PrivilegeValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestParam;

public class ManagePrivilegesPageController {

    protected final Log log = LogFactory.getLog(getClass());
	
	/**
	 * @param model
	 * @param userService
	 */
	public void get(PageModel model, @SpringBean("userService") UserService userService) {
		model.addAttribute("privileges", userService.getAllPrivileges());
	}


    public String post(PageModel model, 
            		   @RequestParam(value = "privilegeName", required = false) @BindParams Privilege privilege,
                       @RequestParam("action") String action,
                       @RequestParam(value = "reason", required = false) String reason,
                       @SpringBean("userService") UserService userService, HttpServletRequest request) {

    	Errors errors = new BeanPropertyBindingResult(privilege, "privilege");
        new PrivilegeValidator().validate(privilege, errors);

        try {
            if ("retire".equals(action)) {
                privilege.setRetired(true);
                privilege.setRetireReason(request.getParameter("reason"));
            	userService.savePrivilege(privilege);
            } else if ("restore".equals(action)) {
                privilege.setRetired(false);
            	userService.savePrivilege(privilege);
            } else if ("purge".equals(action)) {
            	userService.purgePrivilege(privilege);
            }
            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.privilege." + action + ".success");
            return "redirect:/adminui/metadata/privileges/managePrivileges.page";
        } catch (Exception e) {
            log.error("Failed to " + action + " privilege:", e);
        }

        request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                "adminui.privilege." + action + ".fail");
        model.addAttribute("errors", errors);
        model.addAttribute("privileges", userService.getAllPrivileges());

        return "metadata/privileges/managePrivileges";
    }

}
