package br.com.nocodigo.simpleldapmanager;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * Classe utilitária
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 18/02/2016
 *
 */
public class Util {

	/**
	 * Cria uma String com base em uma expressão
	 * @param expression Expressão
	 * @param parameters Parametros
	 * @return
	 */
	public static String createString(String expression, String ... parameters) {
		String result = String.format(expression, (Object[]) parameters);
		return result;
	}

	/**
	 * Obtem o primeiro nome
	 * @param nomeCompleto
	 * @return
	 */
	public static String fistName(String nomeCompleto) {
		String[] split = nomeCompleto.split(" ");
		return split[0];
	}
	
	/**
	 * Obtem o último nome
	 * @param nomeCompleto
	 * @return
	 */
	public static String lastName(String nomeCompleto) {
		String[] split = nomeCompleto.split(" ");
		return split[split.length - 1];
	}

	/**
	 * Obtem o sobrenome completo
	 * @param nomeCompleto
	 * @return
	 */
	public static String fullLastName(String nomeCompleto) {
		int fistIndex = nomeCompleto.indexOf(" ");
		String sobreNome = nomeCompleto.substring(fistIndex + 1, nomeCompleto.length());
		return sobreNome;
	}

	/**
	 * Cria uma extrutura básica de nome de usuário, juntando o primeiro nome + . + ultimo nome, exemplo: primeiroNome.ultimoNome
	 * @param fullName
	 * @return
	 */
	public static String createUserName(String fullName) {
		String userName = Util.createString("%s.%s", Util.fistName(fullName), Util.lastName(fullName));
		return userName.toLowerCase();
	}

	/**
	 * Extrai as iniciais do nome
	 * @param nomeCompleto
	 * @return
	 */
	public static String extractInitials(String nomeCompleto) {
		String[] split = nomeCompleto.toUpperCase().split(" ");
		List<String> reservadas = Arrays.asList("DO", "DOS", "DA", "DAS", "DE"); 
		String iniciais = "";
		for (String nome : split) {
			if (nome.length() > 1 && !reservadas.contains(nome))
				iniciais += nome.charAt(0);
		}
		return iniciais;
	}
	
	/**
	 * Converte uma String em um array de bytes, válido para a senha do Active Directory
	 * @param password
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static byte[] converteStringToByteArray(String password) throws UnsupportedEncodingException {
	    String newQuotedPassword = "\"" + password + "\"";
	    return newQuotedPassword.getBytes("UTF-16LE");
	}
}
