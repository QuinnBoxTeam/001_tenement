package com.quinn.tenement.controller;

import com.quinn.tenement.entity.House;
import com.quinn.tenement.entity.User;
import com.quinn.tenement.service.HouseService;
import com.quinn.tenement.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Controller
@RequestMapping("/house")
public class HouseController {
    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private HouseService houseService;

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String listHouses(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        List<House> houses;
        if (user != null) {
            houses = houseService.findAvailableHousesExceptUser(user.getId());
        } else {
            houses = houseService.findAvailableHouses();
        }
        model.addAttribute("houses", houses);
        return "house/list";
    }

    @GetMapping("/publish")
    public String publishPage(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        return "house/publish";
    }

    @PostMapping("/publish")
    public String publish(House house, @RequestParam("imageFile") MultipartFile imageFile,
                         HttpSession session, RedirectAttributes attributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        if (!imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                house.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                attributes.addFlashAttribute("error", "图片上传失败");
                return "redirect:/house/publish";
            }
        }

        house.setUserId(user.getId());
        house.setCreateTime(LocalDateTime.now());
        houseService.saveHouse(house);
        attributes.addFlashAttribute("message", "发布成功");
        return "redirect:/house/list";
    }

    @GetMapping("/my")
    public String myHouses(HttpSession session, Model model, @RequestParam(required = false) String status) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        List<House> houses = houseService.findHousesByUserId(user.getId());
        if (status != null && !status.isEmpty()) {
            houses = houses.stream()
                    .filter(house -> status.equals(house.getStatus()))
                    .toList();
        }
        model.addAttribute("houses", houses);
        model.addAttribute("selectedStatus", status);
        return "house/my";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model, HttpSession session) {
        House house = houseService.findHouseById(id);
        if (house == null) {
            return "redirect:/house/list";
        }

        User landlord = userService.findById(house.getUserId());
        model.addAttribute("house", house);
        model.addAttribute("landlord", landlord);
        return "house/detail";
    }

    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        House house = houseService.findHouseById(id);
        if (house == null || !house.getUserId().equals(user.getId())) {
            return "redirect:/house/my";
        }

        model.addAttribute("house", house);
        return "house/edit";
    }

    @PostMapping("/edit")
    public String edit(House house, @RequestParam("imageFile") MultipartFile imageFile,
                      HttpSession session, RedirectAttributes attributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        House existingHouse = houseService.findHouseById(house.getId());
        if (existingHouse == null || !existingHouse.getUserId().equals(user.getId())) {
            return "redirect:/house/my";
        }

        if (!imageFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath);
                house.setImageUrl("/uploads/" + fileName);
            } catch (IOException e) {
                attributes.addFlashAttribute("error", "图片上传失败");
                return "redirect:/house/edit/" + house.getId();
            }
        } else {
            house.setImageUrl(existingHouse.getImageUrl());
        }

        house.setUserId(existingHouse.getUserId());
        house.setCreateTime(existingHouse.getCreateTime());
        houseService.saveHouse(house);
        attributes.addFlashAttribute("message", "修改成功");
        return "redirect:/house/my";
    }
}