package com.rbac.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class SaveRoleRequest {
    private Long id;
    private String code;
    private String name;
    private String remark;
    private List<Long> menuIds;
}
