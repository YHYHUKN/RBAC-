package com.rbac.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserInfo {
    private String username;
    private String name;
    private List<String> roles;
    private List<MenuVO> menus;
}
