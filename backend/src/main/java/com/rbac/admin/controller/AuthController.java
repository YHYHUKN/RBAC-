package com.rbac.admin.controller;

import com.rbac.admin.common.Result;
import com.rbac.admin.dto.LoginRequest;
import com.rbac.admin.dto.LoginResponse;
import com.rbac.admin.dto.UserInfo;
import com.rbac.admin.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest req, HttpServletRequest request) {
        try {
            return Result.success(authService.login(req.getUsername(), req.getPassword(), getClientIp(request)));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String auth) {
        authService.logout(auth);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserInfo> info() {
        return Result.success(authService.currentInfo());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip == null ? "unknown" : ip.split(",")[0].trim();
    }
}
