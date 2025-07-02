package com.yhh.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhh.resource.entity.Category;
import com.yhh.resource.entity.Resource;
import com.yhh.resource.mapper.ResourceMapper;
import com.yhh.resource.service.CategoryService;
import com.yhh.resource.service.ResourceService;
import com.yhh.resource.vo.ResourceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源服务实现类
 */
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {
    
    @Autowired
    private CategoryService categoryService;
    
    @Override
    public IPage<ResourceVO> getResourcePage(Integer pageNum, Integer pageSize, String keyword, Long categoryId) {
        Page<Resource> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        
        // 关键词搜索
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(Resource::getTitle, keyword)
                  .or()
                  .like(Resource::getDescription, keyword);
        }
        
        // 分类筛选
        if (categoryId != null) {
            wrapper.eq(Resource::getCategoryId, categoryId);
        }
        
        // 只查询上架的资源
        wrapper.eq(Resource::getStatus, 1);
        wrapper.orderByDesc(Resource::getCreateTime);
        
        IPage<Resource> resourcePage = page(page, wrapper);
        
        // 转换为VO
        IPage<ResourceVO> voPage = new Page<>(pageNum, pageSize, resourcePage.getTotal());
        List<ResourceVO> voList = resourcePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    public ResourceVO getResourceById(Long id) {
        Resource resource = getById(id);
        if (resource == null) {
            return null;
        }
        
        // 增加浏览次数
        incrementViewCount(id);
        
        return convertToVO(resource);
    }
    
    @Override
    public ResourceVO createResource(Resource resource) {
        save(resource);
        return convertToVO(resource);
    }
    
    @Override
    public ResourceVO updateResource(Long id, Resource resource) {
        resource.setId(id);
        updateById(resource);
        return convertToVO(resource);
    }
    
    @Override
    public void deleteResource(Long id) {
        removeById(id);
    }
    
    @Override
    public void incrementViewCount(Long id) {
        // 使用Redis或数据库更新浏览次数
        Resource resource = getById(id);
        if (resource != null) {
            resource.setViewCount(resource.getViewCount() + 1);
            updateById(resource);
        }
    }
    
    @Override
    public void incrementDownloadCount(Long id) {
        Resource resource = getById(id);
        if (resource != null) {
            resource.setDownloadCount(resource.getDownloadCount() + 1);
            updateById(resource);
        }
    }
    
    @Override
    public IPage<ResourceVO> getRecommendResources(Integer pageNum, Integer pageSize) {
        Page<Resource> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getStatus, 1)
               .eq(Resource::getIsRecommend, 1)
               .orderByDesc(Resource::getCreateTime);
        
        IPage<Resource> resourcePage = page(page, wrapper);
        
        IPage<ResourceVO> voPage = new Page<>(pageNum, pageSize, resourcePage.getTotal());
        List<ResourceVO> voList = resourcePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    @Override
    public IPage<ResourceVO> getHotResources(Integer pageNum, Integer pageSize) {
        Page<Resource> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Resource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Resource::getStatus, 1)
               .eq(Resource::getIsHot, 1)
               .orderByDesc(Resource::getViewCount);
        
        IPage<Resource> resourcePage = page(page, wrapper);
        
        IPage<ResourceVO> voPage = new Page<>(pageNum, pageSize, resourcePage.getTotal());
        List<ResourceVO> voList = resourcePage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);
        
        return voPage;
    }
    
    /**
     * 转换为VO
     */
    private ResourceVO convertToVO(Resource resource) {
        ResourceVO vo = new ResourceVO();
        BeanUtil.copyProperties(resource, vo);
        
        // 处理图片列表
        if (StrUtil.isNotBlank(resource.getImages())) {
            try {
                List<String> images = JSONUtil.toList(resource.getImages(), String.class);
                vo.setImages(images);
            } catch (Exception e) {
                vo.setImages(Arrays.asList(resource.getImages().split(",")));
            }
        }
        
        // 处理标签列表
        if (StrUtil.isNotBlank(resource.getTags())) {
            vo.setTags(Arrays.asList(resource.getTags().split(",")));
        }
        
        // 获取分类名称
        if (resource.getCategoryId() != null) {
            Category category = categoryService.getById(resource.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }
        }
        
        return vo;
    }
} 