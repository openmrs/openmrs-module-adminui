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

import org.openmrs.api.UserService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

public class ChangeSecretQuestionPageController {

    public void get(PageModel model) {
        //TODO populate the model
        SecretQuestion secretQuestion = null;
        model.put("secretQuestion", secretQuestion);
    }

    public String post(PageModel model, @BindParams SecretQuestion secretQuestion,
                       BindingResult errors,
                       @SpringBean("userService") UserService userService,
                       HttpServletRequest request) {

        if (!secretQuestion.getAnswer().equals(secretQuestion.getConfirmAnswer())) {
            model.put("secretQuestion", secretQuestion);
            errors.rejectValue("confirmAnswer", "adminui.account.answerAndConfirmAnswer.doesNotMatch");
            return "myaccount/changeSecretQuestion";
        }

        return changeSecretQuestion(secretQuestion, userService, request);
    }

    private String changeSecretQuestion(SecretQuestion secretQuestion, UserService userService, HttpServletRequest request) {
        try {
            userService.changeQuestionAnswer(secretQuestion.getPassword(), secretQuestion.getQuestion(), secretQuestion.getAnswer());
            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), "adminui.account.secretQuestion.success");
        } catch (Exception ex) {
            request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE, "registrationapp.save.fail");
            return "myaccount/changeSecretQuestion";
        }
        return "myaccount/myAccount";
    }

    public class SecretQuestion {

        private String password;
        private String question;
        private String answer;
        private String confirmAnswer;

        public SecretQuestion(String password, String question, String answer, String confirmAnswer) {
            this.password = password;
            this.question = question;
            this.answer = answer;
            this.confirmAnswer = confirmAnswer;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getConfirmAnswer() {
            return confirmAnswer;
        }

        public void setConfirmAnswer(String confirmAnswer) {
            this.confirmAnswer = confirmAnswer;
        }

        @Override
        public String toString() {
            return "Question=" + question + ", Answer=" + answer;
        }
    }
}
