package com.flavour.fusion.app.model.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuthResponse {
    private String token;
    private String refreshToken;
    private String expirationDate;
}
