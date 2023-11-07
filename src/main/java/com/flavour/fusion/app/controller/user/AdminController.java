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

@RestController
@RequestMapping("/api/v1.0/flavour-fusion/admin")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminController {

    private final ProtectedUserService protectedUserService;

    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ResponsePayload>> retrieveAdminDetails(ServerHttpRequest request,
            @RequestParam("username") String username) {
        return protectedUserService.retrieveUserDetails(request.getURI().getPath(), username).map(ResponseEntity::ok);
    }

    @PostMapping("/change-status")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ResponsePayload>> changeUserStatus(ServerHttpRequest request,
            @RequestParam("username") String username) {
        return protectedUserService.changeUserStatus(request.getURI().getPath(), username).map(ResponseEntity::ok);
    }

    @GetMapping("/all-users/{pageNumber}/{pageSize}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<ResponsePayload>> getAllUsers(ServerHttpRequest request,
            @PathVariable("pageNumber") String pageNumber, @PathVariable("pageSize") String pageSize) {
        return protectedUserService
                .getAllUsers(request.getURI().getPath(), Integer.parseInt(pageNumber) - 1, Integer.parseInt(pageSize))
                .map(ResponseEntity::ok);
    }

}
