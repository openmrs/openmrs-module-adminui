package org.openmrs.module.adminui.account;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openmrs.Person;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.adminui.EmrApiConstants;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.springframework.transaction.annotation.Transactional;


@Transactional
public class AccountServiceImpl extends BaseOpenmrsService implements AccountService {

    private UserService userService;

    private PersonService personService;

    private ProviderService providerService;

    private ProviderManagementService providerManagementService;

    //private EmrApiProperties emrApiProperties;

    //private ProviderIdentifierGenerator providerIdentifierGenerator = null;
    
    /**
     * @param userService the userService to set
     */
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * @param providerService
     */
    public void setProviderService(ProviderService providerService) {
        this.providerService = providerService;
    }

    /**
     * @param providerManagementService
     */
    public void setProviderManagementService(ProviderManagementService providerManagementService) {
        this.providerManagementService = providerManagementService;
    }

    /**
     * @param personService the personService to set
     */
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
    
    /**
     * @see org.openmrs.module.adminui.account.AccountService#saveAccount(org.openmrs.module.adminui.account.AccountDomainWrapper)
     */
    @Override
    @Transactional
    public void saveAccount(AccountDomainWrapper account) {
        account.save();
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AccountDomainWrapper> getAllAccounts() {

        Map<Person, AccountDomainWrapper> byPerson = new LinkedHashMap<Person, AccountDomainWrapper>();

        for (User user : userService.getAllUsers()) {
            //exclude daemon user
            if (EmrApiConstants.DAEMON_USER_UUID.equals(user.getUuid()))
                continue;

            if (!user.getPerson().isVoided()) {
                byPerson.put(user.getPerson(), new AccountDomainWrapper(user.getPerson(), this, userService,
                        providerService, providerManagementService, personService));
            }
        }

        /*for (Provider provider : providerService.getAllProviders()) {

            // skip the baked-in unknown provider
            if (provider.equals(emrApiProperties.getUnknownProvider())) {
                continue;
            }

            if (provider.getPerson() == null)
                throw new APIException("Providers not associated to a person are not supported");

            AccountDomainWrapper account = byPerson.get(provider.getPerson());
            if (account == null && !provider.getPerson().isVoided()) {
                byPerson.put(provider.getPerson(), new AccountDomainWrapper(provider.getPerson(), this, userService,
                        providerService, providerManagementService, personService, providerIdentifierGenerator));
            }
        }*/

        List<AccountDomainWrapper> accounts = new ArrayList<AccountDomainWrapper>();
        for (AccountDomainWrapper account : byPerson.values()) {
            accounts.add(account);
        }

        return accounts;
    }
    
    /**
     * @see org.openmrs.module.adminui.account.AccountService#getAllCapabilities()
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllCapabilities() {
        List<Role> capabilities = new ArrayList<Role>();
        for (Role candidate : userService.getAllRoles()) {
            if (candidate.getName().startsWith(EmrApiConstants.ROLE_PREFIX_CAPABILITY))
                capabilities.add(candidate);
        }
        return capabilities;
    }

    /**
     * @see org.openmrs.module.adminui.account.AccountService#getAllPrivilegeLevels()
     */
    @Override
    @Transactional(readOnly = true)
    public List<Role> getAllPrivilegeLevels() {
        List<Role> privilegeLevels = new ArrayList<Role>();
        for (Role candidate : userService.getAllRoles()) {
            if (candidate.getName().startsWith(EmrApiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL))
                privilegeLevels.add(candidate);
        }
        return privilegeLevels;
    }
    
    /**
     * @see org.openmrs.module.adminui.account.AccountService#getAccountByPerson(org.openmrs.Person)
     */
    @Override
    @Transactional(readOnly = true)
    public AccountDomainWrapper getAccountByPerson(Person person) {
        return new AccountDomainWrapper(person, this, userService,
                providerService, providerManagementService, personService);
    }

}