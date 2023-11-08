package com.flavour.fusion.app.controller.user;

import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.Address;
import com.flavour.fusion.app.service.user.ProtectedUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1.0/flavour-fusion/user")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final ProtectedUserService protectedUserService;

    @GetMapping("/details")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> retrieveUserDetails(ServerHttpRequest request,
            @RequestParam("username") String username) {
        return protectedUserService.retrieveUserDetails(request.getURI().getPath(), username).map(ResponseEntity::ok);
    }

    @PatchMapping("/update-address")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> updateAddress(ServerHttpRequest request, @RequestBody Address address,
            @RequestParam("username") String username) {
        return protectedUserService.updateAddress(request.getURI().getPath(), username, address)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/add-address")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> addAddress(ServerHttpRequest request, @RequestBody Address address,
            @RequestParam("username") String username) {
        return protectedUserService.addAddress(request.getURI().getPath(), username, address).map(ResponseEntity::ok);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<ResponsePayload>> deleteUser(ServerHttpRequest request,
            @RequestParam("username") String username) {
        return protectedUserService.deleteUser(request.getURI().getPath(), username).map(ResponseEntity::ok);
    }
}
