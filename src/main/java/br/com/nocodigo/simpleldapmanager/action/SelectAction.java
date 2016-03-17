package br.com.nocodigo.simpleldapmanager.action;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Select;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * Classe responsável por buscar informações dos usuários
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 18/02/2016
 *
 */
public class SelectAction implements Select {

	private Connection connection;
	private ConnectionModel model;
	
	public SelectAction(Connection connection, ConnectionModel model)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		this.model = model;
		this.connection = connection;
		this.connection.execute(model);
	}
	
	private SearchResult findAccountByAccountName(String ldapSearchBaseDn, String accountName)
			throws NamingException {

		String searchFilter = Util.createString("(&(objectClass=user)(sAMAccountName=%s))", accountName);

		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> results = connection.getDirContext().search(ldapSearchBaseDn, searchFilter, searchControls);

		SearchResult searchResult = null;
		if (results.hasMoreElements()) {
			searchResult = (SearchResult) results.nextElement();

			// make sure there is not another item available, there should be
			// only 1 match
			if (results.hasMoreElements()) {
				System.err.println("Matched multiple users for the accountName: " + accountName);
				return null;
			}
		}

		return searchResult;
	}

	private LdapUser extractUser(SearchResult searchRes) throws NamingException {
		LdapUser user;
		user = new LdapUser().extractUser(searchRes);
		return user;
	}
	
	@Override
	public LdapUser byAccountName(String sAMAccountName) throws NamingException {
		LdapUser user = null;
		
		try {
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			
			SearchResult searchResult = findAccountByAccountName(model.getBaseDn(), sAMAccountName);
			user = extractUser(searchResult);
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		return user;
	}

	@Override
	public ListUsers allByOu(String ou) throws NamingException {
		ListUsers listUsers = new ListUsers();
		
		try {
			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			
			NamingEnumeration<SearchResult> results = connection.getDirContext()
					.search(Util.createString("%s,%s", ou, model.getBaseDn()), null);
			
			while (results.hasMore()) {
				SearchResult searchResult = (SearchResult) results.next();
				
				LdapUser user = extractUser(searchResult);
				
				listUsers.add(user);
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
		return listUsers;
	}
}
