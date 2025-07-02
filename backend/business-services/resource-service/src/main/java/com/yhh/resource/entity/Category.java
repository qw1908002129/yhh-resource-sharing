package com.yhh.resource.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yhh.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分类实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("category")
public class Category extends BaseEntity {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String code;
    
    private Long parentId;
    
    private Integer sort;
    
    private String icon;
    
    private String description;
    
    private Integer status;
} 