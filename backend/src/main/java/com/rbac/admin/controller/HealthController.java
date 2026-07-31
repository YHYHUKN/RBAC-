package com.rbac.admin.controller;

import com.rbac.admin.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Result<Object> health() {
        return Result.success("UP");
    }
}
