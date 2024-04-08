package pl.lodz.p.it.ssbd2024.ssbd01;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.HandlerExceptionResolver;
import pl.lodz.p.it.ssbd2024.ssbd01.config.WebConfig;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Account;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mok.Role;
import pl.lodz.p.it.ssbd2024.ssbd01.entities.mow.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(classes = {WebConfig.class})
public class TestHelloWorld {

    @Autowired
    private HelloWorldController helloWorldController;


    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private MockMvc mockMvc;


    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(helloWorldController)
                .setHandlerExceptionResolvers(handlerExceptionResolver)
                .build();

    }

    @Test
    public void testHelloWorldEndpoint() throws Exception {
        mockMvc.perform(get("/api/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

}