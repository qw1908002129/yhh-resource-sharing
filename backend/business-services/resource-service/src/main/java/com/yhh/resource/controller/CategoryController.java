package com.yhh.resource.controller;

import com.yhh.common.result.Result;
import com.yhh.resource.entity.Category;
import com.yhh.resource.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类控制器
 */
@Tag(name = "分类管理", description = "分类相关接口")
@RestController
@RequestMapping("/resource/category")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Operation(summary = "获取所有分类")
    @GetMapping("/list")
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }
    
    @Operation(summary = "根据父分类ID获取子分类")
    @GetMapping("/children/{parentId}")
    public Result<List<Category>> getCategoriesByParentId(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getCategoriesByParentId(parentId);
        return Result.success(categories);
    }
    
    @Operation(summary = "创建分类")
    @PostMapping
    public Result<Category> createCategory(@RequestBody Category category) {
        Category created = categoryService.createCategory(category);
        return Result.success(created);
    }
    
    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public Result<Category> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        Category updated = categoryService.updateCategory(id, category);
        return Result.success(updated);
    }
    
    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return Result.success();
    }
} 