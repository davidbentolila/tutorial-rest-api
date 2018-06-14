package com.davidbentolila.tutorial.rest.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import com.davidbentolila.tutorial.rest.api.entity.Person;

public class PersonServiceTest extends JerseyTest {

	@Override
	public Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(PersonService.class);
	}

	@Test
	public void criarUmaPessoa() {
		Person p = new Person();
		p.setName("Fulano");
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		Response output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK
		ApiResponse response = output.readEntity(ApiResponse.class);
		assertNotNull("Deve retormar uma pessoa", response.getPerson());
		assertNotNull("A pessoa deve possuir um ID", response.getPerson().getId());
	}

	@Test
	public void criarUmaPessoaSemNome() {
		Person p = new Person();
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		Response output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		ApiResponse response = output.readEntity(ApiResponse.class);
		assertNull("**Não** deve retornar uma pessoa", response.getPerson());
		assertFalse("Deve retornar false (erro)", response.isStatus());
		assertTrue("Deve retornar sobre nome", response.getMessage().equals("O nome é obrigatório."));
	}

	@Test
	public void recuperaUmaPessoaComUmIdExistente() {
		Person p = new Person();
		p.setName("Fulano");
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		Response output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		ApiResponse response = output.readEntity(ApiResponse.class);
		Integer id = response.getPerson().getId();

		output = target("/person/" + id).request().get();
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		Person p2 = output.readEntity(Person.class);
		assertNotNull("Deve retornar uma pessoa", p2);
		assertEquals("O id deve ser o mesmo que eu solicitei", id, p2.getId());

		// A pessoa retornada deve ser igual a que eu criei anteriormente
		assertEquals("mesmo endereço", p.getAddress(), p2.getAddress());
		assertEquals("mesma idade", p.getAge(), p2.getAge());
		assertEquals("mesmo nome", p.getName(), p2.getName());
	}

	@Test
	public void recuperaUmaPessoaComUmIdNAOExistente() {

		Response output = target("/person/9999999").request().get();
		assertEquals("deve retornar status 204", 204, output.getStatus());// No Content

	}

	@Test
	public void atualizarUmaPessoaExistente() {
		Person p = new Person();
		p.setName("Fulano");
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		Response output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		ApiResponse response = output.readEntity(ApiResponse.class);
		Integer id = response.getPerson().getId();

		output = target("/person/" + id).request().get();
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		Person p2 = output.readEntity(Person.class);
		assertEquals("mesmo nome", p.getName(), p2.getName());

		p2.setName("Beltrano");

		output = target("/person/" + id).request().put(Entity.json(p2));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		output = target("/person/" + id).request().get();
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		Person p3 = output.readEntity(Person.class);
		assertEquals("nome foi alterado", "Beltrano", p3.getName());
	}

	@Test
	public void atualizarUmaPessoaExistente2() {
		Person p = new Person();
		p.setName("Fulano");
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		Response output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		ApiResponse response = output.readEntity(ApiResponse.class);
		Integer id = response.getPerson().getId();

		output = target("/person/" + id).request().get();
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		Person p2 = output.readEntity(Person.class);
		assertEquals("mesmo nome", p.getName(), p2.getName());

		p2.setName("Beltrano");

		output = target("/person").request().put(Entity.json(p2));
		assertEquals("deve retornar status 405", 405, output.getStatus());// Method Not Allowed
	}

	@Test
	public void removerUmaPessoaExistente() {
		Person p = new Person();
		p.setName("Fulano");
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		Response output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		ApiResponse response = output.readEntity(ApiResponse.class);
		Integer id = response.getPerson().getId();

		output = target("/person/" + id).request().delete();
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		response = output.readEntity(ApiResponse.class);

		assertTrue("Deve retornar true (sucesso)", response.isStatus());
		assertTrue("Deve retornar pessoa nao encontrada", response.getMessage().equals("Pessoa removida com sucesso."));

	}

	@Test
	public void removerUmaPessoaNaoExistente() {

		Response output = target("/person/9999").request().delete();
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK

		ApiResponse response = output.readEntity(ApiResponse.class);

		assertFalse("Deve retornar false (erro)", response.isStatus());
		assertTrue("Deve retornar sobre nome", response.getMessage().equals("Pessoa não encontrada."));
	}

	@Test
	public void removerUmaPessoaSemInformarId() {
		Response output = target("/person").request().delete();
		assertEquals("deve retornar status 200", 405, output.getStatus());// Method Not Allowed
	}

	
	@SuppressWarnings("unchecked")
	@Test
	public void obterTodasAsPessoas() {
		Response output = target("/person/all").request().get();
		List<Person> pessoas = output.readEntity(List.class);
		Integer quantidadePessoas = pessoas.size();
		
		Person p = new Person();
		p.setName("Fulano");
		p.setAge(41);
		p.setAddress("Rua nova, numero 1");

		output = target("/person").request().post(Entity.json(p));
		assertEquals("deve retornar status 200", 200, output.getStatus());// OK
		
		output = target("/person/all").request().get();
		pessoas = output.readEntity(List.class);
		
		assertTrue("Deve incrementar em 1 o numero de pessoas retornadas", pessoas.size() == quantidadePessoas + 1);
	}
	   
}
