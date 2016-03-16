package br.com.nocodigo.simpleldapmanager.action;

import static org.junit.Assert.assertNotNull;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;

import org.junit.Test;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 16/02/2016
 *
 */
public class LdapConnectionTest {

	private static final String HOST = "INFORME O IP OU HOST DO SERVIDOR";
	private static final String PORTA_PADRAO = "389";
	private static final String PORTA_SSL = "636";
	private static final String PASSWORD = "INFORME A SENHA";
	
	private ConnectionModel createModel(
			String host, 
			String port, 
			String password, 
			String cn, 
			String ou, 
			String baseDn, 
			String connectionType,
			String useSSL) {
		return new ConnectionModel(host, port, password, cn, ou, baseDn, connectionType, Boolean.parseBoolean(useSSL));
	}

	private ConnectionModel createBasicModel(String host, String port, String password, boolean useSSL) {
		String cn 				= "CN=Valdinei Reis da Silva";
		String ou 				= "OU=Contas de Serviços,OU=TI  Administração";
		String baseDn 			= "DC=EMPRESA,DC=LOCAL";
		String connectionType 	= "simple";
		
		return createModel(host, port, password, cn, ou, baseDn, connectionType, String.valueOf(useSSL));
	}

	private Connection conecta(ConnectionModel model)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		Connection ldapConnection = new ConnectionAction();
		ldapConnection.execute(model);
		ldapConnection.close();
		return ldapConnection;
	}
	
	@Test
	public void deveCriarOObjetoDeConexao() throws Exception {
		ConnectionModel model = createBasicModel(HOST, PORTA_PADRAO, PASSWORD, false);
		
		Connection ldapConnection = conecta(model);
		
		assertNotNull(ldapConnection);
	}
	
	@Test
	public void deveEstabelecerUmaConexaoSegura() throws Exception {
		ConnectionModel model = createBasicModel(HOST, PORTA_SSL, PASSWORD, true);
		
		Connection ldapConnection = conecta(model);
		
		assertNotNull(ldapConnection);
	}
	
	@Test(expected = javax.naming.AuthenticationException.class)
	public void deveGerarUmaExcecaoDeAutenticacao() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel model = createBasicModel(HOST, PORTA_PADRAO, "123", false);
		conecta(model);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deveGerarUmaExcecaoDeSenhaVazia() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel model = createBasicModel(HOST, PORTA_PADRAO, "", false);
		conecta(model);
	}
	
	@Test(expected = javax.naming.CommunicationException.class)
	public void deveGerarUmaExcecaoDeComunicacaoComARede() throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {
		ConnectionModel model = createBasicModel("111.11.11.1", PORTA_PADRAO, "123", false);
		conecta(model);
	}

}
