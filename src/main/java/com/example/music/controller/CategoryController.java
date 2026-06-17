package com.example.music.controller;

import com.example.music.common.Result;
import com.example.music.dto.CategoryRequest;
import com.example.music.dto.CategoryResponse;
import com.example.music.entity.Category;
import com.example.music.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")   // 注意：实际路径会拼接 /api + /categories
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 1. 获取所有分类（公开，不需要登录，前端下拉用）
    @GetMapping
    public Result<List<CategoryResponse>> listAll() {
        List<Category> categories = categoryService.listAll();
        List<CategoryResponse> responses = categories.stream().map(c -> {
            CategoryResponse resp = new CategoryResponse();
            BeanUtils.copyProperties(c, resp);
            return resp;
        }).collect(Collectors.toList());
        return Result.success(responses);
    }

    // 2. 获取单个分类详情
    @GetMapping("/{id}")
    public Result<CategoryResponse> getById(@PathVariable Long id) {
        Category category = categoryService.getById(id);
        CategoryResponse resp = new CategoryResponse();
        BeanUtils.copyProperties(category, resp);
        return Result.success(resp);
    }

    // 3. 创建分类（管理功能，建议需登录+管理员权限，这里为了演示先加上 token 校验）
    @PostMapping
    public Result<CategoryResponse> create(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           @Valid @RequestBody CategoryRequest request) {
        // 简单校验登录（复用你的 JWT 校验逻辑，可以提取工具类）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录，无权限操作");
        }
        // 可以再扩展 admin 权限判断（此处省略）
        try {
            Category category = categoryService.create(request);
            CategoryResponse resp = new CategoryResponse();
            BeanUtils.copyProperties(category, resp);
            return Result.success(resp);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    // 4. 更新分类
    @PutMapping("/{id}")
    public Result<CategoryResponse> update(@RequestHeader(value = "Authorization", required = false) String authHeader,
                                           @PathVariable Long id,
                                           @Valid @RequestBody CategoryRequest request) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录，无权限操作");
        }
        try {
            Category category = categoryService.update(id, request);
            CategoryResponse resp = new CategoryResponse();
            BeanUtils.copyProperties(category, resp);
            return Result.success(resp);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }

    // 5. 删除分类
    @DeleteMapping("/{id}")
    public Result<Void> delete(@RequestHeader(value = "Authorization", required = false) String authHeader,
                               @PathVariable Long id) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error(401, "未登录，无权限操作");
        }
        try {
            categoryService.delete(id);
            return Result.success(null);
        } catch (RuntimeException e) {
            return Result.error(400, e.getMessage());
        }
    }
}