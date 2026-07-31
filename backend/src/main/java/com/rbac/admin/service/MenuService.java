package com.rbac.admin.service;

import com.rbac.admin.dto.MenuVO;
import com.rbac.admin.entity.Menu;
import com.rbac.admin.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuVO> tree() {
        return buildTree(menuRepository.findAllByOrderBySortAsc(), 0L);
    }

    public List<MenuVO> buildTree(List<Menu> all, Long parentId) {
        List<MenuVO> result = new ArrayList<>();
        for (Menu m : all) {
            if (parentId.equals(m.getParentId())) {
                MenuVO vo = toVO(m);
                vo.setChildren(buildTree(all, m.getId()));
                result.add(vo);
            }
        }
        result.sort(Comparator.comparingInt(v -> v.getSort() == null ? 0 : v.getSort()));
        return result;
    }

    public MenuVO toVO(Menu m) {
        MenuVO vo = new MenuVO();
        vo.setId(m.getId());
        vo.setParentId(m.getParentId());
        vo.setName(m.getName());
        vo.setType(m.getType());
        vo.setPath(m.getPath());
        vo.setComponent(m.getComponent());
        vo.setIcon(m.getIcon());
        vo.setPermission(m.getPermission());
        vo.setSort(m.getSort());
        return vo;
    }

    public List<Menu> findAll() {
        return menuRepository.findAllByOrderBySortAsc();
    }

    public Menu getById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public Menu save(Menu m) {
        return menuRepository.save(m);
    }

    public void delete(Long id) {
        menuRepository.deleteById(id);
    }
}
