package br.gov.sp.cps.produtos.config;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.Assert;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurer;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig implements WsConfigurer {

    private final String username;
    private final String password;

    public WebServiceConfig(
            @Value("${ws.security.username}") String username,
            @Value("${ws.security.password}") String password) {
        Assert.hasText(username, "WS-Security username must not be empty");
        Assert.hasText(password, "WS-Security password must not be empty");
        this.username = username;
        this.password = password;
    }

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext context) {

        MessageDispatcherServlet servlet = new MessageDispatcherServlet();

        servlet.setApplicationContext(context);
        servlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean<>(servlet, "/ws/*");
    }

    @Bean(name = "produtos")
    public DefaultWsdl11Definition produtosWsdl(XsdSchema produtosSchema) {

        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();

        wsdl.setPortTypeName("ProdutosPort");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://cps.sp.gov.br/produtos");
        wsdl.setSchema(produtosSchema);

        return wsdl;
    }

    @Bean
    public XsdSchema produtosSchema() {
        return new SimpleXsdSchema(
                new ClassPathResource("produtos.xsd")
        );
    }

    @Bean
    public SimplePasswordValidationCallbackHandler passwordValidationCallbackHandler() {

        SimplePasswordValidationCallbackHandler handler =
                new SimplePasswordValidationCallbackHandler();
        handler.setUsersMap(Map.of(username, password));
        return handler;
    }

    @Bean
    public Wss4jSecurityInterceptor securityInterceptor(
            SimplePasswordValidationCallbackHandler passwordValidationCallbackHandler) {

        Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
        interceptor.setValidationActions("UsernameToken");
        interceptor.setValidationCallbackHandler(passwordValidationCallbackHandler);
        interceptor.setSecureResponse(false);
        return interceptor;
    }

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(securityInterceptor(passwordValidationCallbackHandler()));
    }
}