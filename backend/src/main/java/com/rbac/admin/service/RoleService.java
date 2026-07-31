package com.rbac.admin.service;

import com.rbac.admin.common.OperationLog;
import com.rbac.admin.dto.SaveRoleRequest;
import com.rbac.admin.entity.Menu;
import com.rbac.admin.entity.Role;
import com.rbac.admin.repository.MenuRepository;
import com.rbac.admin.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final MenuRepository menuRepository;

    public RoleService(RoleRepository roleRepository, MenuRepository menuRepository) {
        this.roleRepository = roleRepository;
        this.menuRepository = menuRepository;
    }

    public List<Role> list() {
        return roleRepository.findAll();
    }

    public Role get(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @OperationLog("保存角色")
    public Role save(SaveRoleRequest req) {
        Role r;
        if (req.getId() != null) {
            r = roleRepository.findById(req.getId()).orElse(new Role());
        } else {
            r = new Role();
            if (roleRepository.findByCode(req.getCode()).isPresent()) {
                throw new RuntimeException("角色编码已存在");
            }
        }
        r.setCode(req.getCode());
        r.setName(req.getName());
        r.setRemark(req.getRemark());
        if (req.getMenuIds() != null) {
            Set<Menu> menus = new HashSet<>();
            req.getMenuIds().forEach(mid -> menuRepository.findById(mid).ifPresent(menus::add));
            r.setMenus(menus);
        }
        return roleRepository.save(r);
    }

    @OperationLog("删除角色")
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }
}
