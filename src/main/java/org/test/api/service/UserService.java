package org.test.api.service;

import org.springframework.stereotype.Service;
import org.test.api.dto.UserRequestDto;
import org.test.api.dto.UserResponseDto;

@Service
public interface UserService {

    UserResponseDto getUserById(UserRequestDto requestDto);

}
