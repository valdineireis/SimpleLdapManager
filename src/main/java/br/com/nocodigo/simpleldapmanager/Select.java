package br.com.nocodigo.simpleldapmanager;

import javax.naming.NamingException;

import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 17/02/2016
 *
 */
public interface Select {

	/**
	 * Busca um usuário pelo atributo sAMAccountName (login de acesso)
	 * @param sAMAccountName
	 * @return
	 * @throws NamingException
	 */
	LdapUser byAccountName(String sAMAccountName) throws NamingException;

	/**
	 * Lista todos os usuários pela OU
	 * @param ou
	 * @return
	 * @throws NamingException
	 */
	ListUsers allByOu(String ou) throws NamingException;

}