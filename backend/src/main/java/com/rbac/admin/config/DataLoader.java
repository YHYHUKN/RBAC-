package com.rbac.admin.config;

import com.rbac.admin.entity.*;
import com.rbac.admin.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;
    private final DepartmentRepository departmentRepository;
    private final OperationLogRepository logRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(UserRepository userRepository, RoleRepository roleRepository,
                      MenuRepository menuRepository, DepartmentRepository departmentRepository,
                      OperationLogRepository logRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
        this.departmentRepository = departmentRepository;
        this.logRepository = logRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        Menu dashboard = saveMenu(0L, "仪表盘", 1, "/dashboard", "views/dashboard/index", "Odometer", null, 1);
        Menu sys = saveMenu(0L, "系统管理", 0, "/system", null, "Setting", null, 2);
        Menu userMenu = saveMenu(sys.getId(), "用户管理", 1, "/system/user", "views/system/user", "User", "sys:user", 1);
        Menu roleMenu = saveMenu(sys.getId(), "角色管理", 1, "/system/role", "views/system/role", "Role", "sys:role", 2);
        Menu menuMenu = saveMenu(sys.getId(), "菜单管理", 1, "/system/menu", "views/system/menu", "Menu", "sys:menu", 3);
        Menu deptMenu = saveMenu(sys.getId(), "部门管理", 1, "/system/dept", "views/system/dept", "Dept", "sys:dept", 4);
        Menu logDir = saveMenu(0L, "日志管理", 0, "/logs", null, "Document", null, 3);
        Menu opLog = saveMenu(logDir.getId(), "操作日志", 1, "/logs/operation", "views/logs/operation", "Log", "sys:log", 1);

        Role admin = new Role();
        admin.setCode("ADMIN");
        admin.setName("管理员");
        admin.setRemark("超级管理员，拥有全部权限");
        admin.setMenus(new HashSet<>(Arrays.asList(dashboard, sys, userMenu, roleMenu, menuMenu, deptMenu, logDir, opLog)));
        roleRepository.save(admin);

        Role userRole = new Role();
        userRole.setCode("USER");
        userRole.setName("普通用户");
        userRole.setRemark("仅可查看仪表盘");
        userRole.setMenus(new HashSet<>(Collections.singletonList(dashboard)));
        roleRepository.save(userRole);

        Department root = new Department();
        root.setName("总公司");
        root.setParentId(0L);
        root.setSort(1);
        root = departmentRepository.save(root);

        Department tech = new Department();
        tech.setName("技术部");
        tech.setParentId(root.getId());
        tech.setSort(1);
        tech = departmentRepository.save(tech);

        Department market = new Department();
        market.setName("市场部");
        market.setParentId(root.getId());
        market.setSort(2);
        market = departmentRepository.save(market);

        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("123456"));
        adminUser.setName("超级管理员");
        adminUser.setEmail("admin@example.com");
        adminUser.setPhone("13800000000");
        adminUser.setStatus(1);
        adminUser.setDeptId(tech.getId());
        adminUser.setRoles(new HashSet<>(Collections.singletonList(admin)));
        userRepository.save(adminUser);

        User normalUser = new User();
        normalUser.setUsername("user");
        normalUser.setPassword(passwordEncoder.encode("123456"));
        normalUser.setName("普通用户");
        normalUser.setStatus(1);
        normalUser.setDeptId(market.getId());
        normalUser.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        userRepository.save(normalUser);

        String[] names = {"张伟", "王芳", "李娜", "刘洋", "陈静"};
        for (int i = 0; i < names.length; i++) {
            User u = new User();
            u.setUsername("user" + (i + 1));
            u.setPassword(passwordEncoder.encode("123456"));
            u.setName(names[i]);
            u.setStatus(1);
            u.setDeptId(i % 2 == 0 ? tech.getId() : market.getId());
            u.setRoles(new HashSet<>(Collections.singletonList(userRole)));
            userRepository.save(u);
        }

        String[] actions = {"登录系统", "查询用户列表", "导出月度报表", "修改角色权限", "查看操作日志", "新增部门"};
        for (int i = 0; i < 14; i++) {
            OperationLog l = new OperationLog();
            l.setUsername(i % 2 == 0 ? "admin" : "user");
            l.setAction(actions[i % actions.length]);
            l.setMethod("GET");
            l.setParams("{}");
            l.setIp("127.0.0.1");
            l.setStatus("SUCCESS");
            logRepository.save(l);
        }
    }

    private Menu saveMenu(Long parentId, String name, int type, String path, String component, String icon, String permission, int sort) {
        Menu m = new Menu();
        m.setParentId(parentId);
        m.setName(name);
        m.setType(type);
        m.setPath(path);
        m.setComponent(component);
        m.setIcon(icon);
        m.setPermission(permission);
        m.setSort(sort);
        return menuRepository.save(m);
    }
}
