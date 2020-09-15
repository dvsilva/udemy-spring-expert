package io.github.dougllasfps.service;

import java.util.Optional;

import io.github.dougllasfps.domain.entity.Pedido;
import io.github.dougllasfps.domain.entity.enums.StatusPedido;
import io.github.dougllasfps.rest.dto.PedidoDTO;

public interface PedidoService {

	Pedido salvar(PedidoDTO dto);

	Optional<Pedido> obterPedidoCompleto(Integer id);
	
    void atualizaStatus(Integer id, StatusPedido statusPedido);
	
}