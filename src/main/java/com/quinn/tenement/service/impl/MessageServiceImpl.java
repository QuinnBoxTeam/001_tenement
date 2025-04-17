package com.quinn.tenement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quinn.tenement.entity.Message;
import com.quinn.tenement.mapper.MessageMapper;
import com.quinn.tenement.service.MessageService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Override
    public void sendMessage(Long senderId, Long receiverId, String content, Long houseId) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setHouseId(houseId);
        message.setCreateTime(LocalDateTime.now());
        save(message);
    }

    @Override
    public List<Message> getUserMessages(Long userId) {
        return list(new LambdaQueryWrapper<Message>()
                .and(wrapper -> wrapper
                    .eq(Message::getSenderId, userId)
                    .or()
                    .eq(Message::getReceiverId, userId))
                .orderByDesc(Message::getCreateTime));
    }

    @Override
    public List<Message> getConversation(Long userId1, Long userId2, Long houseId) {
        return list(new LambdaQueryWrapper<Message>()
                .eq(Message::getHouseId, houseId)
                .and(wrapper -> wrapper
                    .and(w -> w
                        .eq(Message::getSenderId, userId1)
                        .eq(Message::getReceiverId, userId2))
                    .or(w -> w
                        .eq(Message::getSenderId, userId2)
                        .eq(Message::getReceiverId, userId1)))
                .orderByAsc(Message::getCreateTime));
    }

    @Override
    public void markAsRead(Long messageId) {
        Message message = getById(messageId);
        if (message != null) {
            message.setIsRead(true);
            updateById(message);
        }
    }
}