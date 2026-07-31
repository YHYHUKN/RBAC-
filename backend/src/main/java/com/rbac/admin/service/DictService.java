package com.rbac.admin.service;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.entity.DictData;
import com.rbac.admin.entity.DictType;
import com.rbac.admin.repository.DictDataRepository;
import com.rbac.admin.repository.DictTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictService {

    private final DictTypeRepository typeRepository;
    private final DictDataRepository dataRepository;

    public DictService(DictTypeRepository typeRepository, DictDataRepository dataRepository) {
        this.typeRepository = typeRepository;
        this.dataRepository = dataRepository;
    }

    public PageResult<DictType> listTypes(String keyword, int page, int size) {
        Specification<DictType> spec = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (StringUtils.hasText(keyword)) {
                list.add(cb.or(
                        cb.like(root.get("name"), "%" + keyword + "%"),
                        cb.like(root.get("type"), "%" + keyword + "%")
                ));
            }
            return cb.and(list.toArray(new Predicate[0]));
        };
        Page<DictType> p = typeRepository.findAll(spec, PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id")));
        return new PageResult<>(p.getTotalElements(), p.getContent());
    }

    public List<DictData> listData(String type) {
        return dataRepository.findByTypeOrderBySortAsc(type);
    }

    public DictType saveType(DictType t) {
        if (t.getId() == null && typeRepository.existsByType(t.getType())) {
            throw new RuntimeException("字典类型已存在");
        }
        return typeRepository.save(t);
    }

    public DictData saveData(DictData d) {
        return dataRepository.save(d);
    }

    public void deleteType(Long id) {
        DictType t = typeRepository.findById(id).orElseThrow(() -> new RuntimeException("字典类型不存在"));
        long count = dataRepository.count();
        if (dataRepository.findByType(t.getType()).size() > 0) {
            throw new RuntimeException("请先删除该类型下的字典数据");
        }
        typeRepository.deleteById(id);
    }

    public void deleteData(Long id) {
        dataRepository.deleteById(id);
    }
}
