package com.rbac.admin.dto;

import lombok.Data;

@Data
public class SaveDeptRequest {
    private Long id;
    private String name;
    private Long parentId;
    private Integer sort;
}
