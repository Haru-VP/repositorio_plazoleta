package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.CantidadPlatoInvalidaException;
import com.pragma.powerup.domain.exception.ClienteTienePedidoActivoException;
import com.pragma.powerup.domain.exception.ParametrosPaginacionInvalidosException;
import com.pragma.powerup.domain.exception.PedidoNoEncontradoException;
import com.pragma.powerup.domain.exception.PedidoNoEstaEnEstadoPendienteException;
import com.pragma.powerup.domain.exception.PedidoNoEstaEnPreparacionException;
import com.pragma.powerup.domain.exception.PedidoNoEstaListoException;
import com.pragma.powerup.domain.exception.PedidoNoSePuedeCancelarException;
import com.pragma.powerup.domain.exception.PinInvalidoException;
import com.pragma.powerup.domain.exception.PedidoSinPlatosException;
import com.pragma.powerup.domain.exception.PlatoInactivoException;
import com.pragma.powerup.domain.exception.PlatoNoEncontradoException;
import com.pragma.powerup.domain.exception.PlatosDeDiferenteRestauranteException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.exception.UsuarioNoAutorizadoException;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.spi.IAuthenticatedUserPort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IMensajeriaServicePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITrazabilidadServicePort;
import com.pragma.powerup.domain.spi.IUserValidationServicePort;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IAuthenticatedUserPort authenticatedUserPort;
    private final IUserValidationServicePort userValidationServicePort;
    private final IMensajeriaServicePort mensajeriaServicePort;
    private final ITrazabilidadServicePort trazabilidadServicePort;
    private final SecureRandom secureRandom = new SecureRandom();

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IAuthenticatedUserPort authenticatedUserPort,
                        IUserValidationServicePort userValidationServicePort,
                        IMensajeriaServicePort mensajeriaServicePort,
                        ITrazabilidadServicePort trazabilidadServicePort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
        this.userValidationServicePort = userValidationServicePort;
        this.mensajeriaServicePort = mensajeriaServicePort;
        this.trazabilidadServicePort = trazabilidadServicePort;
    }

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IAuthenticatedUserPort authenticatedUserPort,
                        IUserValidationServicePort userValidationServicePort,
                        IMensajeriaServicePort mensajeriaServicePort) {
        this(orderPersistencePort, restaurantPersistencePort, dishPersistencePort,
                authenticatedUserPort, userValidationServicePort, mensajeriaServicePort, null);
    }

    @Override
    public void realizarPedido(OrderModel orderModel) {
        Long idCliente = authenticatedUserPort.obtenerIdUsuarioAutenticado();
        if (idCliente == null) {
            throw new UsuarioNoAutorizadoException();
        }

        if (orderPersistencePort.existePedidoActivoPorCliente(idCliente)) {
            throw new ClienteTienePedidoActivoException();
        }

        if (orderModel.getRestaurante() == null || orderModel.getRestaurante().getId() == null
                || !restaurantPersistencePort.existePorId(orderModel.getRestaurante().getId())) {
            throw new RestauranteNoEncontradoException();
        }
        Long idRestaurante = orderModel.getRestaurante().getId();

        List<OrderDishModel> pedidosPlatos = orderModel.getPedidosPlatos();
        if (pedidosPlatos == null || pedidosPlatos.isEmpty()) {
            throw new PedidoSinPlatosException();
        }

        for (OrderDishModel pedidoPlato : pedidosPlatos) {
            if (pedidoPlato.getCantidad() == null || pedidoPlato.getCantidad() <= 0) {
                throw new CantidadPlatoInvalidaException();
            }

            if (pedidoPlato.getPlato() == null || pedidoPlato.getPlato().getId() == null) {
                throw new PlatoNoEncontradoException();
            }

            DishModel platoEncontrado = dishPersistencePort.obtenerPlatoPorId(pedidoPlato.getPlato().getId());
            if (platoEncontrado == null) {
                throw new PlatoNoEncontradoException();
            }

            if (platoEncontrado.getRestaurante() == null || !idRestaurante.equals(platoEncontrado.getRestaurante().getId())) {
                throw new PlatosDeDiferenteRestauranteException();
            }

            if (!Boolean.TRUE.equals(platoEncontrado.getActivo())) {
                throw new PlatoInactivoException();
            }

            pedidoPlato.setPlato(platoEncontrado);
            pedidoPlato.setPedido(orderModel);
        }

        orderModel.setIdCliente(idCliente);
        orderModel.setFecha(LocalDate.now());
        orderModel.setEstado(OrderStatus.PENDIENTE.name());
        orderModel.setIdChef(null);

        orderPersistencePort.guardarPedido(orderModel);
        registrarTrazabilidadSegura(orderModel.getId(), idCliente, null, null, OrderStatus.PENDIENTE.name(), null, null);
    }

    @Override
    public List<OrderModel> listarPedidosPorEstado(String estado, Long idRestaurante, int page, int size) {
        if (page < 0 || size <= 0) {
            throw new ParametrosPaginacionInvalidosException();
        }

        if (idRestaurante == null) {
            Long idEmpleado = authenticatedUserPort.obtenerIdUsuarioAutenticado();
            if (idEmpleado != null) {
                idRestaurante = orderPersistencePort.obtenerIdRestaurantePorEmpleado(idEmpleado);
            }
        }

        if (idRestaurante != null && !restaurantPersistencePort.existePorId(idRestaurante)) {
            throw new RestauranteNoEncontradoException();
        }

        String estadoFiltrado = (estado != null && !estado.trim().isEmpty())
                ? estado.trim().toUpperCase()
                : OrderStatus.PENDIENTE.name();

        return orderPersistencePort.listarPedidosPorEstado(estadoFiltrado, idRestaurante, page, size);
    }

    @Override
    public OrderModel asignarPedido(Long idPedido) {
        if (idPedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idEmpleado = authenticatedUserPort.obtenerIdUsuarioAutenticado();
        if (idEmpleado == null) {
            throw new UsuarioNoAutorizadoException();
        }

        OrderModel pedido = orderPersistencePort.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idRestauranteEmpleado = orderPersistencePort.obtenerIdRestaurantePorEmpleado(idEmpleado);
        if (idRestauranteEmpleado != null && pedido.getRestaurante() != null
                && !idRestauranteEmpleado.equals(pedido.getRestaurante().getId())) {
            throw new UsuarioNoAutorizadoException();
        }

        if (!OrderStatus.PENDIENTE.name().equalsIgnoreCase(pedido.getEstado())) {
            throw new PedidoNoEstaEnEstadoPendienteException();
        }

        pedido.setEstado(OrderStatus.EN_PREPARACION.name());
        pedido.setIdChef(idEmpleado);

        OrderModel pedidoActualizado = orderPersistencePort.actualizarPedido(pedido);
        registrarTrazabilidadSegura(pedido.getId(), pedido.getIdCliente(), null,
                OrderStatus.PENDIENTE.name(), OrderStatus.EN_PREPARACION.name(), idEmpleado, null);

        return pedidoActualizado;
    }

    @Override
    public OrderModel notificarPedidoListo(Long idPedido) {
        if (idPedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idEmpleado = authenticatedUserPort.obtenerIdUsuarioAutenticado();
        if (idEmpleado == null) {
            throw new UsuarioNoAutorizadoException();
        }

        OrderModel pedido = orderPersistencePort.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idRestauranteEmpleado = orderPersistencePort.obtenerIdRestaurantePorEmpleado(idEmpleado);
        if (idRestauranteEmpleado != null && pedido.getRestaurante() != null
                && !idRestauranteEmpleado.equals(pedido.getRestaurante().getId())) {
            throw new UsuarioNoAutorizadoException();
        }

        if (!OrderStatus.EN_PREPARACION.name().equalsIgnoreCase(pedido.getEstado())) {
            throw new PedidoNoEstaEnPreparacionException();
        }

        String pin = String.format("%06d", secureRandom.nextInt(1_000_000));
        pedido.setPin(pin);
        pedido.setEstado(OrderStatus.LISTO.name());

        OrderModel pedidoActualizado = orderPersistencePort.actualizarPedido(pedido);

        String celular = userValidationServicePort.obtenerCelularUsuario(pedido.getIdCliente());
        mensajeriaServicePort.notificarPedidoListo(celular, pin, pedido.getId());
        registrarTrazabilidadSegura(pedido.getId(), pedido.getIdCliente(), null,
                OrderStatus.EN_PREPARACION.name(), OrderStatus.LISTO.name(), idEmpleado, null);

        return pedidoActualizado;
    }

    @Override
    public OrderModel entregarPedido(Long idPedido, String pin) {
        if (idPedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idEmpleado = authenticatedUserPort.obtenerIdUsuarioAutenticado();
        if (idEmpleado == null) {
            throw new UsuarioNoAutorizadoException();
        }

        OrderModel pedido = orderPersistencePort.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idRestauranteEmpleado = orderPersistencePort.obtenerIdRestaurantePorEmpleado(idEmpleado);
        if (idRestauranteEmpleado != null && pedido.getRestaurante() != null
                && !idRestauranteEmpleado.equals(pedido.getRestaurante().getId())) {
            throw new UsuarioNoAutorizadoException();
        }

        if (!OrderStatus.LISTO.name().equalsIgnoreCase(pedido.getEstado())) {
            throw new PedidoNoEstaListoException();
        }

        if (pin == null || pin.trim().isEmpty() || !pin.trim().equals(pedido.getPin())) {
            throw new PinInvalidoException();
        }

        pedido.setEstado(OrderStatus.ENTREGADO.name());

        OrderModel pedidoActualizado = orderPersistencePort.actualizarPedido(pedido);
        registrarTrazabilidadSegura(pedido.getId(), pedido.getIdCliente(), null,
                OrderStatus.LISTO.name(), OrderStatus.ENTREGADO.name(), idEmpleado, null);

        return pedidoActualizado;
    }

    @Override
    public OrderModel cancelarPedido(Long idPedido) {
        if (idPedido == null) {
            throw new PedidoNoEncontradoException();
        }

        Long idCliente = authenticatedUserPort.obtenerIdUsuarioAutenticado();
        if (idCliente == null) {
            throw new UsuarioNoAutorizadoException();
        }

        OrderModel pedido = orderPersistencePort.obtenerPedidoPorId(idPedido);
        if (pedido == null) {
            throw new PedidoNoEncontradoException();
        }

        if (!idCliente.equals(pedido.getIdCliente())) {
            throw new UsuarioNoAutorizadoException();
        }

        if (!OrderStatus.PENDIENTE.name().equalsIgnoreCase(pedido.getEstado())) {
            throw new PedidoNoSePuedeCancelarException();
        }

        pedido.setEstado(OrderStatus.CANCELADO.name());

        OrderModel pedidoActualizado = orderPersistencePort.actualizarPedido(pedido);
        registrarTrazabilidadSegura(pedido.getId(), idCliente, null,
                OrderStatus.PENDIENTE.name(), OrderStatus.CANCELADO.name(), null, null);

        return pedidoActualizado;
    }

    private void registrarTrazabilidadSegura(Long idPedido, Long idCliente, String correoCliente,
                                            String estadoAnterior, String nuevoEstado,
                                            Long idEmpleado, String correoEmpleado) {
        if (trazabilidadServicePort != null && idPedido != null) {
            try {
                trazabilidadServicePort.registrarCambioEstado(idPedido, idCliente, correoCliente,
                        estadoAnterior, nuevoEstado, idEmpleado, correoEmpleado);
            } catch (Exception ignored) {
                // Trazabilidad no debe bloquear el flujo principal de pedidos
            }
        }
    }
}
