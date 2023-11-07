package com.flavour.fusion.app.controller.user;

import com.flavour.fusion.app.model.auth.AuthRequest;
import com.flavour.fusion.app.model.auth.AuthResponse;
import com.flavour.fusion.app.model.envelope.ResponsePayload;
import com.flavour.fusion.app.model.user.UserDto;
import com.flavour.fusion.app.security.JWTUtil;
import com.flavour.fusion.app.security.PBKDF2Encoder;
import com.flavour.fusion.app.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1.0/flavour-fusion/auth")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthController {

    private final UserService userService;
    private final PBKDF2Encoder encoder;
    private final JWTUtil jwtUtil;

    @PostMapping("/register")
    public Mono<ResponseEntity<ResponsePayload>> registerUser(ServerHttpRequest request, @RequestBody UserDto userDto) {
        return userService.createUser(userDto, request.getURI().getPath())
                .map(responsePayload -> ResponseEntity.created(request.getURI()).body(responsePayload));
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<ResponsePayload>> login(ServerHttpRequest request, @RequestBody AuthRequest ar) {
        return userService.findByUsername(ar.getUsername())
                .filter(user -> encoder.encode(ar.getPassword()).equals(user.getPassword())).map(user -> {
                    String token = jwtUtil.generateToken(user);
                    String refreshToken = jwtUtil.generateRefreshToken(user);
                    String expirationDate = jwtUtil.getExpirationDateFromToken(token).toString();
                    ResponsePayload responsePayload = new ResponsePayload(request.getURI().getPath(), HttpStatus.OK);
                    responsePayload.put("response", new AuthResponse(token, refreshToken, expirationDate));
                    return ResponseEntity.ok(responsePayload);
                }).switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @GetMapping("/refresh")
    public Mono<ResponseEntity<ResponsePayload>> refresh(ServerHttpRequest request,
            @RequestHeader("Refresh-Token") String refreshToken) {
        String newToken = jwtUtil.generateTokenFromRefreshToken(refreshToken);
        ResponsePayload responsePayload = new ResponsePayload(request.getURI().getPath(), HttpStatus.OK);
        responsePayload.put("response",
                new AuthResponse(newToken, refreshToken, jwtUtil.getExpirationDateFromToken(newToken).toString()));
        return Mono.just(ResponseEntity.ok(responsePayload))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()));
    }

    @PutMapping("/change-password")
    public Mono<ResponseEntity<ResponsePayload>> changePassword(ServerHttpRequest request,
            @RequestBody Map<String, Object> payload) {
        return userService.changePassword(request.getURI().getPath(), ((String) payload.get("email")),
                ((String) payload.get("newPassword"))).map(ResponseEntity::ok);
    }
}
