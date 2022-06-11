package com.hugo.urlshortner.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hugo.urlshortner.model.dto.UrlDTO;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UrlControllerTest {

    private static final String API = "/v1/api/url";

    private static final String JSON_CONTENT_TYPE = "application/json";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static UrlDTO urlDTO;

    @BeforeAll
    public void setup() {
        log.info("--- STARTING TESTS ON CONTROLLER ---");

        urlDTO = UrlDTO.builder()
                .originalUrl("www.facebook.com2")
                .build();
    }

    @Order(1)
    @Test
    @Rollback
    void shouldSaveClient() throws Exception {
        mockMvc.perform(post(API)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(urlDTO)))
                .andExpect(status().isCreated());
    }

    @Order(2)
    @Test
    void shouldNotSaveUrl() throws Exception {
        urlDTO.setOriginalUrl(StringUtils.EMPTY);
        mockMvc.perform(post(API)
                        .contentType(JSON_CONTENT_TYPE)
                        .content(objectMapper.writeValueAsString(urlDTO)))
                .andExpect(status().isBadRequest());
    }

    @Order(3)
    @Test
    void shouldNotDeleteUrl() throws Exception {
        mockMvc.perform(delete(API.concat("/0")))
                .andExpect(status().isNotFound());
    }

}
