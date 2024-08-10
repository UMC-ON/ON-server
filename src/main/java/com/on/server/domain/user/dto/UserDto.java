package com.on.server.domain.user.dto;

import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String token;


}
