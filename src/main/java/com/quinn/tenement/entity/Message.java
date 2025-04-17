package com.quinn.tenement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("messages")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private LocalDateTime createTime;
    private Boolean isRead = false;
    private Long houseId;  // 关联的房源ID
    
    @TableField(exist = false)
    private String houseTitle;  // 房源标题，用于显示
    
    @TableField(exist = false)
    private Long messageCount;  // 消息数量，用于显示
}