package com.github.ggarabs.user_service.adapter.in;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ggarabs.user_service.port.in.RegisterUserCommand;

@SpringBootTest(classes = com.github.ggarabs.user_service.UserServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(locations = "classpath:/.env")
class AuthIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Test
        void shouldRegisterNewUserSuccessfully() throws Exception {
                RegisterUserCommand command = new RegisterUserCommand(
                                "Gustavo Integration",
                                "gustavo.integration@email.com",
                                "password123",
                                List.of("WORKER"));

                mockMvc.perform(post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.email").value("gustavo.integration@email.com"))
                                .andExpect(jsonPath("$.name").value("Gustavo Integration"));
        }

        @Test
        void shouldReturnConflictWhenRegisteringDuplicateEmail() throws Exception {
                RegisterUserCommand command = new RegisterUserCommand(
                                "Duplicado",
                                "duplicado@email.com",
                                "password123",
                                List.of("EMPLOYER"));

                mockMvc.perform(post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command)))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/api/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(command)))
                                .andExpect(status().isConflict())
                                .andExpect(jsonPath("$.message").value("E-mail já cadastrado."));
        }
}