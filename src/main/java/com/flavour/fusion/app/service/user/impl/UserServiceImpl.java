package com.flavour.fusion.app.service.user.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.User;
import com.flavour.fusion.app.model.user.UserDto;
import com.flavour.fusion.app.repository.UserRepository;
import com.flavour.fusion.app.security.PBKDF2Encoder;
import com.flavour.fusion.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PBKDF2Encoder encoder;

    @Override
    public Mono<ResponsePayload> createUser(UserDto userDto, String uri) {
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        return userRepository.save(objectMapper.convertValue(userDto, User.class)).map(user -> {
            ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.CREATED);
            responsePayload.put("response", user);
            return responsePayload;
        });
    }

    @Override
    public Mono<ResponsePayload> changePassword(String uri, String email, String password) {
        return userRepository.findUserByEmail(email).flatMap(user -> {
            user.setPassword(encoder.encode(password));
            return userRepository.save(user).map(newUser -> {
                ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
                responsePayload.put("message", "password successfully changed");
                return responsePayload;
            });
        });
    }

    @Override
    public Mono<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
