package br.com.nocodigo.simpleldapmanager;

import java.io.UnsupportedEncodingException;

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
	void resetPassword(String accountName, String newPassword) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException, UnsupportedEncodingException;
	
	/**
	 * Atualiza a senha do usuário
	 * @param accountName
	 * @param password
	 * @param newPassword
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 * @throws JavaHomePathException
	 * @throws UnsupportedEncodingException
	 */
	void updatePassword(String accountName, String password, String newPassword) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException, UnsupportedEncodingException;
	
	/**
	 * Remove a conta do usuário
	 * @param accountName
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 * @throws JavaHomePathException
	 */
	void deleteAccount(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;
	
	/**
	 * Adiciona uma nova conta
	 * @param fullName
	 * @param department
	 * @param physicalDeliveryOfficeName
	 * @param description
	 * @param telephoneNumber
	 * @param company
	 * @param title
	 * @param password
	 * @param organizationalUnitToInsert
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 * @throws JavaHomePathException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	void addAccount(
			String fullName,
			String department, 
			String physicalDeliveryOfficeName, 
			String description, 
			String telephoneNumber,
			String company,
			String title,
			String password,
			String organizationalUnitToInsert) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException, IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException;

	/**
	 * Desabilita uma conta
	 * @param accountName
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 * @throws JavaHomePathException
	 */
	void disableAccount(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;

	/**
	 * Habilita uma conta
	 * @param accountName
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 * @throws JavaHomePathException
	 */
	void EnableAccount(String accountName) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;

}