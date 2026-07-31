package com.rbac.admin.service;

import com.rbac.admin.common.OperationLog;
import com.rbac.admin.dto.SaveDeptRequest;
import com.rbac.admin.entity.Department;
import com.rbac.admin.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeptService {

    private final DepartmentRepository departmentRepository;

    public DeptService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> list() {
        return departmentRepository.findAll();
    }

    public List<Department> tree() {
        List<Department> all = departmentRepository.findAll();
        return buildTree(all, 0L);
    }

    private List<Department> buildTree(List<Department> all, Long parentId) {
        List<Department> result = new ArrayList<>();
        for (Department d : all) {
            if (parentId.equals(d.getParentId())) {
                result.add(d);
            }
        }
        result.sort(Comparator.comparingInt(d -> d.getSort() == null ? 0 : d.getSort()));
        return result;
    }

    @OperationLog("保存部门")
    public Department save(SaveDeptRequest req) {
        Department d;
        if (req.getId() != null) {
            d = departmentRepository.findById(req.getId()).orElse(new Department());
        } else {
            d = new Department();
        }
        d.setName(req.getName());
        d.setParentId(req.getParentId() == null ? 0L : req.getParentId());
        d.setSort(req.getSort() == null ? 0 : req.getSort());
        return departmentRepository.save(d);
    }

    @OperationLog("删除部门")
    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
