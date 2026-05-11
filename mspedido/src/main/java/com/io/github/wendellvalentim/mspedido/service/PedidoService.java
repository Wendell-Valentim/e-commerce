package com.io.github.wendellvalentim.mspedido.service;

import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;
import com.io.github.wendellvalentim.mspedido.event.PedidoCriadoEvent;
import com.io.github.wendellvalentim.mspedido.exception.PedidoNaoEncontradoException;
import com.io.github.wendellvalentim.mspedido.infra.ProdutoResourceClient;
import com.io.github.wendellvalentim.mspedido.infra.mqueue.ProdutoPublisher;
import com.io.github.wendellvalentim.mspedido.mapper.ItemPedidoMapper;
import com.io.github.wendellvalentim.mspedido.model.ItemPedido;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import com.io.github.wendellvalentim.mspedido.model.produto.ProdutoResponseDTO;
import com.io.github.wendellvalentim.mspedido.repository.PedidoRepository;
import com.io.github.wendellvalentim.mspedido.validators.PedidoValidator;
import com.io.github.wendellvalentim.mspedido.validators.ProdutoValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.io.github.wendellvalentim.mspedido.repository.PedidoSpecs.*;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoResourceClient produtoResourceClient;
    private final ItemPedidoMapper mapper;
    private final ProdutoPublisher produtoPublisher;
    private final ProdutoValidator produtoValidator;
    private final PedidoValidator pedidoValidator;

    @Transactional
    public Pedido salvar (PedidoRequestDTO request) {
        Pedido pedido = new Pedido();
        pedido.setCodigoPedido(UUID.randomUUID().toString());
        pedido.setStatus(StatusPedido.RECEBIDO);
        List<ItemPedido> listaDeItens = request.itens().stream().map(itemDTO -> {

            ResponseEntity<ProdutoResponseDTO> response = produtoResourceClient.getProdutosById(itemDTO.produtoId());
            ProdutoResponseDTO produtoData = response.getBody();

            produtoValidator.validar(itemDTO,produtoData);

            ItemPedido itemEntity = mapper.toEntity(produtoData);

            itemEntity.setQuantidade(itemDTO.quantidade());

            itemEntity.setPedido(pedido);

            itemEntity.setSubTotal(itemEntity.getPrecoUnitario().multiply(BigDecimal.valueOf(itemEntity.getQuantidade())));

            return itemEntity;
        }).toList();

        BigDecimal totalPedido = listaDeItens.stream()
                .map(ItemPedido::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedidoValidator.validarNovoPedido(request,totalPedido);

        pedido.setItems(listaDeItens);
        pedido.setTotal(totalPedido);
        pedido.setStatus(StatusPedido.APROVADO);


        Pedido pedidoSalvo =  pedidoRepository.save(pedido);

        pedidoSalvo.getItems().forEach(item -> {
            PedidoCriadoEvent event = new PedidoCriadoEvent(item.getProdutoId(), item.getQuantidade());
            this.solicitarBaixaDoEstoque(event);
        });
        return pedidoSalvo;
    }

    public Pedido buscarPorId(UUID id) {
        return pedidoRepository.findById(id).orElseThrow(() -> new PedidoNaoEncontradoException("Pedido não encontrado!"));
    }

    public void solicitarBaixaDoEstoque(PedidoCriadoEvent event) {
        produtoPublisher.abaixarEstoqueProduto(event);
        System.out.println("Efetuado!");
    }

    public void aumentarEstoque(PedidoCriadoEvent event){
        produtoPublisher.aumentarEstoqueProdut(event);
        System.out.println("Efetuado!");
    }


    @Transactional
    public Pedido solicitarCancelamentoPedido(UUID id) {
       Pedido pedidoEncontrado = buscarPorId(id);

        pedidoValidator.validarCancelamento(pedidoEncontrado);

        pedidoEncontrado.setStatus(StatusPedido.CANCELADO);
        Pedido pedidoCancelado = pedidoRepository.save(pedidoEncontrado);

        pedidoEncontrado.getItems().forEach(item ->{
            PedidoCriadoEvent event = new PedidoCriadoEvent(item.getProdutoId(), item.getQuantidade());
            this.aumentarEstoque(event);
        });

        System.out.println("Pedido cancelado com sucesso!");
        return pedidoCancelado;

    }

    public Page<Pedido> pesquisa(String codPedido,
                                 String nomeProduto,
                                 LocalDate dataBusca,
                                 Integer pagina,
                                 Integer tamanhoPagina) {

        Specification<Pedido> specs = Specification.where((root, query, cb) ->
                cb.conjunction());
        if (codPedido != null) {
            specs = specs.and(codPedidoLike(codPedido));
        }

        if(nomeProduto != null) {
            specs = specs.and(nomeProdutoLike(nomeProduto));
        }

        if(dataBusca != null) {
            specs = specs.and(dataPedidoEqual(dataBusca));
        }

        Pageable pageRequest = PageRequest.of(pagina, tamanhoPagina);

        return pedidoRepository.findAll(specs, pageRequest);
    }
}
