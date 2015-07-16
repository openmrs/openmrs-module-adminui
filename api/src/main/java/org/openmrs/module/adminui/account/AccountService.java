/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.adminui.account;

import org.openmrs.Role;
import org.openmrs.Person;
import java.util.List;

public interface AccountService {

	 /**
     * Save the account details to the database
     *
     * @param account
     * @return
     */
    void saveAccount(AccountDomainWrapper account);
    
    /**
     * @return
     * @should get all unique accounts
     */
    List<AccountDomainWrapper> getAllAccounts();
    
    /**
     * Gets all Capabilities, i.e roles with the {@link org.openmrs.module.adminui.AdminUiConstants#ROLE_PREFIX_CAPABILITY} prefix
     *
     * @return a list of Roles
     * @should return all roles with the capability prefix
     */
    List<Role> getAllCapabilities();

    /**
     * Gets all Privilege Levels, i.e roles with the
     *
     * @return a list of Roles
     * @should return all roles with the privilege level prefix
     */
    List<Role> getAllPrivilegeLevels();
    
    /**
     * Gets an account for the Specified person object
     *
     * @return
     * @should return the account for the specified person if they are associated to a user
     * @should return the account for the specified person if they are associated to a provider
     */
    AccountDomainWrapper getAccountByPerson(Person person);

}