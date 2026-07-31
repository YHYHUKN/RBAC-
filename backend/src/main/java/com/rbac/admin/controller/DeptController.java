package com.rbac.admin.controller;

import com.rbac.admin.common.Result;
import com.rbac.admin.dto.SaveDeptRequest;
import com.rbac.admin.entity.Department;
import com.rbac.admin.service.DeptService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/depts")
public class DeptController {

    private final DeptService deptService;

    public DeptController(DeptService deptService) {
        this.deptService = deptService;
    }

    @GetMapping
    public Result<List<Department>> list() {
        return Result.success(deptService.list());
    }

    @GetMapping("/tree")
    public Result<List<Department>> tree() {
        return Result.success(deptService.tree());
    }

    @PostMapping
    public Result<Department> save(@RequestBody SaveDeptRequest req) {
        return Result.success(deptService.save(req));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        deptService.delete(id);
        return Result.success();
    }
}
