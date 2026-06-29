package com.club.mapper;

import com.club.entity.Venue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VenueMapper {
    List<Venue> selectList(@Param("keyword") String keyword);
    Venue selectById(Integer id);
    int insert(Venue venue);
    int update(Venue venue);
    int deleteById(Integer id);
}