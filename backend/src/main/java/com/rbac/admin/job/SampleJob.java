package com.rbac.admin.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 演示用定时任务。真实项目中可替换为调用业务 Service 的 Job 实现类。
 * 该类由 Quartz 实例化（非 Spring 托管），execute 内仅做日志演示。
 */
public class SampleJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(SampleJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        String group = context.getJobDetail().getKey().getGroup();
        log.info("定时任务执行 -> jobName={}, group={}, fireTime={}", jobName, group, context.getFireTime());
        System.out.println("[Quartz] SampleJob executed: " + jobName + " @ " + context.getFireTime());
    }
}
