package br.com.nocodigo.simpleldapmanager.action;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 21/03/2016
 *
 */
public class AddNewAccountAction extends AbstractConnection implements Connection {
	
	private Attributes entry = null;
	private String entryDN;
	
	/**
	 * @param user
	 * @param organizationalUnit
	 * @param domainComponent
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public AddNewAccountAction(
			LdapUser user,
			String organizationalUnit,
			String domainComponent) throws IllegalArgumentException, IllegalAccessException {
		
		if (organizationalUnit.isEmpty() || domainComponent.isEmpty()) {
			throw new IllegalArgumentException("The fields are required: organizationalUnit and domainComponent.");
		}
		
		this.entry 		= user.createAtributes();
		this.entryDN 	= Util.createString("CN=%s,%s,%s", user.getCn(), organizationalUnit, domainComponent);
	}

	@Override
	public void execute(ConnectionModel model)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		
		String dn = this.createDn(model.getCn());
		this.environment = createEnvironment(model, dn);
		this.createConnection();
		
		this.getDirContext().createSubcontext(this.entryDN, this.entry);
	}
	
}
