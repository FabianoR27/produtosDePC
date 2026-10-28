package br.gov.sp.cps.produtos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import static org.springframework.ws.test.server.RequestCreators.withSoapEnvelope;
import static org.springframework.ws.test.server.ResponseMatchers.clientOrSenderFault;
import static org.springframework.ws.test.server.ResponseMatchers.noFault;
import static org.springframework.ws.test.server.ResponseMatchers.payload;

@SpringBootTest(properties = {
        "ws.security.username=test-user",
        "ws.security.password=test-password"
})
class SoapSecurityIntegrationTests {

    private static final String SOAP_ENVELOPE = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:prod="http://cps.sp.gov.br/produtos"
                              xmlns:wsse="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">
                %s
                <soapenv:Body>
                    <prod:consultarProdutoRequest>
                        <prod:codigo>%d</prod:codigo>
                    </prod:consultarProdutoRequest>
                </soapenv:Body>
            </soapenv:Envelope>
            """;

    private static final String VALID_SECURITY_HEADER = """
            <soapenv:Header>
                <wsse:Security soapenv:mustUnderstand="1">
                    <wsse:UsernameToken>
                        <wsse:Username>test-user</wsse:Username>
                        <wsse:Password Type="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText">test-password</wsse:Password>
                    </wsse:UsernameToken>
                </wsse:Security>
            </soapenv:Header>
            """;

    private static final String INVALID_SECURITY_HEADER = VALID_SECURITY_HEADER
            .replace("test-password", "wrong-password");
    private static final String EMPTY_SECURITY_HEADER = "<soapenv:Header/>";

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient client;

    @BeforeEach
    void setUp() {
        client = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    void acceptsRequestWithValidUsernameToken() throws Exception {
        String request = SOAP_ENVELOPE.formatted(VALID_SECURITY_HEADER, 1);
        String expectedPayload = """
                <prod:consultarProdutoResponse xmlns:prod="http://cps.sp.gov.br/produtos">
                    <prod:nome>Teclado Mecânico RGB</prod:nome>
                    <prod:descricao>Switch Red, layout ABNT2</prod:descricao>
                    <prod:marca>Corsair</prod:marca>
                    <prod:quantidadeEstoque>15</prod:quantidadeEstoque>
                </prod:consultarProdutoResponse>
                """;

        client.sendRequest(withSoapEnvelope(new StringSource(request)))
                .andExpect(noFault())
                .andExpect(payload(new StringSource(expectedPayload)));
    }

    @Test
    void rejectsRequestWithInvalidPassword() throws Exception {
        String request = SOAP_ENVELOPE.formatted(INVALID_SECURITY_HEADER, 1);

        client.sendRequest(withSoapEnvelope(new StringSource(request)))
                .andExpect(clientOrSenderFault());
    }

    @Test
    void rejectsRequestWithoutUsernameToken() throws Exception {
        String request = SOAP_ENVELOPE.formatted(EMPTY_SECURITY_HEADER, 1);

        client.sendRequest(withSoapEnvelope(new StringSource(request)))
                .andExpect(clientOrSenderFault());
    }

    @Test
    void returnsClientFaultWhenProductDoesNotExist() throws Exception {
        String request = SOAP_ENVELOPE.formatted(VALID_SECURITY_HEADER, 999);

        client.sendRequest(withSoapEnvelope(new StringSource(request)))
                .andExpect(clientOrSenderFault());
    }
}
