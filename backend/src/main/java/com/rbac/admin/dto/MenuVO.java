package com.rbac.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuVO {
    private Long id;
    private Long parentId;
    private String name;
    private Integer type;
    private String path;
    private String component;
    private String icon;
    private String permission;
    private Integer sort;
    private List<MenuVO> children;
}
