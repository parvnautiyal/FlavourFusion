package com.flavour.fusion.app.model.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthRequest {
    private String username;
    private String password;
}
