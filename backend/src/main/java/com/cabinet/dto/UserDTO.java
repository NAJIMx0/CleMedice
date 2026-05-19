package com.cabinet.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nom;
    private String email;
    private String role;
    private boolean enabled;
}
