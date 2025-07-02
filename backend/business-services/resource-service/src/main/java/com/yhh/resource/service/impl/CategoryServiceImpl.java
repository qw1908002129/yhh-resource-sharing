package com.yhh.resource.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.resource.entity.Category;
import com.yhh.resource.mapper.CategoryMapper;
import com.yhh.resource.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 分类服务实现类
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    
    @Override
    public List<Category> getAllCategories() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, 1)
               .orderByAsc(Category::getSort);
        return list(wrapper);
    }
    
    @Override
    public List<Category> getCategoriesByParentId(Long parentId) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, parentId)
               .eq(Category::getStatus, 1)
               .orderByAsc(Category::getSort);
        return list(wrapper);
    }
    
    @Override
    public Category createCategory(Category category) {
        save(category);
        return category;
    }
    
    @Override
    public Category updateCategory(Long id, Category category) {
        category.setId(id);
        updateById(category);
        return category;
    }
    
    @Override
    public void deleteCategory(Long id) {
        // 检查是否有子分类
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getParentId, id);
        if (count(wrapper) > 0) {
            throw new RuntimeException("该分类下还有子分类，无法删除");
        }
        
        removeById(id);
    }
} 