# SimpleLdapManager
Simplificando o Gerenciamento das Informações do Active Directory

## Utilizando conexão SSL

Para utilizar a conexão via protocolo LDAPS (SSL), será necessário importar o certificado de segurança configurado no servidor do Active Directory, para o servidor que irá rodar a aplicação.

Para isso utilize o seguinte comando:

+ No Linux utilize o sudo ou yum
+ No Windows abra o terminal como administrador

```sh
keytool -import -v -trustcacerts -alias INFORME-UM-APELIDO -file "INFORME-O-CAMINHO-COMPLETO-DO-CERTIFICADO\NOME-DO-ARQUIVO.cer" -keystore "INFORME-O-CAMINHO-COMPLETO-DA-INSTALACAO-DO-JAVA\jre\lib\security\cacerts" -keypass changeit -storepass changeit
```

Para listar todos os certificados:

```
keytool -list -keystore "INFORME-O-CAMINHO-COMPLETO-DA-INSTALACAO-DO-JAVA\jre\lib\security\cacerts"
```

Para remover o certificado:

```sh
keytool -delete -alias INFORME-O-APELIDO -keystore "INFORME-O-CAMINHO-COMPLETO-DA-INSTALACAO-DO-JAVA\jre\lib\security\cacerts"
```

+ Para facilitar, existe uma ferramenta chamada [Portecle](http://portecle.sourceforge.net/).

## Configuração

+ Edite o arquivo ```ldap.properties``` informando as informações do servidor.
+ Para manipular as informações (exemplo: reset de senha e ativação de conta), o usuário deve estar no grupo de administradores ou ter permissões equivalentes.

## Exemplos de código

+ Pacote
```java
import br.com.nocodigo.simpleldapmanager.*;
```

+ Implementação
```java
// Instancia o Objeto responsável por manipular as informações
LdapManager ldapManager = new LdapManager();

// Método para autenticação
ldapManager.verifyCredentials(LOGIN, PASSWORD);

// Busca todos os usuários pela OU
ListUsers users = ldapManager.selectAllByOu(OU);
for (LdapUser user : users.getAll()) {
	System.out.println(user.toString());
}

// Busca um usuário pelo login (AccountName)
LdapUser user = ldapManager.selectByAccountName(LOGIN);
System.out.println(user.toString());

// Reseta a senha do usuário
ldapManager.resetPassword(LOGIN, PASSWORD, NEW_PASSWORD);

// Remoção de conta
ldapManager.deleteAccount(ACCOUNT_NAME);
```
