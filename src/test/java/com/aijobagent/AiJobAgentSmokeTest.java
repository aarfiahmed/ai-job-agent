package com.aijobagent;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties={"app.mock-ai=true","app.daily-enabled=false"})
class AiJobAgentSmokeTest {
    @Test void contextLoads() {}
}
