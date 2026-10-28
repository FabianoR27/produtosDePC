package br.gov.sp.cps.produtos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"ws.security.username=test-user",
		"ws.security.password=test-password"
})
class ProdutosApplicationTests {

	@Test
	void contextLoads() {
	}

}
