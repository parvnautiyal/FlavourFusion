package com.flavour.fusion.app.service.user.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.Address;
import com.flavour.fusion.app.repository.UserRepository;
import com.flavour.fusion.app.service.user.ProtectedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProtectedUserServiceImpl implements ProtectedUserService {

    private static final String RESPONSE = "response";

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<ResponsePayload> retrieveUserDetails(String uri, String username) {
        return userRepository.findByUsername(username).map(user -> {
            ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
            responsePayload.put(RESPONSE, user);
            return responsePayload;
        });
    }

    @Override
    public Mono<ResponsePayload> updateAddress(String uri, String username, Address updatedAddress) {
        return userRepository.findByUsername(username).flatMap(user -> {
            List<Address> newAddressList = new ArrayList<>(user.getAddress().stream()
                    .filter(address -> !address.getNickname().equals(updatedAddress.getNickname())).toList());
            newAddressList.add(updatedAddress);
            user.setAddress(newAddressList);
            return userRepository.save(user).map(patchUser -> {
                ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
                responsePayload.put(RESPONSE, patchUser);
                return responsePayload;
            });
        });
    }

    @Override
    public Mono<ResponsePayload> addAddress(String uri, String username, Address address) {
        return userRepository.findByUsername(username).flatMap(user -> {
            List<Address> userAddress = user.getAddress();
            userAddress.add(address);
            user.setAddress(userAddress);
            return userRepository.save(user).map(updatedUser -> {
                ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
                responsePayload.put(RESPONSE, updatedUser);
                return responsePayload;
            });
        });
    }

    @Override
    public Mono<ResponsePayload> deleteUser(String uri, String username) {
        return userRepository.deleteByUsername(username).then(Mono.fromCallable(() -> {
            ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
            responsePayload.put("message", "User with username" + username + " deleted");
            return responsePayload;
        }));
    }

    @Override
    public Mono<ResponsePayload> changeUserStatus(String uri, String username) {
        return userRepository.findByUsername(username).flatMap(user -> {
            user.setEnabled(!user.isEnabled());
            return userRepository.save(user);
        }).map(updateUser -> {
            ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
            String message = updateUser.isEnabled() ? "User enabled" : "User disabled";
            responsePayload.put("message", message);
            return responsePayload;
        });
    }

    @Override
    public Mono<ResponsePayload> getAllUsers(String uri, int pageNumber, int pageSize) {
        return userRepository.findAllUsers(PageRequest.of(pageNumber, pageSize)).collectList().map(users -> {
            ResponsePayload responsePayload = new ResponsePayload(uri, HttpStatus.OK);
            responsePayload.put(RESPONSE, Collections.singletonMap("users", users));
            return responsePayload;
        });
    }
}
