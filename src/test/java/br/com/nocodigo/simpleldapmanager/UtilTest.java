package br.com.nocodigo.simpleldapmanager;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

	@Test
	public void deveCriarUmaStringComBaseEmUmaExpressao() {
		String result = Util.createString("%s %s %s %s", "Valdinei", "Reis", "da", "Silva");
		assertEquals("Valdinei Reis da Silva", result);
	}
	
	@Test
	public void devePegarOPrimeiroNome() {
		String nomeCompleto = "Valdinei Reis da Silva";
		String resultado = Util.fistName(nomeCompleto);
		
		assertEquals("Valdinei", resultado);
	}
	
	@Test
	public void devePegarOPrimeiroNomePassandoUmNomeSimples() {
		String nomeCompleto = "Valdinei";
		String resultado = Util.fistName(nomeCompleto);
		
		assertEquals("Valdinei", resultado);
	}
	
	@Test
	public void devePegarOUltimoNome() {
		String nomeCompleto = "Valdinei Reis da Silva";
		String resultado = Util.lastName(nomeCompleto);
		
		assertEquals("Silva", resultado);
	}
	
	@Test
	public void devePegarOUltimoNomePassandoUmNomeSimples() {
		String nomeCompleto = "Silva";
		String resultado = Util.lastName(nomeCompleto);
		
		assertEquals("Silva", resultado);
	}
	
	@Test
	public void devePegarOSebreNome() {
		String nomeCompleto = "Valdinei Reis da Silva";
		String resultado = Util.fullLastName(nomeCompleto);
		
		assertEquals("Reis da Silva", resultado);
	}
	
	@Test
	public void devePegarOSebreNomePassandoUmNomeSimples() {
		String nomeCompleto = "Silva";
		String resultado = Util.fullLastName(nomeCompleto);
		
		assertEquals("Silva", resultado);
	}
	
	@Test
	public void deveCriarOUserNameJuntandoOFirsNameEOLastName() {
		String nomeCompleto = "Valdinei Reis da Silva";
		String resultado = Util.createUserName(nomeCompleto);
		
		assertEquals("valdinei.silva", resultado);
	}
	
	@Test
	public void deveExtrairAsIniciaisNoNome() {
		String valdinei = Util.extractInitials("Valdinei Reis da Silva");
		String aline = Util.extractInitials("Aline Bulhoes De Morais");
		String edimar = Util.extractInitials("Edimar Benetti");
		String Iagles = Util.extractInitials("Iaglessilma Pinto Dos Santos");
		String joana = Util.extractInitials("Joana Martins e Mendonca Sodre");
		String mariana = Util.extractInitials("Mariana Di Cavalcanti Gomes");
		
		assertEquals("VRS", valdinei);
		assertEquals("ABM", aline);
		assertEquals("EB", edimar);
		assertEquals("IPS", Iagles);
		assertEquals("JMMS", joana);
		assertEquals("MDCG", mariana);
	}

}
