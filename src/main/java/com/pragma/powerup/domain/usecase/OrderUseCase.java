package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.CantidadPlatoInvalidaException;
import com.pragma.powerup.domain.exception.ClienteTienePedidoActivoException;
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
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

import java.time.LocalDate;
import java.util.List;

public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IAuthenticatedUserPort authenticatedUserPort;

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IAuthenticatedUserPort authenticatedUserPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.authenticatedUserPort = authenticatedUserPort;
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
    }
}
