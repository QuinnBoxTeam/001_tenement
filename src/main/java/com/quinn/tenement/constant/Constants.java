package com.quinn.tenement.constant;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
public class Constants {
    // 租赁申请状态
    public static final String APPLICATION_STATUS_PENDING = "pending";
    public static final String APPLICATION_STATUS_APPROVED = "approved";
    public static final String APPLICATION_STATUS_REJECTED = "rejected";

    // 房源状态
    public static final String HOUSE_STATUS_AVAILABLE = "available";
    public static final String HOUSE_STATUS_RENTED = "rented";
    public static final String HOUSE_STATUS_OFFLINE = "offline";

    // 分页配置
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 50;

    // 文件上传配置
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif"};

    // 时间格式
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // 系统配置
    public static final String SYSTEM_NAME = "Quinn 原创";
    public static final String DEFAULT_ENCODING = "UTF-8";
}