package com.quinn.tenement.service;

import com.quinn.tenement.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
public interface MessageService extends IService<Message> {
    /**
     * 发送消息
     */
    void sendMessage(Long senderId, Long receiverId, String content, Long houseId);

    /**
     * 获取用户的消息列表
     */
    List<Message> getUserMessages(Long userId);

    /**
     * 获取两个用户之间的对话记录
     */
    List<Message> getConversation(Long userId1, Long userId2, Long houseId);

    /**
     * 将消息标记为已读
     */
    void markAsRead(Long messageId);
}