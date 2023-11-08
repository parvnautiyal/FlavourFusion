package com.flavour.fusion.app.service.user;

import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.Address;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ProtectedUserService {
    Mono<ResponsePayload> retrieveUserDetails(String uri, String username);

    Mono<ResponsePayload> updateAddress(String uri, String username, Address updatedAddress);

    Mono<ResponsePayload> addAddress(String uri, String username, Address address);

    Mono<ResponsePayload> deleteUser(String uri, String username);

    Mono<ResponsePayload> changeUserStatus(String uri, String username);

    Mono<ResponsePayload> getAllUsers(String uri, int pageNumber, int pageSize);
}
