package com.davidbentolila.tutorial.rest.api.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.davidbentolila.tutorial.rest.api.entity.Person;

@Path("/person")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PersonService {

	private static Map<Integer, Person> persons = new HashMap<Integer, Person>();

	@POST
	@Path("/")
	public ApiResponse createPerson(Person p) {
		ApiResponse apiResponse = new ApiResponse();

		if (p.getName() == null || p.getName().isEmpty()) {
			apiResponse.setStatus(false);
			apiResponse.setMessage("O nome é obrigatório.");
			return apiResponse;
		}

		p.setId(persons.size());
		persons.put(p.getId(), p);
		apiResponse.setStatus(true);
		apiResponse.setMessage("Pessoa criada com sucesso.");
		apiResponse.setPerson(p);
		return apiResponse;
	}

	@GET
	@Path("/{id}")
	public Person getPerson(@PathParam("id") int id) {
		return persons.get(id);
	}

	@PUT
	@Path("/{id}")
	public ApiResponse updatePerson(@PathParam("id") int id, Person p) {
		ApiResponse apiResponse = new ApiResponse();

		Person pu = persons.get(id);
		if (pu == null) {
			apiResponse.setStatus(false);
			apiResponse.setMessage("Pessoa não encontrada.");
			return apiResponse;
		}

		pu.setAddress(p.getAddress());
		pu.setName(p.getName());
		pu.setAge(p.getAge());

		apiResponse.setStatus(true);
		apiResponse.setMessage("Pessoa atualizada com sucesso.");
		return apiResponse;
	}

	@DELETE
	@Path("/{id}")
	public ApiResponse deletePerson(@PathParam("id") int id) {
		ApiResponse apiResponse = new ApiResponse();
		if (persons.get(id) == null) {
			apiResponse.setStatus(false);
			apiResponse.setMessage("Pessoa não encontrada.");
			return apiResponse;
		}
		persons.remove(id);
		apiResponse.setStatus(true);
		apiResponse.setMessage("Pessoa removida com sucesso.");
		return apiResponse;
	}

	@GET
	@Path("/all")
	public Collection<Person> getAllPersons() {
		return persons.values();
	}

}