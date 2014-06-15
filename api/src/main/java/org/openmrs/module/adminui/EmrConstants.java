/*
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

package org.openmrs.module.adminui;

public class EmrConstants {

    public static final String EMR_MODULE_ID = "emr";

    public static final String GP_TRIAGE_ENCOUNTER_TYPE = "emr.triageEncounterType";

    public static final String COOKIE_NAME_LAST_SESSION_LOCATION = "emr.lastSessionLocation";

    public static final String SESSION_ATTRIBUTE_ERROR_MESSAGE = "emr.errorMessage";

    public static final String SESSION_ATTRIBUTE_INFO_MESSAGE = "emr.infoMessage";

    public static final String SESSION_ATTRIBUTE_TOAST_MESSAGE = "emr.toastMessage";

    public static final String TASK_CLOSE_STALE_VISITS_NAME = "EMR module - Close Stale Visits";

    public static final String TASK_CLOSE_STALE_VISITS_DESCRIPTION = "Closes any open visits that are no longer active";

    public static final long TASK_CLOSE_STALE_VISITS_REPEAT_INTERVAL = 5 * 60; // 5 minutes

    public static final String PAYMENT_AMOUNT_CONCEPT = "emr.paymentAmountConcept";
    public static final String PAYMENT_REASON_CONCEPT = "emr.paymentReasonConcept";
    public static final String PAYMENT_RECEIPT_NUMBER_CONCEPT = "emr.paymentReceiptNumberConcept";
    public static final String PAYMENT_CONSTRUCT_CONCEPT = "emr.paymentConstructConcept";

    // codes in the concept source provided by this module

}