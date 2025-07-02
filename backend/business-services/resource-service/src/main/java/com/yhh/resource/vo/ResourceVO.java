package com.yhh.resource.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 资源VO
 */
@Data
public class ResourceVO {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private String content;
    
    private String coverImage;
    
    private List<String> images;
    
    private Long categoryId;
    
    private String categoryName;
    
    private List<String> tags;
    
    private String downloadUrl;
    
    private String downloadPassword;
    
    private String fileSize;
    
    private String version;
    
    private String platform;
    
    private String language;
    
    private Integer status;
    
    private Integer isRecommend;
    
    private Integer isHot;
    
    private Long viewCount;
    
    private Long downloadCount;
    
    private Long likeCount;
    
    private Long commentCount;
    
    private Long createUserId;
    
    private String createUserName;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 