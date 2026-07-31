package com.rbac.admin.service;

import com.rbac.admin.entity.JobConfig;
import com.rbac.admin.job.SampleJob;
import com.rbac.admin.repository.JobConfigRepository;
import com.rbac.admin.common.PageResult;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

/**
 * 定时任务服务：基于 Quartz 内存调度（RamJobStore），任务配置持久化在 sys_job 表。
 * 应用启动时（CommandLineRunner）自动加载所有 status=1 的任务进入调度器。
 */
@Service
public class JobService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobConfigRepository repository;
    private final Scheduler scheduler;

    public JobService(JobConfigRepository repository, Scheduler scheduler) {
        this.repository = repository;
        this.scheduler = scheduler;
    }

    public PageResult<JobConfig> list(String keyword, int page, int size) {
        List<JobConfig> all = repository.findAll(
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"));
        List<JobConfig> filtered = (keyword == null || keyword.isBlank())
                ? all
                : all.stream().filter(j -> j.getJobName() != null && j.getJobName().contains(keyword)).toList();
        int from = Math.min((page - 1) * size, filtered.size());
        int to = Math.min(from + size, filtered.size());
        return PageResult.of(filtered.size(), filtered.subList(from, to));
    }

    public JobConfig get(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("任务不存在"));
    }

    @Transactional
    public JobConfig save(JobConfig cfg) {
        boolean isNew = cfg.getId() == null;
        if (!isNew) {
            unschedule(cfg.getId());
        }
        JobConfig saved = repository.save(cfg);
        if (saved.getStatus() != null && saved.getStatus() == 1) {
            try {
                schedule(saved);
            } catch (SchedulerException e) {
                throw new RuntimeException("调度任务失败: " + e.getMessage(), e);
            }
        }
        return saved;
    }

    @Transactional
    public void delete(Long id) {
        unschedule(id);
        repository.deleteById(id);
    }

    @Transactional
    public void pause(Long id) {
        JobConfig cfg = get(id);
        cfg.setStatus(0);
        repository.save(cfg);
        unschedule(id);
    }

    @Transactional
    public void resume(Long id) {
        JobConfig cfg = get(id);
        cfg.setStatus(1);
        repository.save(cfg);
        try {
            schedule(cfg);
        } catch (SchedulerException e) {
            throw new RuntimeException("恢复任务失败: " + e.getMessage(), e);
        }
    }

    public void runOnce(Long id) {
        JobConfig cfg = get(id);
        JobKey key = new JobKey(cfg.getJobName(), cfg.getJobGroup());
        try {
            if (!scheduler.checkExists(key)) {
                schedule(cfg);
            }
            scheduler.triggerJob(key);
        } catch (SchedulerException e) {
            throw new RuntimeException("立即执行失败: " + e.getMessage(), e);
        }
    }

    private void schedule(JobConfig cfg) throws SchedulerException {
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
        JobKey key = new JobKey(cfg.getJobName(), cfg.getJobGroup());
        if (scheduler.checkExists(key)) {
            scheduler.deleteJob(key);
        }
        JobDetail detail = JobBuilder.newJob(SampleJob.class)
                .withIdentity(key)
                .withDescription(cfg.getDescription())
                .storeDurably()
                .build();
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(cfg.getJobName() + "_trigger", cfg.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(cfg.getCron()))
                .build();
        scheduler.scheduleJob(detail, trigger);
    }

    private void unschedule(Long id) {
        repository.findById(id).ifPresent(cfg -> {
            try {
                scheduler.deleteJob(new JobKey(cfg.getJobName(), cfg.getJobGroup()));
            } catch (SchedulerException ignored) {
                log.warn("移除调度任务失败: {}", cfg.getJobName());
            }
        });
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            JobConfig demo = new JobConfig();
            demo.setJobName("demoHeartbeat");
            demo.setJobGroup("DEFAULT");
            demo.setCron("0/30 * * * * ?");
            demo.setDescription("演示任务：每30秒打印一次心跳");
            demo.setStatus(1);
            demo.setBeanName("sampleJob");
            repository.save(demo);
        }
        List<JobConfig> active = repository.findAll().stream()
                .filter(c -> c.getStatus() != null && c.getStatus() == 1)
                .toList();
        for (JobConfig cfg : active) {
            try {
                schedule(cfg);
            } catch (SchedulerException e) {
                log.warn("启动调度任务失败: {}", cfg.getJobName(), e);
            }
        }
        log.info("定时任务调度器初始化完成，已加载 {} 个运行中的任务", active.size());
    }
}
