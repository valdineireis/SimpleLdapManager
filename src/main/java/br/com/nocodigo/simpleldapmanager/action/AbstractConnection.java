package br.com.nocodigo.simpleldapmanager.action;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 17/02/2016
 *
 */
public abstract class AbstractConnection {

	private static final String LDAP = "LDAP";
	private static final String LDAPS = "LDAPS";

	protected DirContext dirContext = null;
	protected Hashtable<String, String> environment = null;
	
	private String createUrl(String host, String port, boolean useSSL) {
		String protocolo = useSSL ? LDAPS : LDAP;
		String url = Util.createString("%s://%s:%s", protocolo, host, port);
		return url;
	}
	
	private String getTrustStorePath() throws JavaHomePathException {
		//String certificatesTrustStorePath = "C:\\Program Files\\Java\\jdk1.8.0_45\\jre\\lib\\security\\cacerts";
		
		String java_home = System.getProperty("java.home");
		
		if (java_home == null || java_home.isEmpty())
			throw new JavaHomePathException("JAVA_HOME path can not be load");
		
		StringBuilder path = new StringBuilder();
		path.append(java_home);
		path.append(File.separator);
		path.append("lib");
		path.append(File.separator);
		path.append("security");
		path.append(File.separator);
		path.append("cacerts");
		
		return path.toString();
	}

	protected Hashtable<String, String> createEnvironment(ConnectionModel model, String dn) throws JavaHomePathException {
		String url = createUrl(model.getHost(), model.getPort(), model.isUseSSL());
		
		Hashtable<String, String> environment = new Hashtable<String, String>();
		
		environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		environment.put(Context.PROVIDER_URL, url);
		environment.put(Context.SECURITY_AUTHENTICATION, model.getConnectionType());
		environment.put(Context.SECURITY_PRINCIPAL, dn);
		environment.put(Context.SECURITY_CREDENTIALS, model.getPassword());
		environment.put(Context.REFERRAL, "ignore");
		
		if (model.isUseSSL()) {
			environment.put(Context.SECURITY_PROTOCOL, "ssl");
			System.setProperty("javax.net.ssl.trustStore", getTrustStorePath());
		}
		
		return environment;
	}
	
	protected void createConnection() throws javax.naming.AuthenticationException, javax.naming.CommunicationException, NamingException {
		dirContext = new InitialDirContext(environment);
	}
	
	protected String createDn(String cn) {
		String dn = cn.replaceAll("CN=", "");
		return dn;
	}
	
	protected byte[] converteStringToByteArray(String password) throws UnsupportedEncodingException {
	    String newQuotedPassword = "\"" + password + "\"";
	    return newQuotedPassword.getBytes("UTF-16LE");
	}
	
	public void close() throws NamingException {
		if (dirContext != null) {
			dirContext.close();
		}
	}
	
	public DirContext getDirContext() {
		return this.dirContext;
	}
	
	public abstract void execute(ConnectionModel model) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;
}
