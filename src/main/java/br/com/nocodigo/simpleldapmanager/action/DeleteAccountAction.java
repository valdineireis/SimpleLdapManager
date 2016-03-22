package br.com.nocodigo.simpleldapmanager.action;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;

/**
 * Classe responsável por remover a conta do usuário
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 18/03/2016
 *
 */
public class DeleteAccountAction extends AbstractConnection implements Connection {

	private String distinguishedName;
	
	public DeleteAccountAction(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}
	
	@Override
	public void execute(ConnectionModel model)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		
		String dn = this.createDn(model.getCn());
		this.environment = createEnvironment(model, dn);
		this.createConnection();
		
		this.getDirContext().destroySubcontext(this.distinguishedName);
	}

}
