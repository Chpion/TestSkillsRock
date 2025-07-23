package com.agafonov.Test.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank(message = "FIO cannot be blank")
    private String fio;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+?[0-9\\-\\s()]+$", message = "Invalid phone number format")
    private String phoneNumber;

    @URL(message = "Avatar must be a valid URL")
    private String avatar;

    @NotBlank(message = "Role name cannot be blank")
    private String roleName;
}
