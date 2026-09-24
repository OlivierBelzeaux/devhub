package com.olivier.devhub;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "app.security.jwt.secret=test-only-secret-that-is-long-enough-for-hs256")
class DevhubApplicationTests {

	@Test
	void contextLoads() {
	}

}
