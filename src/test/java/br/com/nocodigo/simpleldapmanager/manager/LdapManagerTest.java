package br.com.nocodigo.simpleldapmanager.manager;

import static org.junit.Assert.*;

import java.util.ResourceBundle;

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

	private static final ResourceBundle CONFIG 	= ResourceBundle.getBundle("test");
	
	private static final String LOGIN 				= CONFIG.getString("login");
	private static final String SENHA 				= CONFIG.getString("senha");
	private static final String OU_DEPARTAMENTO		= CONFIG.getString("ou_departamento");
	private static final String OU_CONTA_SERVICO 	= CONFIG.getString("ou_conta_servico");
	private static final String DISPLAY_NAME		= CONFIG.getString("display_name");
	private static final int ENABLED_ACCOUNT		= Integer.parseInt(CONFIG.getString("enabled_account"));

	private static final String USER_NAME_TEST		= CONFIG.getString("user_name_test");
	private static final String USER_NAME_PASSWORD	= CONFIG.getString("user_name_password");
	private static final String USER_NAME_NPASSWORD	= CONFIG.getString("user_name_npassword");
	
	private static final String NEW_ACCOUNT_NAME	= CONFIG.getString("new_account_name");
	
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
	
	@Test
	public void deveResetarASenha() {
		try {
			ldapManager.resetPassword(USER_NAME_TEST, USER_NAME_PASSWORD, USER_NAME_NPASSWORD);
		} catch (NamingException | JavaHomePathException e) {
			fail("Reset Password failure. " + e.getMessage());
		}
	}
	
	@Test
	public void deveRemoverUmaConta() {
		try {
			ldapManager.deleteAccount(NEW_ACCOUNT_NAME);
		} catch (NamingException | JavaHomePathException e) {
			fail("Delete failure. " + e.getMessage());
		}
	}

}
