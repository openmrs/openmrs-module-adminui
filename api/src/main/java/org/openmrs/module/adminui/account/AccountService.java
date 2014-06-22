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
     * Gets all Capabilities, i.e roles with the {@link org.openmrs.module.emrapi.EmrApiConstants#ROLE_PREFIX_CAPABILITY} prefix
     *
     * @return a list of Roles
     * @should return all roles with the capability prefix
     */
    List<Role> getAllCapabilities();

    /**
     * Gets all Privilege Levels, i.e roles with the
     * {@link org.openmrs.module.emrapi.EmrApiConstants#ROLE_PREFIX_PRIVILEGE_LEVEL} prefix
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