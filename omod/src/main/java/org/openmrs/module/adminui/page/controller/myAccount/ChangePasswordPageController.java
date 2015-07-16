/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.page.controller.myaccount;

import org.openmrs.User;
import org.openmrs.api.PasswordException;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.DAOException;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsConstants;
import org.openmrs.util.OpenmrsUtil;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public class ChangePasswordPageController {

    public void get(PageModel model) {
        setModelAttributes(model);
    }

    public void setModelAttributes(PageModel model) {
        model.addAttribute("passwordMinLength",
                Context.getAdministrationService().getGlobalProperty(OpenmrsConstants.GP_PASSWORD_MINIMUM_LENGTH, "8"));
    }

    public String post(PageModel model, @BindParams ChangePassword changePassword, BindingResult errors,
                       @SpringBean("userService") UserService userService, HttpServletRequest request) {

        User user = Context.getAuthenticatedUser();
        try {
            OpenmrsUtil.validatePassword(user.getUsername(), changePassword.getNewPassword(), user.getSystemId());
        } catch (PasswordException e) {
            errors.reject(e.getMessage());
        }

        if (errors.hasErrors()) {
            model.addAttribute("errors", errors);
            setModelAttributes(model);
            return "myaccount/changePassword";
        } else {
            try {
                userService.changePassword(changePassword.getOldPassword(), changePassword.getNewPassword());
                InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "emr.account.changePassword.success");
                return "myaccount/myAccount";
            } catch (DAOException e) {
                request.getSession().setAttribute(
                        UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "adminui.account.changePassword.fail");

                setModelAttributes(model);
                return "myaccount/changePassword";
            }
        }
    }

    private class ChangePassword {

        private final String oldPassword;

        private final String newPassword;

        private final String confirmPassword;

        public ChangePassword(String oldPassword, String newPassword, String confirmPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
            this.confirmPassword = confirmPassword;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public String getOldPassword() {
            return oldPassword;
        }
    }
}
