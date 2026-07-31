package com.rbac.admin.service;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.entity.Announcement;
import com.rbac.admin.repository.AnnouncementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {

    private final AnnouncementRepository repository;

    public AnnouncementService(AnnouncementRepository repository) {
        this.repository = repository;
    }

    public PageResult<Announcement> list(String keyword, int page, int size) {
        List<Announcement> all = repository.findAll(
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createTime"));
        List<Announcement> filtered = (keyword == null || keyword.isBlank())
                ? all
                : all.stream().filter(a -> a.getTitle() != null && a.getTitle().contains(keyword)).toList();
        int from = Math.min((page - 1) * size, filtered.size());
        int to = Math.min(from + size, filtered.size());
        return PageResult.of(filtered.size(), filtered.subList(from, to));
    }

    public Announcement get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("公告不存在"));
    }

    public Announcement save(Announcement a) {
        if (a.getId() != null) {
            Announcement old = get(a.getId());
            if (a.getStatus() == null) {
                a.setStatus(old.getStatus());
            }
        }
        return repository.save(a);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Announcement publish(Long id) {
        Announcement a = get(id);
        a.setStatus(1);
        a.setPublishTime(LocalDateTime.now());
        return repository.save(a);
    }

    public Announcement unpublish(Long id) {
        Announcement a = get(id);
        a.setStatus(0);
        return repository.save(a);
    }
}
