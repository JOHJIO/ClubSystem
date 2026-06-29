package com.club.mapper;

import com.club.entity.Announcement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnnouncementMapper {
    List<Announcement> selectList(@Param("keyword") String keyword);
    Announcement selectById(Integer id);
    int insert(Announcement announcement);
    int update(Announcement announcement);
    int deleteById(Integer id);
}