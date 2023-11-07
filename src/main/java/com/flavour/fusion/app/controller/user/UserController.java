package com.flavour.fusion.app.controller.user;

import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.service.user.ProtectedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/flavour-fusion/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final ProtectedUserService protectedUserService;

    @GetMapping("/details")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> retrieveUserDetails(ServerHttpRequest request, @RequestParam("username") String username) {
        return protectedUserService.retrieveUserDetails(request.getURI().getPath(), username)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> updateUser(ServerHttpRequest request, @RequestBody Map<String, Object> payload, @RequestParam("username") String username) {
        return protectedUserService.updateUser(request.getURI().getPath(), username, payload)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> deleteUser(ServerHttpRequest request, @RequestParam("username") String username) {
        return protectedUserService.deleteUser(request.getURI().getPath(), username)
                .map(ResponseEntity::ok);
    }
}
