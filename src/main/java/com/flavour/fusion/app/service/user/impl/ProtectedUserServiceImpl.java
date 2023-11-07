package com.flavour.fusion.app.service.user.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.Address;
import com.flavour.fusion.app.repository.UserRepository;
import com.flavour.fusion.app.service.user.ProtectedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProtectedUserServiceImpl implements ProtectedUserService {

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<ResponsePayload> retrieveUserDetails(String uri, String username) {
        return userRepository.findByUsername(username)
                .map(user -> {
                    ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
                    responsePayload.put("response", user);
                    return responsePayload;
                });
    }

    @Override
    public Mono<ResponsePayload> updateUser(String uri, String username, Map<String, Object> updatedUser) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    Address updatedAddress = objectMapper.convertValue(updatedUser.get("updatedAddress"), Address.class);
                    user.setAddress(updatedAddress);
                    user.setPhoneNumber(((String) updatedUser.get("updatedPhoneNumber")));
                    return userRepository.save(user)
                            .map(patchUser -> {
                                ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
                                responsePayload.put("response", patchUser);
                                return responsePayload;
                            });
                });
    }

    @Override
    public Mono<ResponsePayload> deleteUser(String uri, String username) {
        return userRepository.deleteByUsername(username)
                .then(Mono.fromCallable(() -> {
                    ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
                    responsePayload.put("message", "User with username" + username + " deleted");
                    return responsePayload;
                }));
    }
}
