package com.rbac.admin.config;

import lombok.Data;

import java.util.List;

@Data
public class JwtUser {
    private String username;
    private List<String> roles;
}
