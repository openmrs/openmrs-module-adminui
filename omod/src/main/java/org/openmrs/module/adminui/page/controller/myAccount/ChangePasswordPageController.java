/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 * <p/>
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * <p/>
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
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
