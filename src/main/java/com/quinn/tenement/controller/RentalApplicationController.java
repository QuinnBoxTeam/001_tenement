package com.quinn.tenement.controller;

import com.quinn.tenement.constant.Constants;
import com.quinn.tenement.entity.RentalApplication;
import com.quinn.tenement.entity.User;
import com.quinn.tenement.service.RentalApplicationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Controller
@RequestMapping("/rental")
public class RentalApplicationController {
    @Autowired
    private RentalApplicationService rentalApplicationService;

    @PostMapping("/apply")
    public String apply(@RequestParam Long houseId,
                       @RequestParam String startTime,
                       @RequestParam String endTime,
                       @RequestParam(required = false) String remark,
                       HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        RentalApplication application = new RentalApplication();
        application.setUserId(user.getId());
        application.setHouseId(houseId);
        application.setStartTime(LocalDate.parse(startTime));
        application.setEndTime(LocalDate.parse(endTime));
        application.setRemark(remark);

        rentalApplicationService.submitApplication(application);
        return "redirect:/rental/my-applications";
    }

    @GetMapping("/my-applications")
    public String myApplications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<RentalApplication> rentalApplications = rentalApplicationService.findByUserId(user.getId());
        model.addAttribute("rentalApplications", rentalApplications);
        return "rental/my-applications";
    }

    @GetMapping("/house-applications")
    public String houseApplications(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<RentalApplication> rentalApplications = rentalApplicationService.findByLandlordId(user.getId());
        model.addAttribute("rentalApplications", rentalApplications);
        return "rental/house-applications";
    }

    @GetMapping("/house-applications/{houseId}")
    public String houseApplicationsByHouseId(@PathVariable Long houseId, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        List<RentalApplication> rentalApplications = rentalApplicationService.findByHouseId(houseId);
        model.addAttribute("rentalApplications", rentalApplications);
        return "rental/house-applications";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, @RequestParam("id") Long formId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        if (!id.equals(formId)) {
            throw new IllegalArgumentException("Invalid application ID");
        }

        boolean success = rentalApplicationService.updateApplicationStatus(id, Constants.APPLICATION_STATUS_APPROVED);

        return "redirect:/rental/house-applications";
    }

    @PostMapping("/reject/{id}")
    public String reject(@PathVariable Long id, @RequestParam("id") Long formId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }

        if (!id.equals(formId)) {
            throw new IllegalArgumentException("Invalid application ID");
        }

        boolean success = rentalApplicationService.updateApplicationStatus(id, Constants.APPLICATION_STATUS_REJECTED);
        return "redirect:/rental/house-applications";
    }
}