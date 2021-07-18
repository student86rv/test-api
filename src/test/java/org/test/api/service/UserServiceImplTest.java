package org.test.api.service;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.test.api.dto.UserRequestDto;
import org.test.api.dto.UserResponseDto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    private static final long SAVED_USER_ID = 1L;
    private static final String SAVED_USER_NAME = "Test Testov";

    private final UserRequestDto requestDto = Mockito.mock(UserRequestDto.class);

    private UserServiceImpl testedInstance;

    @Before
    public void setUp() {
        when(requestDto.getId()).thenReturn(SAVED_USER_ID);

        testedInstance = new UserServiceImpl();
        ReflectionTestUtils.setField(testedInstance, "password", "password");
    }

    @Test
    public void shouldGetUserById() {
        final UserResponseDto expected = new UserResponseDto(SAVED_USER_NAME);
        final UserResponseDto actual = testedInstance.getUserById(requestDto);

        assertEquals("Should be equals", expected, actual);
    }

    @Test
    public void shouldGetNullIfWrongId() {
        when(requestDto.getId()).thenReturn(100500L);

        final UserResponseDto actual = testedInstance.getUserById(requestDto);

        assertNull("Should be null", actual);
    }
}
