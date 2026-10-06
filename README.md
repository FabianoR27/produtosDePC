# 💻 P1 - Catálogo de Periféricos para PC

## Visão geral

Este projeto foi desenvolvido em **Java + Spring Boot** para expor um **Web Service SOAP** responsável por consultar informações de periféricos para PC a partir do código informado.

O serviço permite recuperar os dados principais do item, como:

- Nome
- Descrição
- Marca
- Quantidade em estoque

---

## Objetivo

Disponibilizar uma API SOAP simples e funcional para consulta de periféricos de computador, seguindo os princípios de interoperabilidade e padronização do protocolo SOAP.

---

## Requisitos

- Java JDK compatível com o projeto
- Maven
- VS Code ou qualquer IDE Java
- Insomnia para testes da requisição SOAP

---

## ▶️ Como executar

### 1. Abrir o projeto

Abra a pasta do projeto no VS Code.

### 2. Compilar o projeto

No terminal, execute:

```powershell
.\mvnw.cmd clean compile
```

### 3. Iniciar o Web Service

```powershell
.\mvnw.cmd spring-boot:run
```

O serviço será iniciado na porta **8081**. Caso seja necessário alterar a porta, verifique o arquivo **src/main/resources/application.properties**.

---

## 🌐 WSDL

Com a aplicação em execução, o WSDL pode ser acessado no endereço:

```text
http://localhost:8081/ws/produtos.wsdl
```

Se o XML do WSDL for exibido corretamente, o serviço está funcionando.

---

## 🧪 Testando no Insomnia

1. Abra o **Insomnia**.
2. Crie uma nova requisição do tipo **POST**.
3. Informe a URL do endpoint SOAP:

```text
http://localhost:8081/ws
```

4. Na aba **Body**, selecione **RAW** e defina o tipo como **XML**.
5. Envie a seguinte estrutura SOAP:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:prod="http://cps.sp.gov.br/produtos">
    <soapenv:Header/>
    <soapenv:Body>
        <prod:consultarProdutoRequest>
            <prod:codigo>1</prod:codigo>
        </prod:consultarProdutoRequest>
    </soapenv:Body>
</soapenv:Envelope>
```

6. Clique em **Send**.

A resposta retornará os dados do periférico solicitado.

---

## 📤 Exemplo de resposta esperada

```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
    <SOAP-ENV:Header/>
    <SOAP-ENV:Body>
        <ns2:consultarProdutoResponse xmlns:ns2="http://cps.sp.gov.br/produtos">
            <ns2:nome>Teclado Mecânico RGB</ns2:nome>
            <ns2:descricao>Switch Red, layout ABNT2</ns2:descricao>
            <ns2:marca>Corsair</ns2:marca>
            <ns2:quantidadeEstoque>15</ns2:quantidadeEstoque>
        </ns2:consultarProdutoResponse>
    </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

---

## 📝 Observações

Este projeto foi desenvolvido como estudo prático de **serviços web SOAP** com **Spring Boot**, servindo como base para compreensão da criação de contratos, endpoints e consumo de WSDL em aplicações Java.
