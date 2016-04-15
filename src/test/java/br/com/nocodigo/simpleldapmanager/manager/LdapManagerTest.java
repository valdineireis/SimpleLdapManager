package br.com.nocodigo.simpleldapmanager.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.UnsupportedEncodingException;
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
import org.junit.runner.RunWith;

import br.com.nocodigo.helper.Order;
import br.com.nocodigo.helper.OrderedRunner;
import br.com.nocodigo.simpleldapmanager.Manager;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;
import br.com.nocodigo.simpleldapmanager.model.ListUsers;

/**
 * @author Valdinei Reis (valdinei@nocodigo.com)
 */
@RunWith(OrderedRunner.class)
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
	
	private static void addAccount(Manager manager, String accountName, String senha) throws AuthenticationException, CommunicationException, IllegalArgumentException, IllegalAccessException, NamingException, JavaHomePathException, UnsupportedEncodingException {
		manager.addAccount(accountName, "SEPLAE", "SEPLAE", "SEPLAE", "99999999", "PREFEITURA MUNICIPAL", "Cargo Teste", senha, OU_TESTE);
	}
	
	private static void addAccount(Manager manager, String accountName) throws AuthenticationException, CommunicationException, IllegalArgumentException, IllegalAccessException, NamingException, JavaHomePathException, UnsupportedEncodingException {
		addAccount(manager, accountName, "minha@senha");
	}
	
	@BeforeClass
	public static void start() throws Exception {
		ldapManager = new LdapManager();
		LdapUser user = ldapManager.selectByAccountName(Util.createUserName(DEFAULT_USER_TEST));
		
		if (user == null) {
			addAccount(ldapManager, DEFAULT_USER_TEST);
		}
	}
	
	@AfterClass
	public static void rollback() throws Exception {
		LdapUser user = ldapManager.selectByAccountName(Util.createUserName(NEW_ACCOUNT_NAME));
		
		if(user != null) {
			ldapManager.deleteAccount(user.getsAMAccountName());
		}
	}

	@Test
	@Order(order=1)
	public void deveCriarOObjetoManager() {
		assertNotNull(ldapManager);
	}
	
	@Test
	@Order(order=2)
	public void deveCriarOModelo() {
		assertNotNull(ldapManager.createModel());
	}
	
	@Test
	@Order(order=3)
	public void deveCriarUmaConexao() throws Exception {
		DirContext dirContext = ldapManager.createConnection();
		dirContext.close();
		assertNotNull(dirContext);
	}
	
	@Test
	@Order(order=4)
	public void deveEfetuarOLogin() {
		try {
			ldapManager.verifyCredentials(LOGIN, SENHA);
		} catch (Exception ex) {
			fail("Login failure");
		}
	}
	
	@Test
	@Order(order=5)
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
	@Order(order=6)
	public void deveBuscarUmUsuarioByAccountName() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		LdapUser user = ldapManager.selectByAccountName(LOGIN);
		
		System.out.println("USUÁRIO");
		System.out.println(user.toString());
		System.out.println("-----------------------------");
		
		assertNotNull(user);
		assertEquals(DISPLAY_NAME, user.getDisplayName());
	}
	
	@Test
	@Order(order=7)
	public void deveBuscarUmUsuarioHabilitado() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		LdapUser user = ldapManager.selectByAccountName(LOGIN);
		String accountControl = String.valueOf(user.getUserAccountControl());
		assertNotNull(user);
		assertTrue(ENABLED_ACCOUNTS.contains( accountControl ));
	}
	
	@Test
	@Order(order=8)
	public void deveResetarASenha() {
		try {
			ldapManager.resetPassword(USER_NAME_TEST, USER_NAME_NPASSWORD);
		} catch (NamingException | JavaHomePathException | UnsupportedEncodingException e) {
			fail("Reset Password failure. " + e.getMessage());
		}
	}
	
	@Test
	@Order(order=8)
	public void deveAtualizarASenha() {
		try {
			ldapManager.updatePassword(USER_NAME_TEST, USER_NAME_PASSWORD, USER_NAME_NPASSWORD);
			ldapManager.verifyCredentials(USER_NAME_TEST, USER_NAME_NPASSWORD);
		} catch (NamingException | JavaHomePathException | UnsupportedEncodingException e) {
			fail("Update Password failure. " + e.getMessage());
		}
	}
	
	@Test
	@Order(order=9)
	public void deveAdicionarUmaNovaConta() {
		try {
			addAccount(ldapManager, NEW_ACCOUNT_NAME);
			
			LdapUser user = ldapManager.selectByAccountName(Util.createUserName(NEW_ACCOUNT_NAME));
			
			assertNotNull(user);
			assertEquals(512, user.getUserAccountControl());
			
		} catch (Exception e) {
			fail("Add failure. " + e.getMessage());
		}
	}

	@Test(expected = java.lang.IllegalArgumentException.class)
	@Order(order=10)
	public void deveRequererONomeEASenha() throws Exception {
		addAccount(ldapManager, "", "");
	}
	
	@Test(expected = javax.naming.NameAlreadyBoundException.class)
	@Order(order=11)
	public void deveRestringirOCadastroDeUsuariosComOMesmoNome() throws Exception {
		addAccount(ldapManager, DEFAULT_USER_TEST);
	}
	
	@Test
	@Order(order=12)
	public void deveRemoverUmaConta() {
		try {
			ldapManager.deleteAccount( Util.createUserName(DEFAULT_USER_TEST));
		} catch (NamingException | JavaHomePathException e) {
			fail("Delete failure. " + e.getMessage());
		}
	}
	
	@Test
	@Order(order=13)
	public void deveDesabilitarUmaConta() throws Exception {
		String accountName = Util.createUserName(NEW_ACCOUNT_NAME);
		
		ldapManager.disableAccount(accountName);
		
		LdapUser user = ldapManager.selectByAccountName(accountName);
		
		assertNotNull(user);
		assertEquals(514, user.getUserAccountControl());
	}
	
	@Test
	@Order(order=14)
	public void deveHabilitarUmaConta() throws Exception {
		String accountName = Util.createUserName(NEW_ACCOUNT_NAME);
		
		ldapManager.EnableAccount(accountName);
		
		LdapUser user = ldapManager.selectByAccountName(accountName);
		
		assertNotNull(user);
		assertEquals(512, user.getUserAccountControl());
	}

}
