# tutorial-rest-api
Este tutorial é basicamente um agregado de vários tutoriais que utilizei até alcançar o resultado esperado. Com o passar o tempo irei incluir novas funcionalidades.

Versão | Data | Detalhes
------- | ------------ | --------------------------
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

Agora, o primeiro passo é garantirmos que o projeto já possui todas as dependências necessárias. Para isto, clique com o botão direito do mouse no projeto e em seguinda 
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
