package com.rbac.admin.service;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.entity.FileInfo;
import com.rbac.admin.repository.FileInfoRepository;
import com.rbac.admin.common.SecurityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.criteria.Predicate;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    @Value("${app.upload-dir:./uploads}")
    private String uploadDir;

    private final FileInfoRepository fileRepository;

    public FileService(FileInfoRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public FileInfo upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("文件为空");
        }
        try {
            String base = uploadDir;
            File dir = new File(base);
            if (!dir.exists()) dir.mkdirs();
            String ext = "";
            String original = file.getOriginalFilename();
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.') + 1);
            }
            String storageName = UUID.randomUUID() + (ext.isEmpty() ? "" : "." + ext);
            File target = new File(dir, storageName);
            file.transferTo(target);

            FileInfo info = new FileInfo();
            info.setOriginalName(original);
            info.setStorageName(storageName);
            info.setPath(base + "/" + storageName);
            info.setSize(file.getSize());
            info.setExt(ext);
            info.setContentType(file.getContentType());
            info.setUploader(SecurityUtils.currentUsername());
            return fileRepository.save(info);
        } catch (Exception e) {
            throw new RuntimeException("上传失败: " + e.getMessage());
        }
    }

    public PageResult<FileInfo> list(String name, int page, int size) {
        org.springframework.data.jpa.domain.Specification<FileInfo> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (name != null && !name.isEmpty()) {
                list.add(cb.like(root.get("originalName"), "%" + name + "%"));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        Page<FileInfo> p = fileRepository.findAll(spec, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id")));
        return new PageResult<>(p.getTotalElements(), p.getContent());
    }

    public FileInfo get(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new RuntimeException("文件不存在"));
    }

    public void delete(Long id) {
        FileInfo info = get(id);
        File f = new File(info.getPath());
        if (f.exists()) f.delete();
        fileRepository.deleteById(id);
    }
}
