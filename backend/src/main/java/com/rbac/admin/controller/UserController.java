package com.rbac.admin.controller;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.common.Result;
import com.rbac.admin.dto.SaveUserRequest;
import com.rbac.admin.entity.User;
import com.rbac.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Result<PageResult<User>> list(@RequestParam(defaultValue = "1") int page,
                                          @RequestParam(defaultValue = "10") int size,
                                          @RequestParam(required = false) String keyword) {
        return Result.success(userService.list(page, size, keyword));
    }

    @GetMapping("/{id}")
    public Result<User> get(@PathVariable Long id) {
        return Result.success(userService.get(id));
    }

    @PostMapping
    public Result<User> save(@RequestBody SaveUserRequest req) {
        try {
            return Result.success(userService.save(req));
        } catch (RuntimeException e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return Result.success();
    }
}
