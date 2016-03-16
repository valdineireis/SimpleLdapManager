package br.com.nocodigo.simpleldapmanager;

import javax.naming.AuthenticationException;
import javax.naming.CommunicationException;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import br.com.nocodigo.simpleldapmanager.exception.JavaHomePathException;
import br.com.nocodigo.simpleldapmanager.model.ConnectionModel;

/**
 * 
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 17/02/2016
 *
 */
public interface Connection {

	/**
	 * Executa a ação
	 * @param model
	 * @throws AuthenticationException
	 * @throws CommunicationException
	 * @throws NamingException
	 */
	void execute(ConnectionModel model) throws AuthenticationException, CommunicationException, NamingException, JavaHomePathException;
	
	/**
	 * Fecha a conexão
	 * @throws NamingException
	 */
	void close() throws NamingException;
	
	/**
	 * 
	 * @return javax.naming.directory.DirContext
	 */
	DirContext getDirContext();

}