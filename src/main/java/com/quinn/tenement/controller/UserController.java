package com.quinn.tenement.controller;

import com.quinn.tenement.entity.User;
import com.quinn.tenement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerPage() {
        return "user/register";
    }

    @PostMapping("/register")
    public String register(User user, RedirectAttributes attributes) {
        try {
            userService.register(user);
            attributes.addFlashAttribute("message", "注册成功，请登录");
            return "redirect:/user/login";
        } catch (RuntimeException e) {
            attributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session, RedirectAttributes attributes) {
        User user = userService.login(username, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/";
        }
        attributes.addFlashAttribute("error", "用户名或密码错误");
        return "redirect:/user/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}