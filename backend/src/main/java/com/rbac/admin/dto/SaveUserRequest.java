package com.rbac.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveUserRequest {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private Integer status;
    private Long deptId;
    private List<Long> roleIds;
}
