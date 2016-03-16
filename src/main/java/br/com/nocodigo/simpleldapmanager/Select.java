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

	LdapUser byAccountName(String sAMAccountName) throws NamingException;

	ListUsers allByOu(String ou) throws NamingException;

}