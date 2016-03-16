package br.com.nocodigo.simpleldapmanager.manager;

import java.util.ResourceBundle;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Manager;
import br.com.nocodigo.simpleldapmanager.Select;
import br.com.nocodigo.simpleldapmanager.action.AuthenticationAction;
import br.com.nocodigo.simpleldapmanager.action.ConnectionAction;
import br.com.nocodigo.simpleldapmanager.action.SelectAction;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * Classe responsável por gerenciar e facilitar a manutenção dos dados
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 16/02/2016
 *
 */
public class LdapManager implements Manager {

	private static final ResourceBundle CONFIG = ResourceBundle.getBundle("ldap");
	
	private Connection ldapConnection;
	
	public LdapManager() {
		ldapConnection = new ConnectionAction();
	}
	
	@Override
	public ConnectionModel createModel() {
		String host 			= CONFIG.getString("ldap.host");
		String port 			= CONFIG.getString("ldap.port");
		String password 		= CONFIG.getString("ldap.password");
		String cn 				= CONFIG.getString("ldap.cn");
		String ou 				= CONFIG.getString("ldap.ou");
		String baseDn 			= CONFIG.getString("ldap.baseDn");
		String connectionType 	= CONFIG.getString("ldap.connectionType");
		String useSSL			= CONFIG.getString("ldap.useSSL");
		
		return new ConnectionModel(host, port, password, cn, ou, baseDn, connectionType, Boolean.parseBoolean(useSSL));
	}
	
	@Override
	public DirContext createConnection() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ldapConnection.execute(createModel());
		return ldapConnection.getDirContext();
	}
	
	@Override
	public void verifyCredentials(String login, String password) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel model = createModel();
		model.setCn(login);
		model.setPassword(password);
		
		Connection ldapAuthenticate = new AuthenticationAction();
		ldapAuthenticate.execute(model);
	}

	@Override
	public ListUsers selectAllByOu(String ou) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel model = createModel();
		Select select = new SelectAction(ldapConnection, model);
		ListUsers users = select.allByOu(ou);
		
		return users;
	}

	@Override
	public LdapUser selectByAccountName(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel model = createModel();
		Select select = new SelectAction(ldapConnection, model);
		LdapUser user = select.byAccountName(accountName);
		
		return user;
	}
	
}
