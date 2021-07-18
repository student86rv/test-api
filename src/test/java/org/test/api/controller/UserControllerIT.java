package org.test.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.test.api.dto.UserRequestDto;
import org.test.api.dto.UserResponseDto;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration
public class UserControllerIT {

    private static final String API_URL = "/api/v1/users";
    private static final long SAVED_USER_ID = 1L;
    private static final String SAVED_USER_NAME = "Test Testov";

    @Autowired
    protected MockMvc mockMvc;

    @Configuration
    @EnableWebMvc
    @ComponentScan("org.test.api")
    protected static class TestContextConfiguration {

        @Bean
        public MockMvc mockMvc(WebApplicationContext webApplicationContext) {
            return MockMvcBuilders
                    .webAppContextSetup(webApplicationContext)
                    .alwaysDo(print())
                    .build();
        }
    }

    @Test
    public void shouldGetUser() throws Exception {
        final UserResponseDto expected = new UserResponseDto(SAVED_USER_NAME);
        final UserRequestDto requestDto = new UserRequestDto(SAVED_USER_ID);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(requestDto);

        final MvcResult result = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        final String content = result.getResponse().getContentAsString();
        final UserResponseDto actual = objectMapper.readValue(content, UserResponseDto.class);

        assertEquals("Should be equals", expected, actual);
    }

    @Test
    public void shouldGetNullAndNotFoundStatus() throws Exception {
        final UserRequestDto requestDto = new UserRequestDto(100500L);
        final ObjectMapper objectMapper = new ObjectMapper();
        final String json = objectMapper.writeValueAsString(requestDto);

        final MvcResult result = mockMvc.perform(post(API_URL)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(json))
                .andExpect(status().isNotFound())
                .andReturn();

        final String content = result.getResponse().getContentAsString();

        assertEquals("Should be empty", "", content);
    }
}
