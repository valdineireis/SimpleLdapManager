package br.com.nocodigo.simpleldapmanager.model;

import java.lang.reflect.Field;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;

/**
 * Objeto responsável por refletir os atributos dos usuários
 * A definicao do nome dos atributos sao referentes aos mesmos utilizados no Active Directory
 * @author Valdinei Reis (valdinei@nocodigo.com)
 * @since 18/02/2016
 *
 */
public class LdapUser {
	
	private String objectClass;

	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	
	private String cn;
	private String sAMAccountName;
	private String givenName;
	private String initials;
	private String sn;
	private String physicalDeliveryOfficeName;
	private String mail;
	private String userPrincipalName;
	private String displayName;
	/**
	 * Title of the organization
	 */
	private String title;
	private String department;
	private String company;
	private String description;
	private String telephoneNumber;
	private int userAccountControl;
	
	private String mailNickName;
	private String memberOf;
	private String streetAddress;
	private String postOfficeBox;
	/**
	 * City
	 */
	private String l;
	/**
	 * State
	 */
	private String st;
	private String postalCode;
	/**
	 * Country
	 */
	private String co;
	/**
	 * Country 2 Digit Code
	 */
	private String c;
	private String countryCode;
	private String wWWHomePage;

	private String extract(Attribute attribute) throws NamingException {
		String result = "";
		
		if (attribute != null)
			result = attribute.get().toString();
		
		return result;
	}
	
	private boolean isString(Field field) {
		return field.getType().getSimpleName().toLowerCase().endsWith("string");
	}
	
	private boolean isInt(Field field) {
		String type = field.getType().getSimpleName().toLowerCase();
		return type.endsWith("int") || type.endsWith("integer");
	}
	
	private void set(Field field, Attributes attributes) throws NamingException {
		try {
			String attributeExtracted = extract(attributes.get( field.getName() ));
			
			if(isString(field)) {
				field.set(this, attributeExtracted);
			}
			else if(isInt(field)) {
				int intValue = attributeExtracted.isEmpty() ? 0 : Integer.parseInt(attributeExtracted);
				field.set(this, intValue);
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("[{");
		
		Class<? extends LdapUser> classe = this.getClass();
		
		int totalDeFields = classe.getDeclaredFields().length;
		
		for (int i = 0; i < totalDeFields; i++) {
			Field field = classe.getDeclaredFields()[i];
			try {
				result.append(field.getName());
				result.append(":");
				
				if (isString(field)) 
					result.append("\"");
				
				result.append(field.get(this));
				
				if (isString(field)) 
					result.append("\"");
				
				if (i != totalDeFields - 1)
					result.append(",");
				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		
		result.append("}]");
		
		return result.toString();
	}
	
	public LdapUser extractUser(SearchResult searchResult) throws NamingException {
		
		if (searchResult == null)
			throw new IllegalArgumentException("The SearchResult can not be null.");
		
		Attributes attributes = searchResult.getAttributes();
		
		Class<? extends LdapUser> classe = this.getClass();
		for (Field field : classe.getDeclaredFields()) {
			set(field, attributes);
		}
		
		return this;
	}
	
	public boolean isActive() {
		// o código 512 quer dizer que o usuário está ativo no servidor
        // o código 66048 quer dizer que o usuário está ativo e a senha nunca expira no servidor
        // ======================================================================================
        // Tabela de Códigos
        // ======================================================================================
        // 512   	Enabled Account
        // 514   	Disabled Account
        // 544   	Enabled, Password Not Required
        // 546   	Disabled, Password Not Required
        // 66048	Enabled, Password Doesn't Expire
        // 66050	Disabled, Password Doesn't Expire
        // 66080	Enabled, Password Doesn't Expire & Not Required
        // 66082	Disabled, Password Doesn't Expire & Not Required
        // 262656	Enabled, Smartcard Required
        // 262658	Disabled, Smartcard Required
        // 262688	Enabled, Smartcard Required, Password Not Required
        // 262690	Disabled, Smartcard Required, Password Not Required
        // 328192	Enabled, Smartcard Required, Password Doesn't Expire
        // 328194	Disabled, Smartcard Required, Password Doesn't Expire
        // 328224	Enabled, Smartcard Required, Password Doesn't Expire & Not Required
        // 328226	Disabled, Smartcard Required, Password Doesn't Expire & Not Required
        // ======================================================================================
        // tabela de códigos consultada do site: http://www.netvision.com/ad_useraccountcontrol.php
		if (userAccountControl == 512 || userAccountControl == 66048)
			return true;
		
		return false;
	}
	
	public String getCn() {
		return cn;
	}
	public void setCn(String cn) {
		this.cn = cn;
	}
	public String getsAMAccountName() {
		return sAMAccountName;
	}
	public void setsAMAccountName(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getInitials() {
		return initials;
	}
	public void setInitials(String initials) {
		this.initials = initials;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getPhysicalDeliveryOfficeName() {
		return physicalDeliveryOfficeName;
	}
	public void setPhysicalDeliveryOfficeName(String physicalDeliveryOfficeName) {
		this.physicalDeliveryOfficeName = physicalDeliveryOfficeName;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getUserPrincipalName() {
		return userPrincipalName;
	}
	public void setUserPrincipalName(String userPrincipalName) {
		this.userPrincipalName = userPrincipalName;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}
	public int getUserAccountControl() {
		return userAccountControl;
	}
	public void setUserAccountControl(int userAccountControl) {
		this.userAccountControl = userAccountControl;
	}

	public String getMailNickName() {
		return mailNickName;
	}
	public void setMailNickName(String mailNickName) {
		this.mailNickName = mailNickName;
	}
	public String getMemberOf() {
		return memberOf;
	}
	public void setMemberOf(String memberOf) {
		this.memberOf = memberOf;
	}
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getPostOfficeBox() {
		return postOfficeBox;
	}
	public void setPostOfficeBox(String postOfficeBox) {
		this.postOfficeBox = postOfficeBox;
	}
	public String getL() {
		return l;
	}
	public void setL(String l) {
		this.l = l;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCo() {
		return co;
	}
	public void setCo(String co) {
		this.co = co;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getwWWHomePage() {
		return wWWHomePage;
	}
	public void setwWWHomePage(String wWWHomePage) {
		this.wWWHomePage = wWWHomePage;
	}
}
