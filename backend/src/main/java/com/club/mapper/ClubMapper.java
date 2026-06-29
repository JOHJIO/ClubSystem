package com.club.mapper;

import com.club.entity.Club;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClubMapper {
    List<Club> selectList(@Param("keyword") String keyword);
    Club selectById(Integer id);
    int insert(Club club);
    int update(Club club);
    int deleteById(Integer id);
}