package com.quinn.tenement.service;

import com.quinn.tenement.entity.House;

import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
public interface HouseService {
    House saveHouse(House house);
    List<House> findAllHouses();
    List<House> findAllHousesExceptUser(Long userId);
    List<House> findHousesByUserId(Long userId);
    House findHouseById(Long id);
    List<House> findAvailableHouses();
    List<House> findAvailableHousesExceptUser(Long userId);
    boolean updateHouseStatus(Long id, String status, Long renterId);
}