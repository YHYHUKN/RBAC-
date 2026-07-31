package com.rbac.admin.controller;

import com.rbac.admin.common.Result;
import com.rbac.admin.entity.OperationLog;
import com.rbac.admin.repository.OperationLogRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class OperationLogController {

    private final OperationLogRepository logRepository;

    public OperationLogController(OperationLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @GetMapping
    public Result<List<OperationLog>> list(@RequestParam(defaultValue = "20") int limit) {
        List<OperationLog> all = logRepository.findAllByOrderByCreateTimeDesc();
        return Result.success(all.stream().limit(limit).toList());
    }
}
