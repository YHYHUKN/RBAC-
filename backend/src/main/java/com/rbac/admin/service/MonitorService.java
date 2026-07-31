package com.rbac.admin.service;

import com.rbac.admin.common.OnlineUserRegistry;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.io.File;
import java.util.*;

@Service
public class MonitorService {

    private final OnlineUserRegistry onlineUserRegistry;

    public MonitorService(OnlineUserRegistry onlineUserRegistry) {
        this.onlineUserRegistry = onlineUserRegistry;
    }

    public Map<String, Object> serverInfo() {
        Map<String, Object> data = new HashMap<>();
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();

        Runtime rt = Runtime.getRuntime();
        long maxMem = rt.maxMemory();
        long totalMem = rt.totalMemory();
        long freeMem = rt.freeMemory();
        long usedMem = totalMem - freeMem;

        double cpuLoad = -1;
        try {
            if (osBean instanceof com.sun.management.OperatingSystemMXBean sun) {
                cpuLoad = sun.getCpuLoad() * 100;
            }
        } catch (Exception ignored) {
        }

        Map<String, Object> server = new HashMap<>();
        server.put("osName", osBean.getName());
        server.put("osArch", osBean.getArch());
        server.put("osVersion", osBean.getVersion());
        server.put("availableProcessors", osBean.getAvailableProcessors());
        server.put("cpuLoad", Math.round(cpuLoad * 100.0) / 100.0);

        File root = new File("/");
        server.put("diskTotal", root.getTotalSpace());
        server.put("diskFree", root.getFreeSpace());
        server.put("diskUsed", root.getTotalSpace() - root.getFreeSpace());

        Map<String, Object> jvm = new HashMap<>();
        jvm.put("jvmName", runtimeBean.getVmName());
        jvm.put("javaVersion", runtimeBean.getSpecVersion());
        jvm.put("maxMemory", maxMem);
        jvm.put("totalMemory", totalMem);
        jvm.put("usedMemory", usedMem);
        jvm.put("freeMemory", freeMem);
        jvm.put("memoryUsage", Math.round((usedMem * 100.0) / maxMem * 100.0) / 100.0);
        jvm.put("threadCount", ManagementFactory.getThreadMXBean().getThreadCount());
        jvm.put("startTime", new Date(runtimeBean.getStartTime()).toString());
        jvm.put("uptime", runtimeBean.getUptime());

        data.put("server", server);
        data.put("jvm", jvm);
        data.put("onlineCount", onlineUserRegistry.count());
        return data;
    }

    public List<OnlineUserRegistry.OnlineUser> onlineUsers() {
        return onlineUserRegistry.list();
    }
}
