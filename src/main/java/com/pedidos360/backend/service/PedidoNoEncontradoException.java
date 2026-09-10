package com.pedidos360.backend.service;

public class PedidoNoEncontradoException extends RuntimeException {
    public PedidoNoEncontradoException(Long id) {
        super("No existe un pedido asociado al ID: " + id);
    }
}