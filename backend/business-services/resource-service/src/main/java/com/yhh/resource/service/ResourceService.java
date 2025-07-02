package com.yhh.resource.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhh.resource.entity.Resource;
import com.yhh.resource.vo.ResourceVO;

/**
 * 资源服务接口
 */
public interface ResourceService extends IService<Resource> {
    
    /**
     * 分页查询资源列表
     */
    IPage<ResourceVO> getResourcePage(Integer pageNum, Integer pageSize, String keyword, Long categoryId);
    
    /**
     * 根据ID获取资源详情
     */
    ResourceVO getResourceById(Long id);
    
    /**
     * 创建资源
     */
    ResourceVO createResource(Resource resource);
    
    /**
     * 更新资源
     */
    ResourceVO updateResource(Long id, Resource resource);
    
    /**
     * 删除资源
     */
    void deleteResource(Long id);
    
    /**
     * 增加浏览次数
     */
    void incrementViewCount(Long id);
    
    /**
     * 增加下载次数
     */
    void incrementDownloadCount(Long id);
    
    /**
     * 获取推荐资源
     */
    IPage<ResourceVO> getRecommendResources(Integer pageNum, Integer pageSize);
    
    /**
     * 获取热门资源
     */
    IPage<ResourceVO> getHotResources(Integer pageNum, Integer pageSize);
} 