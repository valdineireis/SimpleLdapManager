package br.com.nocodigo.simpleldapmanager.action;

import java.io.UnsupportedEncodingException;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;

/**
 * Classe responsável por resetar a senha do usuário
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 16/03/2016
 *
 */
public class ResetPasswordAction extends AbstractConnection implements Connection {

	private String newPassword;
	private String distinguishedName;
	
	private void modifyAdAttribute(String userCN, String attribute, Object value) throws NamingException {
	    ModificationItem[] modificationItem = new ModificationItem[1];
	    modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
	            new BasicAttribute(attribute, value));
	    getDirContext().modifyAttributes(userCN, modificationItem);
	}
	
	public ResetPasswordAction(String newPassword, String distinguishedName) {
		this.newPassword = newPassword;
		this.distinguishedName = distinguishedName;
	}
	
	@Override
	public void execute(ConnectionModel model)
			throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException {

		String dn = this.createDn(model.getCn());
		this.environment = createEnvironment(model, dn);
		this.createConnection();
		
		try {
			modifyAdAttribute(this.distinguishedName, "unicodePwd", this.converteStringToByteArray(newPassword));
		} catch (UnsupportedEncodingException e) {
			throw new CommunicationException(e.getMessage());
		}
	}
}
