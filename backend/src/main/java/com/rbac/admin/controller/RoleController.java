package com.rbac.admin.controller;

import com.rbac.admin.common.Result;
import com.rbac.admin.dto.SaveRoleRequest;
import com.rbac.admin.entity.Role;
import com.rbac.admin.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public Result<List<Role>> list() {
        return Result.success(roleService.list());
    }

    @GetMapping("/{id}")
    public Result<Role> get(@PathVariable Long id) {
        return Result.success(roleService.get(id));
    }

    @PostMapping
    public Result<Role> save(@RequestBody SaveRoleRequest req) {
        try {
            return Result.success(roleService.save(req));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }
}
