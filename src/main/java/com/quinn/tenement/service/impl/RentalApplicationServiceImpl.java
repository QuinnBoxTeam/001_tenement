package com.quinn.tenement.service.impl;

import com.quinn.tenement.entity.House;
import com.quinn.tenement.entity.RentalApplication;
import com.quinn.tenement.mapper.RentalApplicationMapper;
import com.quinn.tenement.service.HouseService;
import com.quinn.tenement.service.RentalApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Service
@Transactional
public class RentalApplicationServiceImpl implements RentalApplicationService {
    @Autowired
    private RentalApplicationMapper rentalApplicationMapper;

    @Override
    public RentalApplication submitApplication(RentalApplication application) {
        application.setStatus("pending");
        application.setCreateTime(LocalDateTime.now());
        application.setUpdateTime(LocalDateTime.now());
        rentalApplicationMapper.insert(application);
        return application;
    }

    @Override
    public RentalApplication findById(Long id) {
        return rentalApplicationMapper.findById(id);
    }

    @Override
    public List<RentalApplication> findByUserId(Long userId) {
        return rentalApplicationMapper.findByUserId(userId);
    }

    @Override
    public List<RentalApplication> findByHouseId(Long houseId) {
        return rentalApplicationMapper.findByHouseId(houseId);
    }

    @Autowired
    private HouseService houseService;

    @Override
    public boolean updateApplicationStatus(Long id, String status) {
        RentalApplication application = findById(id);
        if (application == null) {
            throw new IllegalArgumentException("租赁申请不存在");
        }

        // 检查当前申请状态
        if ("approved".equals(application.getStatus()) || "rejected".equals(application.getStatus())) {
            throw new IllegalStateException("该申请已经被处理过");
        }

        House house = houseService.findHouseById(application.getHouseId());
        if (house == null) {
            throw new IllegalStateException("房源不存在");
        }

        // 如果是批准申请，需要检查房源当前状态
        if ("approved".equals(status)) {
            if ("rented".equals(house.getStatus())) {
                throw new IllegalStateException("该房源已被租出");
            }
            // 检查是否有其他已批准的申请
            RentalApplication approvedApplication = findApprovedByHouseId(house.getId());
            if (approvedApplication != null && !approvedApplication.getId().equals(id)) {
                throw new IllegalStateException("该房源已有其他批准的申请");
            }
        }

        // 更新申请状态
        application.setStatus(status);
        application.setUpdateTime(LocalDateTime.now());
        boolean success = rentalApplicationMapper.updateStatus(application) > 0;
        
        if (!success) {
            throw new IllegalStateException("更新申请状态失败");
        }

        // 同步更新房源状态
        if ("approved".equals(status)) {
            houseService.updateHouseStatus(house.getId(), "rented", application.getUserId());
        }

        return true;
    }

    @Override
    public RentalApplication findApprovedByHouseId(Long houseId) {
        return rentalApplicationMapper.findApprovedByHouseId(houseId);
    }

    @Override
    public List<RentalApplication> findByLandlordId(Long landlordId) {
        return rentalApplicationMapper.findByLandlordId(landlordId);
    }

    @Override
    public List<RentalApplication> findAll() {
        return rentalApplicationMapper.findAll();
    }
}