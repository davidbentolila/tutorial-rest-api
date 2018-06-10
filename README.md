# tutorial-rest-api
Este tutorial é basicamente um agregado de vários tutoriais que utilizei até alcançar o resultado esperado. Com o passar o tempo irei incluir novas funcionalidades.

Versão | Data | Detalhes
------- | ------------ | --------------------------
1.0.1 | 2018-06-10 | Criando o serviço Person
1.0.0 | 2018-06-10 | Criando um rest Básico

## Pré-requisitos
* [Eclipse IDE](http://www.eclipse.org)
* [Java 1.8+](http://www.oracle.com/technetwork/pt/java/javase/downloads/jdk8-downloads-2133151.html)
* [Apache tomcat 8+](http://tomcat.apache.org/)

[TOCM]

## Criando o Rest

No Eclipse clique em *File* -> *New* -> *Maven Project*.
Na primeira tela deixe como estã e clique em *Next* 
Na tela seguinte filtre por jersey-quick e escolha a opção com a versão 2.27 e clique em *Next*, como na imagem abaixo

![](https://image.ibb.co/f5f7V8/imagem1.png)

Caso não encontre o artefato em questão, verifique esta resposta no [stackoverflow](https://stackoverflow.com/a/29116241/3330253).

Finalmente, é necessário preencher os campos abaixo:

![](https://image.ibb.co/mxHXxo/imagem2.png)

"Group Id":  Este será um nome único, separado por pontos, Desta forma, outros projetos poderão referenciar o seu projeto através do Maven. Normalmente é preenchido com um domínio.

"Artifact Id": Este é o nome do projeto em si.

"Package": será o nome do pacote principal do projeto. Comumente formado pela união do “Group Id” + “Artifact Id”

Então clique em *Finish*.

Esta será a estrutura do projeto criado.

![](https://image.ibb.co/kGcXxo/imagem3.png)

### Arquivos importantes
* **pom.xml**

Como utilizamos o jersey archetype, ele automaticamente as dependências que o projeto precisa para rodar. Estas dependencias são incluídas no pom.xml.

* **web.xml**

Neste arquivo é importante destacar 2 pontos.
1. na tag **init-param** é indicado o nosso pacote padrão.
```xml
<init-param>
    <param-name>jersey.config.server.provider.packages</param-name>
    <param-value>com.davidbentolila.tutorial.rest.api</param-value>
</init-param>
```
2. na tag **servlet-mapping** é indicado qual será o nosso padrão de URL para invocar o REST. Neste caso será /webapi

```xml
<servlet-mapping>
    <servlet-name>Jersey Web Application</servlet-name>
    <url-pattern>/webapi/*</url-pattern>
</servlet-mapping>
```

* **MyResource.java**

Finalmente, a classe responsável por disponibilizar um recurso REST.
```java
/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
 
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Got it!";
    }
}
```

- *@Path*: Esta anotação define qual o caminho do recurso a ser consumido.

- @GET: Define o metodo será chamado quando for feita uma requisição do tipo GET.

- @Produces(MediaType.TEXT_PLAIN): O tipo que será retornado como resultado da requisição. Neste caso um texto.

### Testando o serviço
Para testarmos o nosso REST, é necessário ter o tomcat configurado no eclipse (caso ainda não o tenha, verifique este [guia](https://www.programmergate.com/setup-tomcat-eclipse/)).

Agora, o primeiro passo é garantirmos que o projeto já possui todas as dependências necessárias. Para isto, clique com o botão direito do mouse no projeto e em seguida 
Maven -> *Update Project*. Isto fará com que o Maven baixe todas as dependências do projeto.

Após baixar as dependências vamos buildar o projeto. Basta clicar com o direito do mouse no projeto ->: Run As -> maven build
Em Goals, digite **clean install**

![](https://image.ibb.co/fhHXxo/imagem4.png)

Se tudo correu como esperado o resultado será algo como a imagem abaixo.

![](https://image.ibb.co/mSO5Ho/imagem5.png)

Agora, é necessário adicionar a aplicação ao tomcat.

![](https://image.ibb.co/gQvwOT/imagem6.png)

![](https://image.ibb.co/dTD5Ho/imagem7.png)

Agora clique com o direito no Tomcat e Cliente em Start. Após iniciado, basta acessar o link abaixo para testar.

[http://localhost:8080/tutorial-rest-api/webapi/myresource](http://localhost:8080/tutorial-rest-api/webapi/myresource)


## Incrementando o serviço
Agora que conseguimos fazer um REST básico, vamos melhorar um pouco disponibilizando todos os métodos necessários para um CRUD (**C**reate, **R**ead, **U**pdate e **D**elete).

### Criando a entidade Person

A classe Person representa a entidade que serã exposta através do REST. Para isso, precisamos criar uma classe chamada Person.

Clique com o botão direito do mouse sobre o projeto, *New* -> *Class*.

Em **Package** coloque *com.davidbentolila.tutorial.rest.api.entity* e em **Name** Coloque **Person**. Então, clique em *Finish*.

A Classe deve estar como abaixo.

```java
package com.davidbentolila.tutorial.rest.api.entity;

public class Person {

	private Integer id;
	private String name;
	private Integer age;
	private String address;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return id + " : " + name + " : " + age + " : " + address;
	}

}
```

###Mensagens de Retorno

Para o meu projeto, eu escolhi retornar mensagens para algumas chamadas. Pra isso, eu criei uma classe **ApiResponse**

Clique com o botão direito do mouse sobre o projeto, *New* -> *Class*.

Em **Package** coloque *com.davidbentolila.tutorial.rest.api.service* e em **Name** Coloque **ApiResponse**. Então, clique em *Finish*.

```java
package com.davidbentolila.tutorial.rest.api.service;

import com.davidbentolila.tutorial.rest.api.entity.Person;

public class ApiResponse {
	private boolean status;
	private String message;
	private Person person;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
```
Esta classe é bem simples. Basicamente possui:

* **Status**: Sucesso ou Erro

* **Message**: Uma mensagem de retorno informando sucesso ou o motivo do erro.

* **Person**: A Entidade Person envolvida na requisição (se for o caso).

###Serviço Pessoa

Agora, criaremos a classe do serviço que irá trabalhar com esta entidade.

Clique com o botão direito do mouse sobre o projeto, *New* -> *Class*.

Em **Package** coloque *com.davidbentolila.tutorial.rest.api.service* e em **Name** Coloque **PersonService**. Então, clique em *Finish*.

Por se tratar de um serviço, é necessário indicarmos o Path com o qual ele será acionado. Além disso, incluí as Anotações *@Consumes* e *@Produces* que indica que o serviço irá receber e enviar dados no formato JSON.

```java
@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
```

Para habilitar o formato JSON, será necessário alterarmos o **pom.xml**, incluindo esta dependência. Para isto, basta descomentar o trecho abaixo. Após isso, é necessário atualizar as dependências MAVEN do projeto, clique com o botão direito do mouse no projeto e em seguida 
Maven -> *Update Project*

```xml
        <!-- uncomment this to get JSON support
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-json-binding</artifactId>
        </dependency>
        -->
```
        
Os métodos desta classe também foram anotados com os tipos de requisição (*@POST, @GET, @PUT e @DELETE*) e o caminho da url para o qual responderão (*@Path*). Além disso, alguns deles também receberão algum tipo de parametro (*@PathParam*) através da URL.

```java
	@POST
	@Path("/")
	public ApiResponse createPerson(Person p) { ... }

	@GET
	@Path("/{id}")
	public Person getPerson(@PathParam("id") int id) { ... }

	@PUT
	@Path("/{id}")
	public ApiResponse updatePerson(@PathParam("id") int id, Person p) { ... }

	@DELETE
	@Path("/{id}")
	public ApiResponse deletePerson(@PathParam("id") int id) { ... }

	@GET
	@Path("/all")
	public Collection<Person> getAllPersons() { ... }

}
```

Ou seja, para excluir uma pessoa, basicamente deveria chamar *http://seudominio/webapi/123* utilizando uma requisição DELETE. Onde *webapi* é a URL do nosso REST (mapeada no arquivo **web.xml**, *123* é o **id** (parâmetro do nosso método **deletePerson**).


###Testando o Serviço Person
Disto isto, podemos fazer chamadas para a nossa API.

Para fins de teste, criei um Map na classe **PersonService** para simular o nosso banco de dados.

Para fins de teste, utilizaremos o Google Chrome para realizar nossas requisições utilizando Javascript. Para isto, utilizaremos o console do navegador. Acessado conforme imagem abaixo.

![](https://image.ibb.co/czJOOT/imagem9.png)

####Obtendo todos as pessoas cadastradas
```javascript
var xhr = new XMLHttpRequest();
var url = 'http://localhost:8080/tutorial-rest-api/webapi/person/all';
var params = [];
xhr.open('GET', url, true);
xhr.setRequestHeader("Content-type", "application/json");
xhr.onreadystatechange = function() {
     console.log(xhr.responseText);
}
xhr.send(params);
```

Como não temos nenhuma pessoa criada ele retorna um array vazio *[]*.

![](https://image.ibb.co/cYKBco/imagem10.png)

####Criando
```javascript
var xhr = new XMLHttpRequest();
var url = 'http://localhost:8080/tutorial-rest-api/webapi/person';
var params = '{"name":"Nome1", "age":26, "address":"Rua dos bobos, no 0"}';
xhr.open('POST', url, true);
xhr.setRequestHeader("Content-type", "application/json");
xhr.onreadystatechange = function() {
     console.log(xhr.responseText);
}
xhr.send(params);
```

O retorno desta chamada será:

```json
{"message":"Pessoa criada com sucesso.","person":{"address":"Rua dos bobos, no 0","age":26,"id":0,"name":"Nome1"},"status":true}
```

Note que, além da mensagem de sucesso, é retornada a Pessoa criada, já com o **id** atribuído à ela. 

####Recuperando
De posse da **id** criada, podemos obter os dados dessa pessoa através da chamada abaixo

```javascript
var xhr = new XMLHttpRequest();
var url = 'http://localhost:8080/tutorial-rest-api/webapi/person/0';
var params = [];
xhr.open('GET', url, true);
xhr.setRequestHeader("Content-type", "application/json");
xhr.onreadystatechange = function() {
      console.log(xhr.responseText);
}
xhr.send(params);
```

####Atualizando dados

```javascript
var xhr = new XMLHttpRequest();
var url = 'http://localhost:8080/tutorial-rest-api/webapi/person/0';
var params = '{"address":"Novo endereço","age":26,"name":"Novo nome"}';
xhr.open('PUT', url, true);
xhr.setRequestHeader("Content-type", "application/json");
xhr.onreadystatechange = function() {
      console.log(xhr.responseText);
}
xhr.send(params);
```

####Removendo
```javascript
var xhr = new XMLHttpRequest();
var url = 'http://localhost:8080/tutorial-rest-api/webapi/person/0';
var params = '';
xhr.open('DELETE', url, true);
xhr.setRequestHeader("Content-type", "application/json");
xhr.onreadystatechange = function() {
      console.log(xhr.responseText);
}
xhr.send(params);
```