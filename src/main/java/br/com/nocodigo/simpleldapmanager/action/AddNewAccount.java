package br.com.nocodigo.simpleldapmanager.action;

import java.io.UnsupportedEncodingException;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;
import br.com.nocodigo.simpleldapmanager.model.Ldap;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 21/03/2016
 *
 */
public class AddNewAccount extends AbstractConnection implements Connection {
	
	private Attributes entry = null;
	private String entryDN;
	private String password;
	
	/**
	 * 
	 * @param sAMAccountName primeiroNome.ultimoNome
	 * @param userPrincipalName sAMAccountName@domainComponent
	 * @param fullName nome completo
	 * @param department
	 * @param physicalDeliveryOfficeName
	 * @param description
	 * @param telephoneNumber
	 * @param company
	 * @param mail
	 * @param title
	 * @param password
	 * @param organizationalUnit
	 * @param domainComponent
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public AddNewAccount(
			LdapUser user,
			String password,
			String organizationalUnit,
			String domainComponent) throws IllegalArgumentException, IllegalAccessException {
		
		if (password.isEmpty() || organizationalUnit.isEmpty() || domainComponent.isEmpty()) {
			throw new IllegalArgumentException("The fields are required: password, organizationalUnit and domainComponent.");
		}
		
		this.password 	= password;
		this.entry 		= user.createAtributes();
		this.entryDN 	= Util.createString("CN=%s,%s,%s", user.getCn(), organizationalUnit, domainComponent);
	}

	private void activateAccount() throws UnsupportedEncodingException, NamingException {
		ModificationItem[] modificationItem = new ModificationItem[2];
		
		modificationItem[0] = new ModificationItem(
				DirContext.REPLACE_ATTRIBUTE, 
				new BasicAttribute("unicodePwd", this.converteStringToByteArray(this.password)));
		modificationItem[1] = new ModificationItem(
				DirContext.REPLACE_ATTRIBUTE, 
				new BasicAttribute("userAccountControl", Ldap.getEnabledAccountCode() ));
		
		this.getDirContext().modifyAttributes(this.entryDN, modificationItem);
	}
	
	@Override
	public void execute(ConnectionModel model)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		
		String dn = this.createDn(model.getCn());
		this.environment = createEnvironment(model, dn);
		this.createConnection();
		
		this.getDirContext().createSubcontext(this.entryDN, this.entry);
		
		try {
			activateAccount();
		} catch (UnsupportedEncodingException e) {
			throw new NamingException(e.getMessage());
		}
	}
	
}
