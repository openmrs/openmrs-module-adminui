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

package org.openmrs.module.adminui.account;


/**
 * Implementers can provide implementations of this interface if they wish
 * to automatically create provider identifiers; custom implementations should
 * be injected into the AccountService;
 * <p/>
 * When saving an Account, if there is an associated Provider and that Provider
 * does not have an identifier, the generateIdentifier method will be called
 * and the result set as the provider.identifier
 * <p/>
 * See MirebalaisProviderIdentifierGenerator for an example implementation; check
 * out the MirebalaisHospitalActivator to see how this generator is set on the
 * AccountService
 */
public class ProviderIdentifierGenerator {

    public String generateIdentifier() {
    	long a = Math.round(Math.random()*1000);
    	long b = Math.round(Math.random()*10);
    	return a+"-"+b;
    }

}