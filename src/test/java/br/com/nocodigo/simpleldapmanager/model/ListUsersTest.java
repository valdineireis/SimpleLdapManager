package br.com.nocodigo.simpleldapmanager.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ListUsersTest {

	private ListUsers listUsers;
	
	private void addUser(int total, boolean ativo) {
		for (int i = 0; i < total; i++) {
			LdapUser user = new LdapUser();
			user.setsAMAccountName("user" + i);
			
			if (ativo) {
				user.setUserAccountControl(512);
			} else {
				user.setUserAccountControl(66050);
			}
			
			listUsers.add(user);
		}
	}
	
	@Before
	public void setUp() {
		listUsers = new ListUsers();
	}
	
	@Test
	public void deveCriarUmaListaVazia() {
		assertTrue(listUsers.isEmpty());
	}
	
	@Test
	public void deveLimparALista() {
		addUser(10, true);
		assertFalse(listUsers.isEmpty());
		
		listUsers.clear();
		assertTrue(listUsers.isEmpty());
	}
	
	@Test
	public void deveContarAQuantidadeDeRegistrosNaLista() {
		addUser(10, true);
		
		assertEquals(10, listUsers.size());
	}
	
	@Test
	public void deveAddicionarUmNovoRegistroNaLista() {
		addUser(10, true);
		
		LdapUser user = new LdapUser();
		user.setUserAccountControl(512);
		listUsers.add(user);
		
		assertEquals(11, listUsers.size());
	}
	
	@Test
	public void deveListarTodosOsRegistros() {
		addUser(10, true);
		
		assertEquals(10, listUsers.getAll().size());
		
		for (int i = 0; i < listUsers.getAll().size(); i++) {
			assertEquals("user" + i, listUsers.getAll().get(i).getsAMAccountName());
		}
	}
	
	@Test
	public void deveListarTodosOsRegistrosAtivos() {
		addUser(5, true);
		addUser(5, false);
		
		assertEquals(5, listUsers.getAllActive().size());
	}

}
