package com.yhh.resource.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yhh.resource.entity.Category;

import java.util.List;

/**
 * 分类服务接口
 */
public interface CategoryService extends IService<Category> {
    
    /**
     * 获取所有分类
     */
    List<Category> getAllCategories();
    
    /**
     * 根据父分类ID获取子分类
     */
    List<Category> getCategoriesByParentId(Long parentId);
    
    /**
     * 创建分类
     */
    Category createCategory(Category category);
    
    /**
     * 更新分类
     */
    Category updateCategory(Long id, Category category);
    
    /**
     * 删除分类
     */
    void deleteCategory(Long id);
} 