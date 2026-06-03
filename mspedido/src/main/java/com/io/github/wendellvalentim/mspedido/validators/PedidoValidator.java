package com.io.github.wendellvalentim.mspedido.validators;

import com.io.github.wendellvalentim.mspedido.controller.dto.ItemPedido.ItemPedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.controller.dto.Pedido.PedidoRequestDTO;
import com.io.github.wendellvalentim.mspedido.enums.StatusPedido;
import com.io.github.wendellvalentim.mspedido.exception.CampoInvalidoException;
import com.io.github.wendellvalentim.mspedido.exception.NaoEPossivelCancelarException;
import com.io.github.wendellvalentim.mspedido.exception.ValorMinimoException;
import com.io.github.wendellvalentim.mspedido.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class  PedidoValidator {
    private BigDecimal VALOR_MINIMO = BigDecimal.valueOf(0);

    private Jwt getJwtUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt;
        }
        throw new AccessDeniedException("Usuário não autenticado.");
    }

    public void validarPropriedadePedido(Pedido pedido)  {
        Jwt jwt = getJwtUsuarioLogado();

        String userSub = jwt.getSubject();
        List<String> roles = jwt.getClaimAsStringList("roles");

        boolean isAdmin = roles != null && roles.contains("ROLE_ADMIN");

        if (!isAdmin && !pedido.getUserSub().equals(userSub)) {
            throw new AccessDeniedException("Você não tem permissão para acessar este pedido.");
        }

    }

    public void validarCancelamento(Pedido pedido) {

        validarPropriedadePedido(pedido);

        if(validarStatus(pedido.getStatus())) {
            throw new NaoEPossivelCancelarException(
                    "Não é possível cancelar o pedido! Status atual: " + pedido.getStatus());
        }
    }

    public void validarNovoPedido(PedidoRequestDTO request, BigDecimal totalCalculado) {
        if(isListaDeItensVazia(request.itens())) {
            throw new CampoInvalidoException("O pedido deve ter pelo menos um item.");
        }
        if(!isValorTotalValido(totalCalculado)){
            throw new ValorMinimoException("Valor total abaixo do mínimo permitido: R$ " + VALOR_MINIMO);
        }
    }

    private boolean validarStatus(StatusPedido statusAtual) {
        List<StatusPedido> statusCancelaveis = List.of(StatusPedido.APROVADO,StatusPedido.PROCESSANDO);
        return !statusCancelaveis.contains(statusAtual);
    }

    private boolean isListaDeItensVazia(List<ItemPedidoRequestDTO> itens) {
        return itens == null || itens.isEmpty();
    }

    private boolean isValorTotalValido(BigDecimal total) {
        return total != null && total.compareTo(VALOR_MINIMO) >= 0;
    }
}
