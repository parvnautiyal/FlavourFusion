package com.flavour.fusion.app.model.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Address {
    private AddressCategory category;
    private String nickname;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String landmark;
    private String pincode;
    private String city;
    private String state;
    private String country;
    private String phoneNumber;
}
