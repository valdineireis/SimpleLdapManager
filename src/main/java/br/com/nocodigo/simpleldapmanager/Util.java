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

	public static String createString(String expression, String ... parameters) {
		String result = String.format(expression, (Object[]) parameters);
		return result;
	}

	public static String fistName(String nomeCompleto) {
		String[] split = nomeCompleto.split(" ");
		return split[0];
	}
	
	public static String lastName(String nomeCompleto) {
		String[] split = nomeCompleto.split(" ");
		return split[split.length - 1];
	}

	public static String fullLastName(String nomeCompleto) {
		int fistIndex = nomeCompleto.indexOf(" ");
		String sobreNome = nomeCompleto.substring(fistIndex + 1, nomeCompleto.length());
		return sobreNome;
	}

	public static String createUserName(String fullName) {
		String userName = Util.createString("%s.%s", Util.fistName(fullName), Util.lastName(fullName));
		return userName.toLowerCase();
	}

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
	
	public static byte[] converteStringToByteArray(String password) throws UnsupportedEncodingException {
	    String newQuotedPassword = "\"" + password + "\"";
	    return newQuotedPassword.getBytes("UTF-16LE");
	}
}
