package br.com.nocodigo.simpleldapmanager.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Objeto responsável facilitar a manipulação das informações dos usuários
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 18/02/2016
 *
 */
public class ListUsers {

	private List<LdapUser> ldapUserList;
	
	public ListUsers() {
		this.ldapUserList = new ArrayList<>();
	}
	
	public void add(LdapUser user) {
		ldapUserList.add(user);
	}
	
	public List<LdapUser> getAll() {
		return ldapUserList;
	}
	
	public List<LdapUser> getAllActive() {
		return ldapUserList.stream()
				.filter(u -> u.isActive())
				.collect(Collectors.toList());
	}
	
	public int size() {
		return ldapUserList.size();
	}
	
	public void clear() {
		ldapUserList.clear();
	}
	
	public boolean isEmpty() {
		return ldapUserList.isEmpty();
	}
	
}
