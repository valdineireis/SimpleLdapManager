package br.com.nocodigo.simpleldapmanager.manager;

import static org.junit.Assert.*;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.junit.Before;
import org.junit.Test;

import br.com.nocodigo.simpleldapmanager.Manager;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * @author Valdinei Reis (valdinei@nocodigo.com)
 */
public class LdapManagerTest {

	private static final String LOGIN 				= "LOGIN";
	private static final String SENHA 				= "SENHA";
	private static final String OU_DEPARTAMENTO 	= "OU=Usuários,OU=Departamentos";
	private static final String OU_CONTA_SERVICO 	= "OU=Contas de Serviços,OU=TI  Administração";
	private static final String DISPLAY_NAME		= "Valdinei Reis da Silva";
	private static final int ENABLED_ACCOUNT		= 66048;
	
	private Manager ldapManager;

	@Before
	public void setUp() throws Exception {
		ldapManager = new LdapManager();
	}

	@Test
	public void deveCriarOObjetoManager() {
		assertNotNull(ldapManager);
	}
	
	@Test
	public void deveCriarOModelo() {
		assertNotNull(ldapManager.createModel());
	}
	
	@Test
	public void deveCriarUmaConexao() throws Exception {
		DirContext dirContext = ldapManager.createConnection();
		dirContext.close();
		assertNotNull(dirContext);
	}
	
	@Test
	public void deveEfetuarOLogin() {
		try {
			ldapManager.verifyCredentials(LOGIN, SENHA);
		} catch (Exception ex) {
			fail("Login failure");
		}
	}
	
	@Test
	public void deveListarTodosUsuariosByOu() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ListUsers users = ldapManager.selectAllByOu(OU_DEPARTAMENTO);
		
		System.out.println("LISTA DE USUÁRIOS ATIVOS");
		for (LdapUser user : users.getAll()) {
			System.out.println(user.toString());
		}
		System.out.println("-----------------------------");
		
		assertNotNull(users);
		assertTrue(users.size() > 0);
	}
	
	@Test
	public void deveBuscarUmUsuarioByAccountName() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		LdapUser user = ldapManager.selectByAccountName(LOGIN);
		
		System.out.println("USUÁRIO");
		System.out.println(user.toString());
		System.out.println("-----------------------------");
		
		assertNotNull(user);
		assertEquals(DISPLAY_NAME, user.getDisplayName());
	}
	
	@Test
	public void deveBuscarUmUsuarioHabilitado() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		LdapUser user = ldapManager.selectByAccountName(LOGIN);
		assertNotNull(user);
		assertEquals(ENABLED_ACCOUNT, user.getUserAccountControl());
	}

}
