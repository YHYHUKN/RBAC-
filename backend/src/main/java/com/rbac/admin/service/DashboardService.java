package com.rbac.admin.service;

import com.rbac.admin.entity.Department;
import com.rbac.admin.entity.OperationLog;
import com.rbac.admin.entity.Role;
import com.rbac.admin.entity.User;
import com.rbac.admin.repository.DepartmentRepository;
import com.rbac.admin.repository.OperationLogRepository;
import com.rbac.admin.repository.RoleRepository;
import com.rbac.admin.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final OperationLogRepository logRepository;
    private final MenuService menuService;

    public DashboardService(UserRepository userRepository, RoleRepository roleRepository,
                            DepartmentRepository departmentRepository, OperationLogRepository logRepository,
                            MenuService menuService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.departmentRepository = departmentRepository;
        this.logRepository = logRepository;
        this.menuService = menuService;
    }

    public Map<String, Object> stats() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", userRepository.count());
        data.put("roleCount", roleRepository.count());
        data.put("deptCount", departmentRepository.count());
        data.put("menuCount", (long) menuService.findAll().size());
        data.put("logCount", logRepository.count());

        List<Map<String, Object>> roleDist = new ArrayList<>();
        List<User> users = userRepository.findAll();
        for (Role r : roleRepository.findAll()) {
            long cnt = users.stream().filter(u -> u.getRoles() != null &&
                    u.getRoles().stream().anyMatch(rl -> rl.getId().equals(r.getId()))).count();
            Map<String, Object> m = new HashMap<>();
            m.put("name", r.getName());
            m.put("value", cnt);
            roleDist.add(m);
        }
        data.put("roleDistribution", roleDist);

        List<Map<String, Object>> deptDist = new ArrayList<>();
        for (Department d : departmentRepository.findAll()) {
            long cnt = users.stream().filter(u -> d.getId().equals(u.getDeptId())).count();
            Map<String, Object> m = new HashMap<>();
            m.put("name", d.getName());
            m.put("value", cnt);
            deptDist.add(m);
        }
        data.put("deptDistribution", deptDist);

        List<OperationLog> recent = logRepository.findAllByOrderByCreateTimeDesc().stream().limit(8).toList();
        data.put("recentLogs", recent);

        return data;
    }
}
