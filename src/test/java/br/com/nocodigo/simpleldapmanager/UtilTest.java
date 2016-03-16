package br.com.nocodigo.simpleldapmanager;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilTest {

	@Test
	public void test() {
		String result = Util.createString("%s %s %s %s", "Valdinei", "Reis", "da", "Silva");
		assertEquals("Valdinei Reis da Silva", result);
	}

}
