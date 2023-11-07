package com.flavour.fusion.app.service.user;

import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.User;
import com.flavour.fusion.app.model.user.UserDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface UserService {
    Mono<ResponsePayload> createUser(UserDto userDto, String uri);

    Mono<ResponsePayload> changePassword(String uri, String email, String password);

    Mono<User> findByUsername(String username);
}
