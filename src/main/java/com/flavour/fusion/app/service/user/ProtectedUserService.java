package com.flavour.fusion.app.service.user;

import com.flavour.fusion.app.model.envelope.ResponsePayload;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public interface ProtectedUserService {
    Mono<ResponsePayload> retrieveUserDetails(String uri, String username);

    Mono<ResponsePayload> updateUser(String uri, String username, Map<String, Object> updatedUser);

    Mono<ResponsePayload> deleteUser(String uri, String username);

    Mono<ResponsePayload> changeUserStatus(String uri, String username);
    Mono<ResponsePayload> getAllUsers(String uri, int pageNumber, int pageSize);
}
