package br.gov.sp.cps.produtos.endpoint;

import br.gov.sp.cps.produtos.model.ConsultarProdutoRequest;
import br.gov.sp.cps.produtos.model.ConsultarProdutoResponse;

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
            response.setNome("Shape 8.0");
            response.setDescricao("Maple Canadense");
            response.setMarca("Pro Model Luan de Oliveira");
            response.setQuantidadeEstoque(6);

        } else if (request.getCodigo() == 2) {
            response.setNome("Rolamento");
            response.setDescricao("Cerâmica");
            response.setMarca("Bronson Speed Co.");
            response.setQuantidadeEstoque(4);

        } else {
            response.setNome("Produto não encontrado");
            response.setDescricao("-");
            response.setMarca("-");
            response.setQuantidadeEstoque(0);
        }

        return response;
    }
}