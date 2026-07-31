package com.rbac.admin.common;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rbac.admin.common.OperationLog;
import com.rbac.admin.repository.OperationLogRepository;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogRepository logRepository;

    @Around("@annotation(opLog)")
    public Object around(ProceedingJoinPoint pjp, OperationLog opLog) throws Throwable {
        String username = SecurityUtils.currentUsername();
        String action = opLog.value();
        if (action == null || action.isEmpty()) {
            action = pjp.getSignature().getName();
        }
        String status = "SUCCESS";
        try {
            return pjp.proceed();
        } catch (Throwable t) {
            status = "FAILED";
            throw t;
        } finally {
            try {
                com.rbac.admin.entity.OperationLog log = new com.rbac.admin.entity.OperationLog();
                log.setUsername(username == null ? "anonymous" : username);
                log.setAction(action);
                log.setMethod(pjp.getSignature().toShortString());
                log.setParams("{}");
                log.setIp("127.0.0.1");
                log.setStatus(status);
                logRepository.save(log);
            } catch (Exception ignored) {
            }
        }
    }
}
