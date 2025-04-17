package com.quinn.tenement.mapper;

import com.quinn.tenement.entity.RentalApplication;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 创建时间: 2025-04-15
 * 作者: Quinn
 * 邮箱: c_qingyun2002@outlook.com
 */
@Mapper
public interface RentalApplicationMapper {
    @Insert("INSERT INTO rental_application (user_id, house_id, start_time, end_time, status, remark, create_time, update_time) " +
            "VALUES (#{userId}, #{houseId}, #{startTime}, #{endTime}, #{status}, #{remark}, #{createTime}, #{updateTime})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(RentalApplication application);

    @Select("SELECT * FROM rental_application WHERE id = #{id}")
    RentalApplication findById(Long id);

    @Select("SELECT * FROM rental_application WHERE user_id = #{userId}")
    List<RentalApplication> findByUserId(Long userId);

    @Select("SELECT * FROM rental_application WHERE house_id = #{houseId}")
    List<RentalApplication> findByHouseId(Long houseId);

    @Update("UPDATE rental_application SET status = #{status}, update_time = #{updateTime} WHERE id = #{id}")
    int updateStatus(RentalApplication application);

    @Select("SELECT * FROM rental_application WHERE house_id = #{houseId} AND status = 'approved'")
    RentalApplication findApprovedByHouseId(Long houseId);

    @Select("SELECT ra.* FROM rental_application ra INNER JOIN houses h ON ra.house_id = h.id WHERE h.user_id = #{landlordId}")
    List<RentalApplication> findByLandlordId(Long landlordId);

    @Select("SELECT * FROM rental_application")
    List<RentalApplication> findAll();
}