package br.com.nocodigo.simpleldapmanager.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import br.com.nocodigo.simpleldapmanager.Connection;
import br.com.nocodigo.simpleldapmanager.Util;
import br.com.nocodigo.simpleldapmanager.model.LdapUser;

public class SetAttributeAction {

	private Map<String, Object> attributes = new HashMap<>();
	private String entryDN;
	private final Connection connection;
	
	public SetAttributeAction(
			Connection connection,
			LdapUser user,
			String organizationalUnit,
			String domainComponent) {
		this.connection = connection;
		this.entryDN = Util.createString("CN=%s,%s,%s", user.getCn(), organizationalUnit, domainComponent);
	}
	
	public void add(String attributeName, Object value) {
		attributes.put(attributeName, value);
	}
	
	public void execute() throws NamingException {
		
		ModificationItem[] modificationItem = new ModificationItem[attributes.size()];
		
		Iterator<Entry<String, Object>> entries = attributes.entrySet().iterator();
		int i = 0;
		
		while (entries.hasNext()) {
			Entry<String, Object> attribute = (Entry<String, Object>) entries.next();
			
			modificationItem[i] = new ModificationItem(
					DirContext.REPLACE_ATTRIBUTE, 
					new BasicAttribute(attribute.getKey(), attribute.getValue()));
			i++;
		}
		
		this.connection.getDirContext().modifyAttributes(this.entryDN, modificationItem);
	}
}
