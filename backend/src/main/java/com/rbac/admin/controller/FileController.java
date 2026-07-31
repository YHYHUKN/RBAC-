package com.rbac.admin.controller;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.common.Result;
import com.rbac.admin.entity.FileInfo;
import com.rbac.admin.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public Result<FileInfo> upload(@RequestParam("file") MultipartFile file) {
        return Result.success(fileService.upload(file));
    }

    @GetMapping
    public Result<PageResult<FileInfo>> list(@RequestParam(defaultValue = "") String name,
                                             @RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return Result.success(fileService.list(name, page, size));
    }

    @GetMapping("/{id}/download")
    public void download(@PathVariable Long id, HttpServletResponse response) throws IOException {
        FileInfo info = fileService.get(id);
        File f = new File(info.getPath());
        if (!f.exists()) {
            response.sendError(404, "文件不存在");
            return;
        }
        response.setContentType(info.getContentType());
        response.setHeader("Content-Disposition",
                "attachment; filename=" + URLEncoder.encode(info.getOriginalName(), "UTF-8"));
        Files.copy(f.toPath(), response.getOutputStream());
        response.getOutputStream().flush();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        fileService.delete(id);
        return Result.success();
    }
}
