package com.yhh.resource.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhh.common.result.Result;
import com.yhh.resource.entity.Resource;
import com.yhh.resource.service.ResourceService;
import com.yhh.resource.vo.ResourceVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 资源控制器
 */
@Slf4j
@Tag(name = "资源管理", description = "资源相关接口")
@RestController
@RequestMapping("/resource")
public class ResourceController {
    
    @Autowired
    private ResourceService resourceService;
    
    @Value("${upload.path}")
    private String uploadPath;
    
    @Value("${upload.allowed-types}")
    private String allowedTypes;
    
    @Value("${upload.url-prefix}")
    private String urlPrefix;

    @Operation(summary = "分页查询资源列表")
    @GetMapping("/list")
    public Result<IPage<ResourceVO>> getResourceList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "12") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId) {
        IPage<ResourceVO> page = resourceService.getResourcePage(pageNum, pageSize, keyword, categoryId);
        return Result.success(page);
    }
    
    @Operation(summary = "获取资源详情")
    @GetMapping("/{id}")
    public Result<ResourceVO> getResourceById(@PathVariable Long id) {
        ResourceVO resource = resourceService.getResourceById(id);
        return Result.success(resource);
    }
    
    @Operation(summary = "上传资源封面")
    @PostMapping("/upload/cover")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> uploadCover(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !isAllowedFileType(contentType)) {
                return Result.failed("不支持的文件类型，仅支持: " + allowedTypes);
            }

            // 检查文件大小（限制为2MB）
            if (file.getSize() > 2 * 1024 * 1024) {
                return Result.failed("图片大小不能超过2MB");
            }

            // 生成日期目录
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            Path dateDir = Paths.get(uploadPath, datePath);
            if (!Files.exists(dateDir)) {
                Files.createDirectories(dateDir);
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : ".jpg";
            String filename = UUID.randomUUID() + extension;

            // 保存文件
            Path filePath = dateDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath);

            // 返回文件访问URL
            String fileUrl = urlPrefix + datePath + "/" + filename;
            log.info("File uploaded successfully: {}", fileUrl);
            return Result.success(fileUrl);

        } catch (IOException e) {
            log.error("File upload failed", e);
            return Result.failed("文件上传失败: " + e.getMessage());
        }
    }
    
    private boolean isAllowedFileType(String contentType) {
        List<String> allowed = Arrays.asList(allowedTypes.split(","));
        return allowed.contains(contentType.toLowerCase());
    }
    
    @Operation(summary = "创建资源")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<ResourceVO> createResource(@RequestBody Resource resource) {
        ResourceVO created = resourceService.createResource(resource);
        return Result.success(created);
    }
    
    @Operation(summary = "更新资源")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<ResourceVO> updateResource(@PathVariable Long id, @RequestBody Resource resource) {
        ResourceVO updated = resourceService.updateResource(id, resource);
        return Result.success(updated);
    }
    
    @Operation(summary = "删除资源")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return Result.success();
    }
    
    @Operation(summary = "增加下载次数")
    @PostMapping("/{id}/download")
    public Result<Void> incrementDownloadCount(@PathVariable Long id) {
        resourceService.incrementDownloadCount(id);
        return Result.success();
    }
    
    @Operation(summary = "获取推荐资源")
    @GetMapping("/recommend")
    public Result<IPage<ResourceVO>> getRecommendResources(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "6") Integer pageSize) {
        IPage<ResourceVO> page = resourceService.getRecommendResources(pageNum, pageSize);
        return Result.success(page);
    }
    
    @Operation(summary = "获取热门资源")
    @GetMapping("/hot")
    public Result<IPage<ResourceVO>> getHotResources(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "6") Integer pageSize) {
        IPage<ResourceVO> page = resourceService.getHotResources(pageNum, pageSize);
        return Result.success(page);
    }
} 