package br.com.nocodigo.simpleldapmanager;

import javax.naming.NamingException;

import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

public interface Select {

	LdapUser byAccountName(String sAMAccountName) throws NamingException;

	ListUsers allByOu(String ou) throws NamingException;

}