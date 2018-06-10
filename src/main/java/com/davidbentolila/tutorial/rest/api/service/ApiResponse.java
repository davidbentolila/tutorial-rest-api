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