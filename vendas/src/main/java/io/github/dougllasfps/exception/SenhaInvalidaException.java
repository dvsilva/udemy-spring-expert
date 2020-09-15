package io.github.dougllasfps.exception;

public class SenhaInvalidaException extends RuntimeException {
	
	private static final long serialVersionUID = 5913521007587417477L;

	public SenhaInvalidaException() {
		super("Senha inválida");
	}
	
}