package com.quinn.tenement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Data
@TableName("houses")
public class House {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String location;
    private Long userId;
    private String contactInfo;
    private String imageUrl;
    private LocalDateTime createTime;
    private String status = "available";  // available, rented
    private Long renterId;
}