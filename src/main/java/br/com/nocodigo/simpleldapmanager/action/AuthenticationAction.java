package br.com.nocodigo.simpleldapmanager.action;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;

/**
 * Classe responsável por efetuar a autenticação
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 17/02/2016
 *
 */
public class AuthenticationAction extends AbstractConnection implements Connection {

	private String createDn(String cn, String baseDn) {
		baseDn = baseDn.toLowerCase().replace("dc=", "").replace(",", ".");
		String dn = Util.createString("%s@%s", cn, baseDn);
		return dn;
	}

	@Override
	public void execute(ConnectionModel model) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		String dn = createDn(model.getCn(), model.getBaseDn());
		environment = createEnvironment(model, dn);
		createConnection();
	}
}
