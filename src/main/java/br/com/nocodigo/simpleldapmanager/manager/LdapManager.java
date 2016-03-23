package br.com.nocodigo.simpleldapmanager.manager;

import java.util.ResourceBundle;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Manager;
import br.com.nocodigo.simpleldapmanager.Select;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.action.AddNewAccount;
import br.com.nocodigo.simpleldapmanager.action.AuthenticationAction;
import br.com.nocodigo.simpleldapmanager.action.ConnectionAction;
import br.com.nocodigo.simpleldapmanager.action.DeleteAccountAction;
import br.com.nocodigo.simpleldapmanager.action.ResetPasswordAction;
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
	
	private ConnectionModel model;
	
	public LdapManager() {
		this.model = createModel();
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
		String domain 			= CONFIG.getString("ldap.domain");
		String useSSL			= CONFIG.getString("ldap.useSSL");
		
		return new ConnectionModel(host, port, password, cn, ou, baseDn, connectionType, domain, Boolean.parseBoolean(useSSL));
	}
	
	@Override
	public DirContext createConnection() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		Connection connection = new ConnectionAction();
		connection.execute(createModel());
		return connection.getDirContext();
	}
	
	@Override
	public void verifyCredentials(String login, String password) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel connectionModel = createModel();
		connectionModel.setCn(login);
		connectionModel.setPassword(password);
		
		Connection connection = new AuthenticationAction();
		connection.execute(connectionModel);
		connection.close();
	}

	@Override
	public ListUsers selectAllByOu(String ou) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		Connection connection = new ConnectionAction();
		Select select = new SelectAction(connection, this.model);
		ListUsers users = select.allByOu(ou);
		connection.close();
		return users;
	}

	@Override
	public LdapUser selectByAccountName(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		Connection connection = new ConnectionAction();
		Select select = new SelectAction(connection, this.model);
		LdapUser user = select.byAccountName(accountName);
		connection.close();
		return user;
	}
	
	@Override
	public void resetPassword(String accountName, String password, String newPassword) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		verifyCredentials(accountName, password);
		LdapUser user = selectByAccountName(accountName);
		Connection connection = new ResetPasswordAction(newPassword, user.getDistinguishedname());
		connection.execute(this.model);
		connection.close();
	}

	@Override
	public void deleteAccount(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		LdapUser user = selectByAccountName(accountName);
		Connection connection = new DeleteAccountAction(user.getDistinguishedname());
		connection.execute(this.model);
		connection.close();
	}
	
	@Override
	public void addAccount(
			String fullName,
			String department, 
			String physicalDeliveryOfficeName, 
			String description, 
			String telephoneNumber,
			String company,
			String title,
			String password,
			String organizationalUnitToInsert) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException, IllegalArgumentException, IllegalAccessException {
		
		if (organizationalUnitToInsert.isEmpty())
			throw new IllegalArgumentException("The field organizationalUnitToInsert can not be empty");
		
		String userPrincipalName_sufixo 	= this.model.getBaseDn().replace("DC=", "").replace(",", ".");
		
		String sAMAccountName 				= Util.createUserName(fullName);
		String mail 						= Util.createString("%s@%s", sAMAccountName, this.model.getDomain());
		String userPrincipalName 			= Util.createString("%s@%s", sAMAccountName, userPrincipalName_sufixo);
		String domainComponent 				= this.model.getBaseDn();
		
		LdapUser ldapUser = new LdapUser(sAMAccountName, userPrincipalName, fullName, department, physicalDeliveryOfficeName, description, telephoneNumber, company, mail, title);
		Connection connection = new AddNewAccount(ldapUser, password, organizationalUnitToInsert, domainComponent);
		
		connection.execute(this.model);
		connection.close();
	}
	
}
