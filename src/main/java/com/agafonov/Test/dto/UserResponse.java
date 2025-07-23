package com.agafonov.Test.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private UUID id;

    private String fio;

    private String phoneNumber;

    private String avatar;

    private String roleName;

}
