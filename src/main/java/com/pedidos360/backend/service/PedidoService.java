package com.pedidos360.backend.service;

import com.pedidos360.backend.model.Pedido;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final Map<Long, Pedido> inventarioPedidos = new ConcurrentHashMap<>();
    private final AtomicLong secuenciaId = new AtomicLong(0);

    @PostConstruct
    public void cargarDatosEjemplo() {
        crear(new Pedido(null, "Empresa Alfa Ltda.", new BigDecimal("150000.00"), "PROCESANDO"));
        crear(new Pedido(null, "Juan Pérez", new BigDecimal("45990.00"), "COMPLETADO"));
    }

    public List<Pedido> listarTodos() {
        return inventarioPedidos.values().stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .collect(Collectors.toList());
    }

    public Pedido buscarPorId(Long id) {
        Pedido pedido = inventarioPedidos.get(id);
        if (pedido == null) {
            throw new PedidoNoEncontradoException(id);
        }
        return pedido;
    }

    public Pedido crear(Pedido nuevo) {
        long id = secuenciaId.incrementAndGet();
        nuevo.setId(id);
        inventarioPedidos.put(id, nuevo);
        return nuevo;
    }

    public Pedido actualizar(Long id, Pedido datosActualizados) {
        Pedido pedidoExistente = buscarPorId(id);
        pedidoExistente.setCliente(datosActualizados.getCliente());
        pedidoExistente.setTotal(datosActualizados.getTotal());
        pedidoExistente.setEstado(datosActualizados.getEstado());
        inventarioPedidos.put(id, pedidoExistente);
        return pedidoExistente;
    }

    public void eliminar(Long id) {
        if (!inventarioPedidos.containsKey(id)) {
            throw new PedidoNoEncontradoException(id);
        }
        inventarioPedidos.remove(id);
    }
}