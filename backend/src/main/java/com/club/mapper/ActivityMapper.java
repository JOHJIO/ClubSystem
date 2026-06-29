package com.club.mapper;

import com.club.entity.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityMapper {
    List<Activity> selectList(@Param("keyword") String keyword);
    Activity selectById(Integer id);
    int insert(Activity activity);
    int update(Activity activity);
    int deleteById(Integer id);
}