package org.test.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.test.api.dto.UserRequestDto;
import org.test.api.dto.UserResponseDto;
import org.test.api.service.UserService;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest {

    private static final String API_URL = "/api/v1/users";
    private static final long SAVED_USER_ID = 1L;
    private static final String SAVED_USER_NAME = "Test Testov";

    private final UserService userService = Mockito.mock(UserService.class);

    private final UserController testedInstance = new UserController(userService);

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(testedInstance).build();

    private final UserRequestDto requestDto = new UserRequestDto(SAVED_USER_ID);

    @Test
    public void shouldGetUser() throws Exception {
        final UserResponseDto expected = new UserResponseDto(SAVED_USER_NAME);
        when(userService.getUserById(requestDto)).thenReturn(expected);
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
        when(userService.getUserById(any(UserRequestDto.class))).thenReturn(null);
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
