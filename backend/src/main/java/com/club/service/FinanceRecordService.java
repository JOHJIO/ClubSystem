package com.club.service;

import com.club.common.PageResult;
import com.club.entity.FinanceRecord;
import com.club.exception.BusinessException;
import com.club.mapper.FinanceRecordMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FinanceRecordService {
    @Autowired
    private FinanceRecordMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<FinanceRecord> rows = mapper.selectList(keyword);
        PageInfo<FinanceRecord> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<FinanceRecord> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public FinanceRecord getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(FinanceRecord financeRecord) {
        mapper.insert(financeRecord);
    }

    @Transactional
    public void update(Integer id, FinanceRecord financeRecord) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        financeRecord.setId(id);
        mapper.update(financeRecord);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}