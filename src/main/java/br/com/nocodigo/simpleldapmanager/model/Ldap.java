package br.com.nocodigo.simpleldapmanager.model;

/**
 * Some useful constants from lmaccess.h
 * @author Valdinei Reis (valdinei@nocodigo.com)
 *
 */
public enum Ldap {
	
	UF_ACCOUNTDISABLE(0x0002),
	UF_PASSWD_NOTREQD(0x0020),
	UF_PASSWD_CANT_CHANGE(0x0040),
	UF_NORMAL_ACCOUNT(0x0200),
	UF_DONT_EXPIRE_PASSWD(0x10000),
	UF_PASSWORD_EXPIRED(0x800000);

	private int value;
	
	Ldap(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public static String getEnabledAccountCode() {
		int codigo = Ldap.UF_NORMAL_ACCOUNT.getValue() + Ldap.UF_PASSWORD_EXPIRED.getValue();
		return Integer.toString(codigo);
	}
	
}
