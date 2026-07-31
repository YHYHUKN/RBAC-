package com.rbac.admin.service;

import com.rbac.admin.common.OperationLog;
import com.rbac.admin.common.OnlineUserRegistry;
import com.rbac.admin.common.SecurityUtils;
import com.rbac.admin.config.JwtUtil;
import com.rbac.admin.dto.LoginResponse;
import com.rbac.admin.dto.UserInfo;
import com.rbac.admin.dto.MenuVO;
import com.rbac.admin.entity.Menu;
import com.rbac.admin.entity.Role;
import com.rbac.admin.entity.User;
import com.rbac.admin.repository.RoleRepository;
import com.rbac.admin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final MenuService menuService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final OnlineUserRegistry onlineUserRegistry;

    public AuthService(UserRepository userRepository, MenuService menuService, JwtUtil jwtUtil,
                       PasswordEncoder passwordEncoder, OnlineUserRegistry onlineUserRegistry) {
        this.userRepository = userRepository;
        this.menuService = menuService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.onlineUserRegistry = onlineUserRegistry;
    }

    public LoginResponse login(String username, String password, String ip) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        List<String> roleCodes = user.getRoles().stream().map(Role::getCode).toList();
        String token = jwtUtil.generateToken(username, roleCodes);
        onlineUserRegistry.register(token, username, ip);
        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUser(buildUserInfo(user, roleCodes));
        return resp;
    }

    public void logout(String token) {
        if (token != null) {
            onlineUserRegistry.removeByToken(token.replace("Bearer ", ""));
        }
    }

    public UserInfo info(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        List<String> roleCodes = user.getRoles().stream().map(Role::getCode).toList();
        return buildUserInfo(user, roleCodes);
    }

    public UserInfo currentInfo() {
        return info(SecurityUtils.currentUsername());
    }

    private UserInfo buildUserInfo(User user, List<String> roleCodes) {
        UserInfo info = new UserInfo();
        info.setUsername(user.getUsername());
        info.setName(user.getName());
        info.setRoles(roleCodes);
        Set<Menu> menus = new LinkedHashSet<>();
        if (user.getRoles() != null) {
            user.getRoles().forEach(r -> {
                if (r.getMenus() != null) menus.addAll(r.getMenus());
            });
        }
        info.setMenus(menuService.buildTree(new ArrayList<>(menus), 0L));
        return info;
    }
}
