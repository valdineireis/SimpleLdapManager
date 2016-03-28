package br.com.nocodigo.simpleldapmanager;

import javax.naming.NamingException;

/**
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 28/03/2016
 */
public interface SetAttribute {

	/**
	 * Adiciona um atributo
	 * @param attributeName
	 * @param value
	 */
	void add(String attributeName, Object value);

	/**
	 * Aplica a atualização
	 * @throws NamingException
	 */
	void apply() throws NamingException;

}