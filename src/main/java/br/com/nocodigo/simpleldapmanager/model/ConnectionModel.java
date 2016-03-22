package br.com.nocodigo.simpleldapmanager.model;

/**
 * Classe de modelo, responsável por manter os dados da conexão
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 16/02/2016
 *
 */
public class ConnectionModel {

	private String host;
	private String port;
	private String password;
	private String cn;
	private String ou;
	private String baseDn;
	private String connectionType;
	private String domain;
	private boolean useSSL;
	
	public ConnectionModel(
			String host, 
			String port, 
			String password, 
			String cn, 
			String ou, 
			String baseDn, 
			String connectionType,
			String domain,
			boolean useSSL) {
		setHost(host);
		setPort(port);
		setPassword(password);
		setCn(cn);
		setOu(ou);
		setBaseDn(baseDn);
		setConnectionType(connectionType);
		setDomain(domain);
		setUseSSL(useSSL);
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		if (password == null || password.isEmpty())
			throw new IllegalArgumentException("The password can not be empty");
		this.password = password;
	}
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getOu() {
		return ou;
	}
	public void setOu(String ou) {
		this.ou = ou;
	}
	public String getBaseDn() {
		return baseDn;
	}
	public void setBaseDn(String baseDn) {
		this.baseDn = baseDn;
	}
	public String getConnectionType() {
		return connectionType;
	}
	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public boolean isUseSSL() {
		return useSSL;
	}
	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}
	
}
