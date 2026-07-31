package com.rbac.admin.controller;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.common.Result;
import com.rbac.admin.entity.JobConfig;
import com.rbac.admin.service.JobService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/job")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public Result<PageResult<JobConfig>> list(@RequestParam(defaultValue = "") String keyword,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        return Result.success(jobService.list(keyword, page, size));
    }

    @PostMapping
    public Result<JobConfig> save(@RequestBody JobConfig cfg) {
        return Result.success(jobService.save(cfg));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        jobService.delete(id);
        return Result.success();
    }

    @PostMapping("/{id}/pause")
    public Result<Void> pause(@PathVariable Long id) {
        jobService.pause(id);
        return Result.success();
    }

    @PostMapping("/{id}/resume")
    public Result<Void> resume(@PathVariable Long id) {
        jobService.resume(id);
        return Result.success();
    }

    @PostMapping("/{id}/run")
    public Result<Void> run(@PathVariable Long id) {
        jobService.runOnce(id);
        return Result.success();
    }
}
