package br.com.nocodigo.simpleldapmanager;

/**
 * Classe utilitária
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 18/02/2016
 *
 */
public class Util {

	public static String createString(String expression, String ... parameters) {
		String result = String.format(expression, (Object[]) parameters);
		return result;
	}
}
