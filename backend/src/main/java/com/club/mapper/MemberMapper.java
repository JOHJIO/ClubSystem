package com.club.mapper;

import com.club.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {
    List<Member> selectList(@Param("keyword") String keyword);
    Member selectById(Integer id);
    int insert(Member member);
    int update(Member member);
    int deleteById(Integer id);
}