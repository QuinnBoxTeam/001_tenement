package com.quinn.tenement.controller;

import com.quinn.tenement.entity.House;
import com.quinn.tenement.entity.Message;
import com.quinn.tenement.entity.User;
import com.quinn.tenement.service.HouseService;
import com.quinn.tenement.service.MessageService;
import com.quinn.tenement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    /**
     * 发送消息
     */
    @PostMapping("/send")
    @ResponseBody
    public String sendMessage(@RequestParam Long receiverId,
                            @RequestParam String content,
                            @RequestParam Long houseId,
                            HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "请先登录";
        }
        messageService.sendMessage(user.getId(), receiverId, content, houseId);
        return "success";
    }

    /**
     * 消息列表页面
     */
    @Autowired
    private HouseService houseService;

    @GetMapping("/list")
    public String messageList(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<Message> messages = messageService.getUserMessages(user.getId());
        
        // 按房源和用户分组，只保留每组最新的消息
        Map<String, Message> latestMessages = new HashMap<>();
        messages.forEach(message -> {
            Long otherUserId = message.getSenderId().equals(user.getId()) ? message.getReceiverId() : message.getSenderId();
            String key = message.getHouseId() + "-" + otherUserId;
            Message existingMessage = latestMessages.get(key);
            if (existingMessage == null || message.getCreateTime().isAfter(existingMessage.getCreateTime())) {
                // 获取房源信息
                House house = houseService.findHouseById(message.getHouseId());
                message.setHouseTitle(house != null ? house.getTitle() : "未知房源");
                
                // 获取该房源-用户组合的消息数量
                long messageCount = messages.stream()
                    .filter(m -> m.getHouseId().equals(message.getHouseId()) &&
                            ((m.getSenderId().equals(user.getId()) && m.getReceiverId().equals(otherUserId)) ||
                             (m.getReceiverId().equals(user.getId()) && m.getSenderId().equals(otherUserId))))
                    .count();
                message.setMessageCount(messageCount);
                
                latestMessages.put(key, message);
            }
        });
        
        model.addAttribute("messages", new ArrayList<>(latestMessages.values()));
        return "message/list";
    }

    /**
     * 查看与特定用户的对话历史
     */
    @GetMapping("/conversation/{userId}/{houseId}")
    public String viewConversation(@PathVariable Long userId, @PathVariable Long houseId,
                                 Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/user/login";
        }

        User otherUser = userService.findById(userId);
        List<Message> messages = messageService.getConversation(currentUser.getId(), userId, houseId);

        // 将未读消息标记为已读
        messages.stream()
                .filter(message -> message.getReceiverId().equals(currentUser.getId()) && !message.getIsRead())
                .forEach(message -> messageService.markAsRead(message.getId()));

        model.addAttribute("otherUser", otherUser);
        model.addAttribute("messages", messages);
        model.addAttribute("otherUserId", userId);
        model.addAttribute("houseId", houseId);
        return "message/conversation";
    }

    /**
     * 标记消息为已读
     */
    @PostMapping("/markAsRead/{messageId}")
    @ResponseBody
    public String markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return "success";
    }
}