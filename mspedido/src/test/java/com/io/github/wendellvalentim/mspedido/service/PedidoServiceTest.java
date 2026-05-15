package com.io.github.wendellvalentim.mspedido.service;


import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;
import com.io.github.wendellvalentim.mspedido.event.PedidoCriadoEvent;
import com.io.github.wendellvalentim.mspedido.exception.*;
import com.io.github.wendellvalentim.mspedido.infra.ProdutoResourceClient;
import com.io.github.wendellvalentim.mspedido.infra.mqueue.ProdutoPublisher;
import com.io.github.wendellvalentim.mspedido.mapper.ItemPedidoMapper;
import com.io.github.wendellvalentim.mspedido.mapper.PedidoMapper;
import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import com.io.github.wendellvalentim.mspedido.model.produto.ProdutoResponseDTO;
import com.io.github.wendellvalentim.mspedido.repository.PedidoRepository;
import com.io.github.wendellvalentim.mspedido.validators.PedidoValidator;
import com.io.github.wendellvalentim.mspedido.validators.ProdutoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    PedidoService pedidoService;

    @Mock
    PedidoValidator pedidoValidator;

    @Mock
    PedidoMapper pedidoMapper;

    @Mock
    ItemPedidoMapper itemPedidoMapper;

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    ProdutoResourceClient produtoResourceClient;

    @Mock
    ProdutoValidator produtoValidator;

    @Mock
    ProdutoPublisher produtoPublisher;

    Pedido pedido;
    ItemPedido itemPedido;
    ProdutoResponseDTO produtoResponseDTO;
    ItemPedidoRequestDTO itemPedidoRequestDTO;
    PedidoRequestDTO pedidoRequestDTO;


    @BeforeEach
    void setUp() {
        itemPedidoRequestDTO = new ItemPedidoRequestDTO(
                UUID.fromString("e092e7ea-6223-4f93-8a17-942df711774c"),
                2
        );
        pedidoRequestDTO = new PedidoRequestDTO(List.of(itemPedidoRequestDTO));

        produtoResponseDTO = new ProdutoResponseDTO(
                itemPedidoRequestDTO.produtoId(),
                "Fone Bluetooth",
                "PROD-001",
                new BigDecimal("100.00"),
                10
        );

        itemPedido = new ItemPedido();
        itemPedido.setProdutoId(produtoResponseDTO.produtoId());
        itemPedido.setNomeProduto("Fone Bluetooth");
        itemPedido.setPrecoUnitario(new BigDecimal("100.00"));
        itemPedido.setCodProduto("PROD-001");
        itemPedido.setQuantidade(2);
        itemPedido.setSubTotal(new BigDecimal("200.00"));
        itemPedido.setPedido(pedido);

        pedido = new Pedido();
        pedido.setCodigoPedido(UUID.randomUUID().toString());
        pedido.setStatus(StatusPedido.APROVADO);
        pedido.setItems(List.of(itemPedido));
        pedido.setTotal(new BigDecimal("200.00"));

    }

    @Test
    @DisplayName("Deve salvar um pedido com sucesso")
    void deveSalvarPedidoComSucesso() {

        when(produtoResourceClient.getProdutosById(any()))
                .thenReturn(ResponseEntity.ok(produtoResponseDTO));

        when(itemPedidoMapper.toEntity(any()))
                .thenReturn(itemPedido);

        when(pedidoRepository.save(any()))
                .thenReturn(pedido);


        Pedido pedidoSalvo = pedidoService.salvar(pedidoRequestDTO);


        assertNotNull(pedidoSalvo);
        assertEquals(StatusPedido.APROVADO, pedidoSalvo.getStatus());
        assertEquals(new BigDecimal("200.00"), pedidoSalvo.getTotal());


        verify(produtoPublisher, times(1)).abaixarEstoqueProduto(any());
    }


    @Test
    @DisplayName("Deve dar erro quando o ProdutoId for nulo")
    void deveLancarErroProdutoIdNulo() {

        when(produtoResourceClient.getProdutosById(any())).thenReturn(ResponseEntity.ok(produtoResponseDTO));

        doThrow(new IllegalArgumentException("produtoId não pode ser nulo"))
                .when(produtoValidator).validar(itemPedidoRequestDTO, produtoResponseDTO);

        assertThrows(IllegalArgumentException.class, () -> pedidoService.salvar(pedidoRequestDTO));

        verify(pedidoRepository, never()).save(any());
    }


    @Test
    @DisplayName("Deve dar erro de Estoque insuficiente")
    void deveDarErroDeEstoqueInsuficiente() {
        ProdutoResponseDTO semEstoque = new ProdutoResponseDTO(
                UUID.fromString("e092e7ea-6223-4f93-8a17-942df711774c"), "Fone", "PROD", new BigDecimal("100"), 0);

        when(produtoResourceClient.getProdutosById(any())).thenReturn(ResponseEntity.ok(semEstoque));

        doThrow(new EstoqueInsuficienteException("Estoque insuficiente"))
                .when(produtoValidator).validar(itemPedidoRequestDTO, semEstoque);

        assertThrows(EstoqueInsuficienteException.class, () -> pedidoService.salvar(pedidoRequestDTO));

        verify(pedidoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve dar erro com um valor abaixo do minimo")
    void deveDarErroComValorAbaixoDoMinimo() {

        when(produtoResourceClient.getProdutosById(any())).thenReturn(ResponseEntity.ok(produtoResponseDTO));
        when(itemPedidoMapper.toEntity(any())).thenReturn(itemPedido);

        doThrow(new ValorMinimoException("Valor minimo insuficiente")).when(pedidoValidator).validarNovoPedido(
                pedidoRequestDTO, new BigDecimal("200.00")
        );

        assertThrows(ValorMinimoException.class, () -> pedidoService.salvar(pedidoRequestDTO));
    }


    @Test
    @DisplayName("Deve lançar erro quando a lista de itens estiver vazia")
    void deveLancarErroAoReceberListaVazia() {
        PedidoRequestDTO requestSemItens = new PedidoRequestDTO(Collections.emptyList());

        doThrow(new CampoInvalidoException("O pedido deve ter pelo menos um item."))
                .when(pedidoValidator).validarNovoPedido(eq(requestSemItens), eq(BigDecimal.ZERO));

        assertThrows(CampoInvalidoException.class, () -> pedidoService.salvar(requestSemItens));

        verifyNoInteractions(produtoResourceClient);
    }

    @Test
    @DisplayName("Deve lançar erro Quando o serviço de produtos falhar")
    void deveLancarErroQuandoServicoProdutoFalhar() {

        when(produtoResourceClient.getProdutosById(any())).thenThrow(new RuntimeException("Servico Indisponivel!"));

        assertThrows(RuntimeException.class, () -> pedidoService.salvar(pedidoRequestDTO));
    }

    @Test
    @DisplayName("Deve retornar um Pedido")
    void DeveRetornarUmPedidoPorId() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));

        Pedido pedidoEncontrado = pedidoService.buscarPorId(pedido.getId());

        assertNotNull(pedidoEncontrado);
        assertEquals(pedido.getId(), pedidoEncontrado.getId());

        verify(pedidoRepository, times(1)).findById(pedido.getId());
    }

    @Test
    @DisplayName("Deve dar erro ao buscar um pedido")
    void deveDarErroAoBuscarUmPedidoPorId() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class, () -> pedidoService.buscarPorId(pedido.getId()));

        verify(pedidoRepository, times(1)).findById(pedido.getId());

    }


    @Test
    @DisplayName("Deve abaixar o estoque")
    void deveAbaixarOEstoque() {
        PedidoCriadoEvent eventoEsperado = new PedidoCriadoEvent(
                itemPedido.getProdutoId(),
                itemPedido.getQuantidade()
        );

        pedidoService.solicitarBaixaDoEstoque(eventoEsperado);

        verify(produtoPublisher, times(1)).abaixarEstoqueProduto(refEq(eventoEsperado));
    }

    @Test
    @DisplayName("Deve repor o estoque")
    void deveReporOEstoque() {
        PedidoCriadoEvent eventoEsperado = new PedidoCriadoEvent(
                itemPedido.getProdutoId(),
                itemPedido.getQuantidade()
        );

        pedidoService.aumentarEstoque(eventoEsperado);

        verify(produtoPublisher, times(1)).aumentarEstoqueProduto(refEq(eventoEsperado));

    }

    @Test
    @DisplayName("Efetuar cancelamento do pedido")
    void deveEfetuarOCancelamentoDoPedido() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedido);


        Pedido pedidoSalvo = pedidoService.solicitarCancelamentoPedido(pedido.getId());

        assertNotNull(pedidoSalvo);

        assertEquals(StatusPedido.CANCELADO, pedidoSalvo.getStatus());

        verify(pedidoRepository, times(1)).save(pedidoSalvo);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar alterar o Status do pedido")
    void deveLancarErroAoAlteraroStatusDoPedido() {
        when(pedidoRepository.findById(any())).thenReturn(Optional.of(pedido));

        Pedido pedidoEncontrado = pedidoService.buscarPorId(pedido.getId());

        doThrow(new NaoEPossivelCancelarException(
                "Não é possível cancelar o pedido! Status atual:" + pedidoEncontrado.getStatus()))
                .when(pedidoValidator).validarCancelamento(pedidoEncontrado);

        assertThrows(NaoEPossivelCancelarException.class,
                () -> pedidoService.solicitarCancelamentoPedido(pedidoEncontrado.getId()));

    }


    }



