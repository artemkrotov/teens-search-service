package ru.krotov.teenssearchservice.configurations.exceptions;

public class InvalidUserException extends RuntimeException {

	public InvalidUserException(String message) {
		super(message);
	}
}
