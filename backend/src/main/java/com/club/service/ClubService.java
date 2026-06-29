package com.club.service;

import com.club.common.PageResult;
import com.club.entity.Club;
import com.club.exception.BusinessException;
import com.club.mapper.ClubMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClubService {
    @Autowired
    private ClubMapper mapper;

    public PageResult page(int page, int size, String keyword) {
        PageHelper.startPage(page, size);
        List<Club> rows = mapper.selectList(keyword);
        PageInfo<Club> info = new PageInfo<>(rows);
        return new PageResult(info.getTotal(), rows);
    }

    public List<Club> list(String keyword) {
        return mapper.selectList(keyword);
    }

    public Club getById(Integer id) {
        return mapper.selectById(id);
    }

    @Transactional
    public void create(Club club) {
        mapper.insert(club);
    }

    @Transactional
    public void update(Integer id, Club club) {
        if (mapper.selectById(id) == null) {
            throw new BusinessException("数据不存在");
        }
        club.setId(id);
        mapper.update(club);
    }

    @Transactional
    public void delete(Integer id) {
        mapper.deleteById(id);
    }
}