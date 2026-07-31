package com.rbac.admin.controller;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.common.Result;
import com.rbac.admin.entity.Announcement;
import com.rbac.admin.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

    private final AnnouncementService service;

    public AnnouncementController(AnnouncementService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public Result<PageResult<Announcement>> list(@RequestParam(defaultValue = "") String keyword,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return Result.success(service.list(keyword, page, size));
    }

    @PostMapping
    public Result<Announcement> save(@RequestBody Announcement a) {
        return Result.success(service.save(a));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }

    @PostMapping("/{id}/publish")
    public Result<Announcement> publish(@PathVariable Long id) {
        return Result.success(service.publish(id));
    }

    @PostMapping("/{id}/unpublish")
    public Result<Announcement> unpublish(@PathVariable Long id) {
        return Result.success(service.unpublish(id));
    }
}
