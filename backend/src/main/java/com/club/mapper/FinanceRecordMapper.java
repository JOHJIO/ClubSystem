package com.club.mapper;

import com.club.entity.FinanceRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FinanceRecordMapper {
    List<FinanceRecord> selectList(@Param("keyword") String keyword);
    FinanceRecord selectById(Integer id);
    int insert(FinanceRecord financeRecord);
    int update(FinanceRecord financeRecord);
    int deleteById(Integer id);
}