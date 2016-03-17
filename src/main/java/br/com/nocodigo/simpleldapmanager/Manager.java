package br.com.nocodigo.simpleldapmanager;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 17/02/2016
 *
 */
public interface Manager {

	/**
	 * Cria o Objeto de Conexão com base no arquivo portal_ldap.properties
	 * @return
	 */
	ConnectionModel createModel();

	/**
	 * Efetua a conexção
	 * @return
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 */
	DirContext createConnection() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;

	/**
	 * Verifica as credenciais do usuário
	 * @param login
	 * @param password
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 */
	void verifyCredentials(String login, String password)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;
	
	/**
	 * Seleciona todos os usuários com base na OU
	 * @param ou
	 * @return
	 */
	ListUsers selectAllByOu(String ou) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;
	
	/**
	 * Seleciona um usuário com base no accountName
	 * @param accountName
	 * @return
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 */
	LdapUser selectByAccountName(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;
	
	/**
	 * Reseta a senha do usuário
	 * @param accountName
	 * @param password
	 * @param newPassword
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 * @throws JavaHomePathException
	 */
	void resetPassword(String accountName, String password, String newPassword) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;

}