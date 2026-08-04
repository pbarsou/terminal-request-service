package com.desafio.terminalrequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.desafio.terminalrequest.integrated.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

class TerminalRequestApplicationTests extends IntegrationTest {

  @Autowired private ApplicationContext applicationContext;

  @Nested
  @DisplayName("Context Loading")
  class ContextLoading {

    @Test
    @DisplayName("Should load application context successfully")
    void testLoadApplicationContext() {
      assertNotNull(applicationContext);
    }

    @Test
    @DisplayName("Should have TerminalRequestApplication bean")
    void testTerminalRequestApplicationBean() {
      TerminalRequestApplication application =
          applicationContext.getBean(TerminalRequestApplication.class);
      assertNotNull(application);
    }
  }
}
