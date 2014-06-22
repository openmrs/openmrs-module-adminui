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