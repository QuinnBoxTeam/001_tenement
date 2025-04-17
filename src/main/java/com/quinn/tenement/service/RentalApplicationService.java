package com.quinn.tenement.service;

import com.quinn.tenement.entity.RentalApplication;

import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
public interface RentalApplicationService {
    RentalApplication submitApplication(RentalApplication application);
    
    RentalApplication findById(Long id);
    
    List<RentalApplication> findByUserId(Long userId);
    
    List<RentalApplication> findByHouseId(Long houseId);
    
    boolean updateApplicationStatus(Long id, String status);
    
    RentalApplication findApprovedByHouseId(Long houseId);
    
    List<RentalApplication> findByLandlordId(Long landlordId);
    
    List<RentalApplication> findAll();
}