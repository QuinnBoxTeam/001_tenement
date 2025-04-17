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
@TableName("rental_records")
public class RentalRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long houseId;
    private Long renterId;
    private Long landlordId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;  // pending, approved, rejected, completed
    private LocalDateTime createTime;
    private String remark;
}