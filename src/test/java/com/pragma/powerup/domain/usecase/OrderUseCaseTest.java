package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.CantidadPlatoInvalidaException;
import com.pragma.powerup.domain.exception.ClienteTienePedidoActivoException;
import com.pragma.powerup.domain.exception.ParametrosPaginacionInvalidosException;
import com.pragma.powerup.domain.exception.PedidoNoEncontradoException;
import com.pragma.powerup.domain.exception.PedidoNoEstaEnEstadoPendienteException;
import com.pragma.powerup.domain.exception.PedidoNoEstaEnPreparacionException;
import com.pragma.powerup.domain.exception.PedidoSinPlatosException;
import com.pragma.powerup.domain.exception.PlatoInactivoException;
import com.pragma.powerup.domain.exception.PlatoNoEncontradoException;
import com.pragma.powerup.domain.exception.PlatosDeDiferenteRestauranteException;
import com.pragma.powerup.domain.exception.RestauranteNoEncontradoException;
import com.pragma.powerup.domain.exception.UsuarioNoAutorizadoException;
import com.pragma.powerup.domain.model.CategoryModel;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IAuthenticatedUserPort;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IMensajeriaServicePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserValidationServicePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private IOrderPersistencePort orderPersistencePort;

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IDishPersistencePort dishPersistencePort;

    @Mock
    private IAuthenticatedUserPort authenticatedUserPort;

    @Mock
    private IUserValidationServicePort userValidationServicePort;

    @Mock
    private IMensajeriaServicePort mensajeriaServicePort;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private RestaurantModel restauranteValido;
    private DishModel platoValido;
    private OrderModel ordenValida;

    @BeforeEach
    void setUp() {
        restauranteValido = new RestaurantModel(1L, "Restaurante Central", "123456789", "Calle 10 # 20-30", "+573001234567", "https://img.com/logo.png", 2L);
        CategoryModel categoriaValida = new CategoryModel(1L, "PLATO FUERTE", "Plato fuerte");
        platoValido = new DishModel(10L, "Bandeja Paisa", categoriaValida, "Deliciosa bandeja", 35000, restauranteValido, "https://img.com/plato.png", true);

        OrderDishModel item = new OrderDishModel(null, null, new DishModel(10L, null, null, null, null, null, null, null), 2);
        List<OrderDishModel> items = new ArrayList<>();
        items.add(item);

        ordenValida = new OrderModel(null, null, null, null, null, null, restauranteValido, items);
    }

    @Test
    void realizarPedido_exitoso() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        when(dishPersistencePort.obtenerPlatoPorId(10L)).thenReturn(platoValido);

        // Act
        orderUseCase.realizarPedido(ordenValida);

        // Assert
        assertEquals(5L, ordenValida.getIdCliente());
        assertEquals(OrderStatus.PENDIENTE.name(), ordenValida.getEstado());
        assertEquals(LocalDate.now(), ordenValida.getFecha());
        assertNull(ordenValida.getIdChef());
        assertEquals(1, ordenValida.getPedidosPlatos().size());
        assertEquals(platoValido, ordenValida.getPedidosPlatos().get(0).getPlato());
        verify(orderPersistencePort, times(1)).guardarPedido(ordenValida);
    }

    @Test
    void realizarPedido_clienteNoAutenticado_lanzaUsuarioNoAutorizadoException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(null);

        // Act & Assert
        assertThrows(UsuarioNoAutorizadoException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_clienteTienePedidoActivo_lanzaClienteTienePedidoActivoException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(true);

        // Act & Assert
        assertThrows(ClienteTienePedidoActivoException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_restauranteNoExiste_lanzaRestauranteNoEncontradoException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RestauranteNoEncontradoException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_listaPlatosVacia_lanzaPedidoSinPlatosException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        ordenValida.setPedidosPlatos(Collections.emptyList());

        // Act & Assert
        assertThrows(PedidoSinPlatosException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_cantidadInvalida_lanzaCantidadPlatoInvalidaException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        ordenValida.getPedidosPlatos().get(0).setCantidad(0);

        // Act & Assert
        assertThrows(CantidadPlatoInvalidaException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_platoNoExiste_lanzaPlatoNoEncontradoException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        when(dishPersistencePort.obtenerPlatoPorId(10L)).thenReturn(null);

        // Act & Assert
        assertThrows(PlatoNoEncontradoException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_platoDeDiferenteRestaurante_lanzaPlatosDeDiferenteRestauranteException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        RestaurantModel otroRestaurante = new RestaurantModel(99L, "Otro", "999", "Dir", "+57300", "url", 3L);
        DishModel platoDeOtroRestaurante = new DishModel(10L, "Plato", null, "Desc", 20000, otroRestaurante, "url", true);
        when(dishPersistencePort.obtenerPlatoPorId(10L)).thenReturn(platoDeOtroRestaurante);

        // Act & Assert
        assertThrows(PlatosDeDiferenteRestauranteException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void realizarPedido_platoInactivo_lanzaPlatoInactivoException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(5L);
        when(orderPersistencePort.existePedidoActivoPorCliente(5L)).thenReturn(false);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        DishModel platoInactivo = new DishModel(10L, "Plato Inactivo", null, "Desc", 20000, restauranteValido, "url", false);
        when(dishPersistencePort.obtenerPlatoPorId(10L)).thenReturn(platoInactivo);

        // Act & Assert
        assertThrows(PlatoInactivoException.class, () -> orderUseCase.realizarPedido(ordenValida));
        verify(orderPersistencePort, never()).guardarPedido(any());
    }

    @Test
    void listarPedidosPorEstado_conRestauranteYEstado_exitoso() {
        // Arrange
        Long idRestaurante = 1L;
        String estado = "PENDIENTE";
        when(restaurantPersistencePort.existePorId(idRestaurante)).thenReturn(true);
        when(orderPersistencePort.listarPedidosPorEstado("PENDIENTE", idRestaurante, 0, 10))
                .thenReturn(Collections.singletonList(ordenValida));

        // Act
        List<OrderModel> resultado = orderUseCase.listarPedidosPorEstado(estado, idRestaurante, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderPersistencePort, times(1)).listarPedidosPorEstado("PENDIENTE", idRestaurante, 0, 10);
    }

    @Test
    void listarPedidosPorEstado_sinRestaurante_resuelvePorEmpleado_exitoso() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(8L);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(8L)).thenReturn(1L);
        when(restaurantPersistencePort.existePorId(1L)).thenReturn(true);
        when(orderPersistencePort.listarPedidosPorEstado("PENDIENTE", 1L, 0, 10))
                .thenReturn(Collections.singletonList(ordenValida));

        // Act
        List<OrderModel> resultado = orderUseCase.listarPedidosPorEstado("PENDIENTE", null, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderPersistencePort, times(1)).listarPedidosPorEstado("PENDIENTE", 1L, 0, 10);
    }

    @Test
    void listarPedidosPorEstado_estadoNull_usaDefaultPendiente() {
        // Arrange
        Long idRestaurante = 1L;
        when(restaurantPersistencePort.existePorId(idRestaurante)).thenReturn(true);
        when(orderPersistencePort.listarPedidosPorEstado("PENDIENTE", idRestaurante, 0, 10))
                .thenReturn(Collections.singletonList(ordenValida));

        // Act
        List<OrderModel> resultado = orderUseCase.listarPedidosPorEstado(null, idRestaurante, 0, 10);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(orderPersistencePort, times(1)).listarPedidosPorEstado("PENDIENTE", idRestaurante, 0, 10);
    }

    @Test
    void listarPedidosPorEstado_paginaNegativa_lanzaParametrosPaginacionInvalidosException() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class, () ->
                orderUseCase.listarPedidosPorEstado("PENDIENTE", 1L, -1, 10)
        );
        verify(orderPersistencePort, never()).listarPedidosPorEstado(anyString(), any(), anyInt(), anyInt());
    }

    @Test
    void listarPedidosPorEstado_tamanioInvalido_lanzaParametrosPaginacionInvalidosException() {
        // Act & Assert
        assertThrows(ParametrosPaginacionInvalidosException.class, () ->
                orderUseCase.listarPedidosPorEstado("PENDIENTE", 1L, 0, 0)
        );
        verify(orderPersistencePort, never()).listarPedidosPorEstado(anyString(), any(), anyInt(), anyInt());
    }

    @Test
    void listarPedidosPorEstado_restauranteNoExiste_lanzaRestauranteNoEncontradoException() {
        // Arrange
        when(restaurantPersistencePort.existePorId(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(RestauranteNoEncontradoException.class, () ->
                orderUseCase.listarPedidosPorEstado("PENDIENTE", 999L, 0, 10)
        );
        verify(orderPersistencePort, never()).listarPedidosPorEstado(anyString(), any(), anyInt(), anyInt());
    }

    @Test
    void asignarPedido_exitoso_cambiaEstadoAEnPreparacionYAsignaChef() {
        // Arrange
        Long idPedido = 1L;
        Long idChef = 50L;
        Long idRestaurante = 10L;

        RestaurantModel restaurante = new RestaurantModel(idRestaurante, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, 100L, LocalDate.now(), OrderStatus.PENDIENTE.name(), null, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(idRestaurante);
        when(orderPersistencePort.actualizarPedido(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderModel resultado = orderUseCase.asignarPedido(idPedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(OrderStatus.EN_PREPARACION.name(), resultado.getEstado());
        assertEquals(idChef, resultado.getIdChef());
        verify(orderPersistencePort, times(1)).actualizarPedido(pedido);
    }

    @Test
    void asignarPedido_idPedidoNulo_lanzaPedidoNoEncontradoException() {
        // Act & Assert
        assertThrows(PedidoNoEncontradoException.class, () ->
                orderUseCase.asignarPedido(null)
        );
        verify(orderPersistencePort, never()).obtenerPedidoPorId(any());
    }

    @Test
    void asignarPedido_usuarioNoAutenticado_lanzaUsuarioNoAutorizadoException() {
        // Arrange
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(null);

        // Act & Assert
        assertThrows(UsuarioNoAutorizadoException.class, () ->
                orderUseCase.asignarPedido(1L)
        );
        verify(orderPersistencePort, never()).obtenerPedidoPorId(any());
    }

    @Test
    void asignarPedido_pedidoNoExiste_lanzaPedidoNoEncontradoException() {
        // Arrange
        Long idPedido = 999L;
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(50L);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(null);

        // Act & Assert
        assertThrows(PedidoNoEncontradoException.class, () ->
                orderUseCase.asignarPedido(idPedido)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void asignarPedido_empleadoDeOtroRestaurante_lanzaUsuarioNoAutorizadoException() {
        // Arrange
        Long idPedido = 1L;
        Long idChef = 50L;
        Long idRestaurantePedido = 10L;
        Long idRestauranteEmpleado = 20L;

        RestaurantModel restaurante = new RestaurantModel(idRestaurantePedido, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, 100L, LocalDate.now(), OrderStatus.PENDIENTE.name(), null, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(idRestauranteEmpleado);

        // Act & Assert
        assertThrows(UsuarioNoAutorizadoException.class, () ->
                orderUseCase.asignarPedido(idPedido)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void asignarPedido_pedidoNoEstaEnPendiente_lanzaPedidoNoEstaEnEstadoPendienteException() {
        // Arrange
        Long idPedido = 1L;
        Long idChef = 50L;
        Long idRestaurante = 10L;

        RestaurantModel restaurante = new RestaurantModel(idRestaurante, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, 100L, LocalDate.now(), OrderStatus.EN_PREPARACION.name(), 40L, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(idRestaurante);

        // Act & Assert
        assertThrows(PedidoNoEstaEnEstadoPendienteException.class, () ->
                orderUseCase.asignarPedido(idPedido)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void asignarPedido_empleadoSinRestauranteAsignado_asignaExitosamente() {
        // Arrange
        Long idPedido = 1L;
        Long idChef = 50L;
        Long idRestaurante = 10L;

        RestaurantModel restaurante = new RestaurantModel(idRestaurante, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, 100L, LocalDate.now(), OrderStatus.PENDIENTE.name(), null, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(null);
        when(orderPersistencePort.actualizarPedido(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        OrderModel resultado = orderUseCase.asignarPedido(idPedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(OrderStatus.EN_PREPARACION.name(), resultado.getEstado());
        assertEquals(idChef, resultado.getIdChef());
        verify(orderPersistencePort, times(1)).actualizarPedido(pedido);
    }

    @Test
    void notificarPedidoListo_exitoso_generaPinYEnviaSms() {
        // Arrange
        Long idPedido = 1L;
        Long idChef = 50L;
        Long idCliente = 100L;
        Long idRestaurante = 10L;

        RestaurantModel restaurante = new RestaurantModel(idRestaurante, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, idCliente, LocalDate.now(), OrderStatus.EN_PREPARACION.name(), idChef, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(idRestaurante);
        when(orderPersistencePort.actualizarPedido(any(OrderModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userValidationServicePort.obtenerCelularUsuario(idCliente)).thenReturn("+573001234567");
        when(mensajeriaServicePort.notificarPedidoListo(anyString(), anyString(), any())).thenReturn(true);

        // Act
        OrderModel resultado = orderUseCase.notificarPedidoListo(idPedido);

        // Assert
        assertNotNull(resultado);
        assertEquals(OrderStatus.LISTO.name(), resultado.getEstado());
        assertNotNull(resultado.getPin());
        assertEquals(6, resultado.getPin().length());
        verify(orderPersistencePort, times(1)).actualizarPedido(pedido);
        verify(userValidationServicePort, times(1)).obtenerCelularUsuario(idCliente);
        verify(mensajeriaServicePort, times(1)).notificarPedidoListo(eq("+573001234567"), eq(resultado.getPin()), eq(idPedido));
    }

    @Test
    void notificarPedidoListo_idPedidoNulo_lanzaPedidoNoEncontradoException() {
        assertThrows(PedidoNoEncontradoException.class, () ->
                orderUseCase.notificarPedidoListo(null)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void notificarPedidoListo_usuarioNoAutenticado_lanzaUsuarioNoAutorizadoException() {
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(null);

        assertThrows(UsuarioNoAutorizadoException.class, () ->
                orderUseCase.notificarPedidoListo(1L)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void notificarPedidoListo_pedidoNoExiste_lanzaPedidoNoEncontradoException() {
        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(50L);
        when(orderPersistencePort.obtenerPedidoPorId(1L)).thenReturn(null);

        assertThrows(PedidoNoEncontradoException.class, () ->
                orderUseCase.notificarPedidoListo(1L)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void notificarPedidoListo_empleadoDeOtroRestaurante_lanzaUsuarioNoAutorizadoException() {
        Long idPedido = 1L;
        Long idChef = 50L;
        RestaurantModel restaurante = new RestaurantModel(10L, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, 100L, LocalDate.now(), OrderStatus.EN_PREPARACION.name(), idChef, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(99L);

        assertThrows(UsuarioNoAutorizadoException.class, () ->
                orderUseCase.notificarPedidoListo(idPedido)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }

    @Test
    void notificarPedidoListo_pedidoNoEstaEnPreparacion_lanzaPedidoNoEstaEnPreparacionException() {
        Long idPedido = 1L;
        Long idChef = 50L;
        RestaurantModel restaurante = new RestaurantModel(10L, "Restaurante", "123456", "Direccion", "+573001234567", "url", 100L);
        OrderModel pedido = new OrderModel(idPedido, 100L, LocalDate.now(), OrderStatus.PENDIENTE.name(), null, null, restaurante, new ArrayList<>());

        when(authenticatedUserPort.obtenerIdUsuarioAutenticado()).thenReturn(idChef);
        when(orderPersistencePort.obtenerPedidoPorId(idPedido)).thenReturn(pedido);
        when(orderPersistencePort.obtenerIdRestaurantePorEmpleado(idChef)).thenReturn(10L);

        assertThrows(PedidoNoEstaEnPreparacionException.class, () ->
                orderUseCase.notificarPedidoListo(idPedido)
        );
        verify(orderPersistencePort, never()).actualizarPedido(any());
    }
}
