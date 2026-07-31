package com.rbac.admin.controller;

import com.rbac.admin.common.PageResult;
import com.rbac.admin.common.Result;
import com.rbac.admin.entity.DictData;
import com.rbac.admin.entity.DictType;
import com.rbac.admin.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dict")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping("/types")
    public Result<PageResult<DictType>> listTypes(@RequestParam(defaultValue = "") String keyword,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return Result.success(dictService.listTypes(keyword, page, size));
    }

    @GetMapping("/data")
    public Result<List<DictData>> listData(@RequestParam String type) {
        return Result.success(dictService.listData(type));
    }

    @PostMapping("/type")
    public Result<DictType> saveType(@RequestBody DictType t) {
        return Result.success(dictService.saveType(t));
    }

    @PostMapping("/data")
    public Result<DictData> saveData(@RequestBody DictData d) {
        return Result.success(dictService.saveData(d));
    }

    @DeleteMapping("/type/{id}")
    public Result<Void> deleteType(@PathVariable Long id) {
        dictService.deleteType(id);
        return Result.success();
    }

    @DeleteMapping("/data/{id}")
    public Result<Void> deleteData(@PathVariable Long id) {
        dictService.deleteData(id);
        return Result.success();
    }
}
