package com.quinn.tenement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.quinn.tenement.entity.House;
import com.quinn.tenement.mapper.HouseMapper;
import com.quinn.tenement.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Service
public class HouseServiceImpl implements HouseService {
    @Autowired
    private HouseMapper houseMapper;

    @Override
    public House saveHouse(House house) {
        houseMapper.insert(house);
        return house;
    }

    @Override
    public List<House> findAllHouses() {
        return houseMapper.selectList(null);
    }

    @Override
    public List<House> findAllHousesExceptUser(Long userId) {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("user_id", userId);
        return houseMapper.selectList(queryWrapper);
    }

    @Override
    public List<House> findHousesByUserId(Long userId) {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return houseMapper.selectList(queryWrapper);
    }

    @Override
    public House findHouseById(Long id) {
        return houseMapper.selectById(id);
    }

    @Override
    public List<House> findAvailableHouses() {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "available");
        return houseMapper.selectList(queryWrapper);
    }

    @Override
    public List<House> findAvailableHousesExceptUser(Long userId) {
        QueryWrapper<House> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "available");
        queryWrapper.ne("user_id", userId);
        return houseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateHouseStatus(Long id, String status, Long renterId) {
        House house = findHouseById(id);
        if (house != null) {
            house.setStatus(status);
            house.setRenterId(renterId);
            return houseMapper.updateById(house) > 0;
        }
        return false;
    }
}