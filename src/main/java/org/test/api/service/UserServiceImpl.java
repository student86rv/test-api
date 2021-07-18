package org.test.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.test.api.dto.UserRequestDto;
import org.test.api.dto.UserResponseDto;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    private static final long SAVED_USER_ID = 1L;

    private static final String SAVED_USER_NAME = "Test Testov";

    @Value("${crypto.password}")
    private String password;

    public UserResponseDto getUserById(final UserRequestDto requestDto) {
        logEncryptedBody(requestDto);
        UserResponseDto responseDto = null;
        if (requestDto.getId() == SAVED_USER_ID) {
            responseDto = new UserResponseDto(SAVED_USER_NAME);
        }
        logEncryptedBody(responseDto);
        return responseDto;
    }

    /*
    There is a little redundancy in the code. Thanks to Spring annotations we don't need to explicitly serialize and
    deserialize our DTOs in controller. But here I use Jackson "manually" to complete this part of the task as closely
    as possible to its description and not to use <dto>.toString() methods.
     */
    private void logEncryptedBody(final Object body) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String json = objectMapper.writeValueAsString(body);

            final String salt = KeyGenerators.string().generateKey();
            final TextEncryptor encryptor = Encryptors.text(password, salt);
            final String encryptedBody = encryptor.encrypt(json);
            final String decryptedBody = encryptor.decrypt(encryptedBody);

            LOGGER.info("=== encryption: {}", encryptedBody);
            LOGGER.info("=== decryption: {}", decryptedBody);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to log encrypted message due to serialization issue: {}", e.getMessage());
        }
    }
}
