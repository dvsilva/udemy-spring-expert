package io.github.dougllasfps.exception;

public class PedidoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = -490060587319452238L;

	public PedidoNaoEncontradoException() {
        super("Pedido não encontrado.");
    }
}