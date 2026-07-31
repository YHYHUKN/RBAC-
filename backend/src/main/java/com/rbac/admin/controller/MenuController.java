package com.rbac.admin.controller;

import com.rbac.admin.common.Result;
import com.rbac.admin.dto.MenuVO;
import com.rbac.admin.entity.Menu;
import com.rbac.admin.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/tree")
    public Result<List<MenuVO>> tree() {
        return Result.success(menuService.tree());
    }

    @GetMapping
    public Result<List<Menu>> all() {
        return Result.success(menuService.findAll());
    }

    @PostMapping
    public Result<Menu> save(@RequestBody Menu menu) {
        return Result.success(menuService.save(menu));
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success();
    }
}
