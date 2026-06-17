package com.example.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.music.dto.CategoryRequest;
import com.example.music.entity.Category;
import com.example.music.mapper.CategoryMapper;
import com.example.music.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Override
    public List<Category> listAll() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSortOrder);
        return this.list(wrapper);
    }

    @Override
    public Category getById(Long id) {
        Category category = this.baseMapper.selectById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        return category;
    }

    @Override
    public Category create(CategoryRequest request) {
        // 检查重名
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getName, request.getName());
        if (this.baseMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("分类名称已存在");
        }
        Category category = new Category();
        BeanUtils.copyProperties(request, category);
        this.save(category);
        return category;
    }

    @Override
    public Category update(Long id, CategoryRequest request) {
        Category category = this.getById(id);
        // 如果修改了名称，检查重名
        if (!category.getName().equals(request.getName())) {
            LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Category::getName, request.getName());
            if (this.baseMapper.selectCount(wrapper) > 0) {
                throw new RuntimeException("分类名称已存在");
            }
        }
        BeanUtils.copyProperties(request, category);
        this.updateById(category);
        return category;
    }

    @Override
    public void delete(Long id) {
        // 检查是否有歌曲关联此分类（可选，建议先检查）
        // 如果没有关联，直接删除
        this.removeById(id);
    }
}