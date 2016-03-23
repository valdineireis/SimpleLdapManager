package br.com.nocodigo.simpleldapmanager.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.nocodigo.simpleldapmanager.Manager;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * @author Valdinei Reis (valdinei@nocodigo.com)
 */
public class LdapManagerTest {

	private static final ResourceBundle CONFIG 	= ResourceBundle.getBundle("test");
	
	private static final String LOGIN 					= CONFIG.getString("login");
	private static final String SENHA 					= CONFIG.getString("senha");
	private static final String OU_DEPARTAMENTO			= CONFIG.getString("ou_departamento");
	private static final String OU_TESTE 				= CONFIG.getString("ou_teste");
	private static final String DISPLAY_NAME			= CONFIG.getString("display_name");
	private static final List<String> ENABLED_ACCOUNTS	= Arrays.asList(CONFIG.getString("enabled_account").split(","));

	private static final String USER_NAME_TEST			= CONFIG.getString("user_name_test");
	private static final String USER_NAME_PASSWORD		= CONFIG.getString("user_name_password");
	private static final String USER_NAME_NPASSWORD		= CONFIG.getString("user_name_npassword");

	// nome do usuario para efetuar o teste de remocao de conta
	private static final String DEFAULT_USER_TEST		= "Teste Novo Usuario Padrao";
	
	// nome do usuario para efetuar o teste de criar uma nova conta
	private static final String NEW_ACCOUNT_NAME		= "Novo Usuario Teste";
	
	private static Manager ldapManager;
	
	@BeforeClass
	public static void start() throws Exception {
		ldapManager = new LdapManager();
		ldapManager.addAccount(DEFAULT_USER_TEST, "SEPLAE", "SEPLAE", "SEPLAE", "99999999", "PREFEITURA MUNICIPAL", "Cargo Teste", "minha@senha", OU_TESTE);
	}
	
	@AfterClass
	public static void rollback() throws Exception {
		LdapUser user = ldapManager.selectByAccountName(Util.createUserName(NEW_ACCOUNT_NAME));
		
		if(user != null) {
			ldapManager.deleteAccount(user.getsAMAccountName());
		}
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
		String accountControl = String.valueOf(user.getUserAccountControl());
		assertNotNull(user);
		assertTrue(ENABLED_ACCOUNTS.contains( accountControl ));
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
	public void deveAdicionarUmaNovaConta() {
		try {
			ldapManager.addAccount(NEW_ACCOUNT_NAME, "SEPLAE", "SEPLAE", "SEPLAE", "99999999", "COMPANY NAME", "Cargo Teste", "minha@senha", OU_TESTE);
		} catch (NamingException | JavaHomePathException | IllegalArgumentException | IllegalAccessException e) {
			fail("Add failure. " + e.getMessage());
		}
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	public void deveRequererONomeEASenha() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException, IllegalArgumentException, IllegalAccessException {
		ldapManager.addAccount("", "SEPLAE", "SEPLAE", "SEPLAE", "99999999", "COMPANY NAME", "Cargo Teste", "", OU_TESTE);
	}
	
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void deveRestringirOCadastroDeUsuariosComOMesmoNome() throws AuthenticationException, CommunicationException, IllegalArgumentException, IllegalAccessException, NamingException, JavaHomePathException {
		ldapManager.addAccount(DEFAULT_USER_TEST, "SEPLAE", "SEPLAE", "SEPLAE", "99999999", "COMPANY NAME", "Cargo Teste", "", OU_TESTE);
	}
	
	@Test
	public void deveRemoverUmaConta() {
		try {
			ldapManager.deleteAccount( Util.createUserName(DEFAULT_USER_TEST));
		} catch (NamingException | JavaHomePathException e) {
			fail("Delete failure. " + e.getMessage());
		}
	}

}
