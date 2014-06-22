package org.openmrs.module.adminui.account;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.api.PersonService;
import org.openmrs.api.ProviderService;
import org.openmrs.api.UserService;
import org.openmrs.module.adminui.EmrApiConstants;
import org.openmrs.module.adminui.account.AccountService;
//import org.openmrs.module.adminui.account.ProviderIdentifierGenerator;
import org.openmrs.module.providermanagement.Provider;
import org.openmrs.module.providermanagement.ProviderRole;
import org.openmrs.module.providermanagement.api.ProviderManagementService;
import org.openmrs.util.OpenmrsConstants;

public class AccountDomainWrapper{

    private Person person;

    private User user;

    private Provider provider;

    private String password;

    private String confirmPassword;

    private AccountService accountService;

    private UserService userService;

    private PersonService personService;

    private ProviderService providerService;

    private ProviderManagementService providerManagementService;
    
    //private ProviderIdentifierGenerator providerIdentifierGenerator;
    
    //private ProviderRole providerRole;

    public AccountDomainWrapper(Person person, AccountService accountService, UserService userService, ProviderService providerService,
                                ProviderManagementService providerManagementService, PersonService personService
                                /*ProviderIdentifierGenerator providerIdentifierGenerator*/) {
        this.accountService = accountService;
        this.userService = userService;
        this.providerService = providerService;
        this.providerManagementService = providerManagementService;
        this.personService = personService;
        //this.providerIdentifierGenerator = providerIdentifierGenerator;
        this.person = person;
        }
    
    public Person getPerson() {
        return person;
    }

    public User getUser() {
        return user;
    }

    public Provider getProvider() {
        return provider;
    }
    
    private void initializePersonNameIfNecessary() {
        if (person.getPersonName() == null) {
            person.addName(new PersonName());
        }
    }

    private void initializeUserIfNecessary() {
        if (user == null) {
            user = new User();
            user.setPerson(person);
        }
    }

    private void initializeProviderIfNecessary() {
        if (provider == null) {
            provider = new Provider();
            provider.setPerson(person);
        }
    }
    
    public void setProviderRole(ProviderRole providerRole) {

        if (providerRole != null) {
            initializeProviderIfNecessary();
            this.provider.setProviderRole(providerRole);
        } else {
            // this prevents us from creating a new provider if we are only setting the provider role to null
            if (this.provider != null) {
                provider.setProviderRole(null);
            }
        }
    }

    public ProviderRole getProviderRole() {
        return this.provider != null ? this.provider.getProviderRole() : null;
    }

    public void setGivenName(String givenName) {
        initializePersonNameIfNecessary();
        person.getPersonName().setGivenName(givenName);
    }

    public String getGivenName() {
        return person.getGivenName();
    }

    public void setFamilyName(String familyName) {
        initializePersonNameIfNecessary();
        person.getPersonName().setFamilyName(familyName);
    }

    public String getFamilyName() {
        return person.getFamilyName();
    }

    public void setGender(String gender) {
        person.setGender(gender);
    }

    public String getGender() {
        return person.getGender();
    }

    public void setUsername(String username) {
        if (StringUtils.isNotBlank(username)) {
            initializeUserIfNecessary();
            user.setUsername(username);
        }
    }

    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public void setUserEnabled(Boolean userEnabled) {
        if(userEnabled) {
        	initializeUserIfNecessary();
        }
        else
        	user = null;
    }


    public Boolean getUserEnabled() {
        if (user == null) {
            return null;
        } else {
            return !user.isRetired();
        }
    }
    
    public void setProviderEnabled(Boolean providerEnabled) {
    	if(providerEnabled)
    		initializeProviderIfNecessary();
    	else
    		provider = null;
    }
    
    public Boolean getProviderEnabled() {
    	if(provider == null)
    		return false;
    	else
    		return true;
    }
    
    public void unlock() {
        if (user == null) {
            throw new IllegalStateException("Cannot unlock an account that doesn't have a user");
        }
        user.removeUserProperty(OpenmrsConstants.USER_PROPERTY_LOCKOUT_TIMESTAMP);
        user.removeUserProperty(OpenmrsConstants.USER_PROPERTY_LOGIN_ATTEMPTS);
        userService.saveUser(user, null);
    }
    
    public Role getPrivilegeLevel() {
        // use getRoles instead of getAllRoles since privilege-level should be explicitly set
        if (user != null && user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().startsWith(EmrApiConstants.ROLE_PREFIX_PRIVILEGE_LEVEL)) {
                    return role;
                }
            }

        }
        return null;
    }
    
    public void setPrivilegeLevel(Role privilegeLevel) {

        if (privilegeLevel != null) {

            if (!accountService.getAllPrivilegeLevels().contains(privilegeLevel)) {
                throw new APIException("Attempting to set invalid privilege level");
            }

            initializeUserIfNecessary();

            if (!user.hasRole(privilegeLevel.getRole(), true)) {
                if (user.getRoles() != null) {
                    user.getRoles().removeAll(accountService.getAllPrivilegeLevels());
                }
                user.addRole(privilegeLevel);
            }
        } else if (user != null) {
            // privilege level is mandatory, so technically we shouldn't ever get here
            if (user.getRoles() != null) {
                user.getRoles().removeAll(accountService.getAllPrivilegeLevels());
            }
        }
    }
    
    public void setCapabilities(Set<Role> capabilities) {

        if (capabilities != null && capabilities.size() > 0) {
            if (!accountService.getAllCapabilities().containsAll(capabilities)) {
                throw new APIException("Attempt to set invalid capability");
            }

            initializeUserIfNecessary();

            if (user.getRoles() != null) {
                user.getRoles().removeAll(accountService.getAllCapabilities());
            }

            for (Role role : capabilities) {
                user.addRole(role);
            }
        } else if (user != null && user.getRoles() != null) {
            user.getRoles().removeAll(accountService.getAllCapabilities());
        }
    }
    
    public Set<Role> getCapabilities() {

        if (user == null) {
            return null;
        }

        Set<Role> capabilities = new HashSet<Role>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                if (role.getRole().startsWith(EmrApiConstants.ROLE_PREFIX_CAPABILITY)) {
                    capabilities.add(role);
                }
            }
        }
        return capabilities;
    }
    
    /*public void setProviderRoles(String[] providerRoles) {

        if (providerRoles != null && providerRoles.length > 0) {
            initializeProviderIfNecessary();
            
            providerRole.get

            for (ProviderRole role : providerRoles) {
                provider.setProviderRole(role);
            }
        
        }
    }*/
    
    public String generateIdentifier()
    {
    	long a = Math.round(Math.random()*1000);
    	long b = Math.round(Math.random()*10);
    	return a+"-"+b;
    }

    public void save() {

        if (person != null) {
            personService.savePerson(person);
        }

        if (user != null) {
            boolean existingUser = (user.getUserId() != null);
            userService.saveUser(user, password);

            // the saveUser(user, password) method will *only* set a password for a new user, it won't change an existing one
            if (existingUser && StringUtils.isNotBlank(password) && StringUtils.isNotBlank(confirmPassword)) {
                userService.changePassword(user, password);
            }
        }

        if (provider != null) {
        	provider.setName(getGivenName());
        	//if (StringUtils.isBlank(provider.getIdentifier())) {
            provider.setIdentifier(/*providerIdentifierGenerator.*/generateIdentifier());
        	//}
            providerService.saveProvider(provider);
        }
    }
}