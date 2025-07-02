package com.yhh.resource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhh.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 资源实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("resource")
public class Resource extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String description;
    
    private String content;
    
    private String coverImage;
    
    private String images;
    
    private Long categoryId;
    
    private String tags;
    
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
} 