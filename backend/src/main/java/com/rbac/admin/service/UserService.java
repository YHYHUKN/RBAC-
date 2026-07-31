package com.rbac.admin.service;

import com.rbac.admin.common.OperationLog;
import com.rbac.admin.common.PageResult;
import com.rbac.admin.dto.SaveUserRequest;
import com.rbac.admin.entity.Role;
import com.rbac.admin.entity.User;
import com.rbac.admin.repository.RoleRepository;
import com.rbac.admin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public PageResult<User> list(int page, int size, String keyword) {
        List<User> all = userRepository.findAll();
        if (keyword != null && !keyword.isEmpty()) {
            String k = keyword.toLowerCase();
            all = all.stream().filter(u ->
                    (u.getUsername() != null && u.getUsername().toLowerCase().contains(k)) ||
                    (u.getName() != null && u.getName().toLowerCase().contains(k))).collect(Collectors.toList());
        }
        long total = all.size();
        int from = Math.min((page - 1) * size, all.size());
        int to = Math.min(from + size, all.size());
        return PageResult.of(total, all.subList(from, to));
    }

    public User get(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @OperationLog("保存用户")
    public User save(SaveUserRequest req) {
        User u;
        if (req.getId() != null) {
            u = userRepository.findById(req.getId()).orElse(new User());
        } else {
            u = new User();
            if (userRepository.existsByUsername(req.getUsername())) {
                throw new RuntimeException("用户名已存在");
            }
        }
        u.setUsername(req.getUsername());
        if (req.getPassword() != null && !req.getPassword().isEmpty()) {
            u.setPassword(passwordEncoder.encode(req.getPassword()));
        }
        u.setName(req.getName());
        u.setEmail(req.getEmail());
        u.setPhone(req.getPhone());
        if (req.getStatus() != null) u.setStatus(req.getStatus());
        u.setDeptId(req.getDeptId());
        if (req.getRoleIds() != null) {
            Set<Role> roles = new HashSet<>();
            req.getRoleIds().forEach(rid -> roleRepository.findById(rid).ifPresent(roles::add));
            u.setRoles(roles);
        }
        return userRepository.save(u);
    }

    @OperationLog("删除用户")
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
