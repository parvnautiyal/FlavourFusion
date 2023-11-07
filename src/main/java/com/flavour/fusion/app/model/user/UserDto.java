package com.flavour.fusion.app.model.user;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private String name;
    private String username;
    private String email;
    private String password;
    private String dateOfBirth;
    private Address address;
    private String phoneNumber;
    private Set<Role> roles;
}
