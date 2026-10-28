package br.gov.sp.cps.produtos.endpoint;

import br.gov.sp.cps.produtos.model.ConsultarProdutoRequest;
import br.gov.sp.cps.produtos.model.ConsultarProdutoResponse;
import br.gov.sp.cps.produtos.exception.ProdutoNaoEncontradoException;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ProdutoEndpoint {

    private static final String NAMESPACE = "http://cps.sp.gov.br/produtos";

    @PayloadRoot(
        namespace = NAMESPACE,
        localPart = "consultarProdutoRequest"
    )
    @ResponsePayload
    public ConsultarProdutoResponse consultarProduto(
            @RequestPayload ConsultarProdutoRequest request) {

        ConsultarProdutoResponse response = new ConsultarProdutoResponse();

        if (request.getCodigo() == 1) {
            response.setNome("Teclado Mecânico RGB");
            response.setDescricao("Switch Red, layout ABNT2");
            response.setMarca("Corsair");
            response.setQuantidadeEstoque(15);

        } else if (request.getCodigo() == 2) {
            response.setNome("Monitor 24\u0027 Full HD");
            response.setDescricao("IPS, 144Hz, HDMI e DisplayPort");
            response.setMarca("AOC");
            response.setQuantidadeEstoque(8);

        } else {
            throw new ProdutoNaoEncontradoException(
                    "Produto não encontrado: " + request.getCodigo());
        }

        return response;
    }
}