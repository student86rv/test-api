package org.test.api.controller;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.test.api.dto.UserRequestDto;
import org.test.api.dto.UserResponseDto;
import org.test.api.service.UserService;

@RestController
@AllArgsConstructor
public class UserController {

    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserResponseDto> getUser(@RequestBody UserRequestDto requestDto) {
        LOGGER.info("Request received with id {}", requestDto.getId());

        final UserResponseDto responseDto = userService.getUserById(requestDto);
        HttpStatus status;
        if (responseDto != null) {
            status = HttpStatus.OK;
            LOGGER.info("Response sent with name = {} and status {}", responseDto.getFullName(), status);
        } else {
            status = HttpStatus.NOT_FOUND;
            LOGGER.info("User with id = {} not found. Response sent with status {}",
                    requestDto.getId(), status);
        }
        return new ResponseEntity<UserResponseDto>(responseDto, status);
    }
}
