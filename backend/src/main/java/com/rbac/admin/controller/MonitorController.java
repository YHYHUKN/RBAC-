package com.rbac.admin.controller;

import com.rbac.admin.common.OnlineUserRegistry;
import com.rbac.admin.common.Result;
import com.rbac.admin.service.MonitorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    private final MonitorService monitorService;

    public MonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping("/server")
    public Result<Map<String, Object>> server() {
        return Result.success(monitorService.serverInfo());
    }

    @GetMapping("/online")
    public Result<List<OnlineUserRegistry.OnlineUser>> online() {
        return Result.success(monitorService.onlineUsers());
    }
}
